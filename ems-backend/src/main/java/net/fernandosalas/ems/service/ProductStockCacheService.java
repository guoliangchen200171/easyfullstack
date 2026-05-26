package net.fernandosalas.ems.service;

import lombok.extern.slf4j.Slf4j;
import net.fernandosalas.ems.entity.ProductInventory;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProductStockCacheService {

    private static final String KEY_PREFIX = "ems:product:stock:";
    private static final long INSUFFICIENT_STOCK = -1L;

    private final StringRedisTemplate redisTemplate;
    private final ProductInventoryRepository inventoryRepository;
    private final DefaultRedisScript<Long> deductStockScript;
    private final boolean enabled;

    public ProductStockCacheService(
            StringRedisTemplate redisTemplate,
            ProductInventoryRepository inventoryRepository,
            @Value("${app.cache.product-stock.enabled:true}") boolean enabled) {
        this.redisTemplate = redisTemplate;
        this.inventoryRepository = inventoryRepository;
        this.enabled = enabled;
        this.deductStockScript = createDeductScript();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getStock(Long productId) {
        if (!enabled) {
            return loadStockFromDatabase(productId);
        }
        try {
            String key = keyFor(productId);
            String value = redisTemplate.opsForValue().get(key);
            if (value != null && !value.isBlank()) {
                return Integer.parseInt(value);
            }
            int stock = loadStockFromDatabase(productId);
            redisTemplate.opsForValue().set(key, String.valueOf(stock));
            return stock;
        } catch (Exception ex) {
            log.warn("Redis unavailable for stock read id {}, fallback to DB: {}", productId, ex.getMessage());
            return loadStockFromDatabase(productId);
        }
    }

    public Map<Long, Integer> getStocks(Collection<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Map.of();
        }
        List<Long> idList = productIds.stream().distinct().toList();
        if (!enabled) {
            return loadStocksFromDatabase(idList);
        }
        try {
            return getStocksFromRedis(idList);
        } catch (Exception ex) {
            log.warn("Redis unavailable for batch stock read, fallback to DB: {}", ex.getMessage());
            return loadStocksFromDatabase(idList);
        }
    }

    public void setStock(Long productId, int stock) {
        if (!enabled || productId == null) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(keyFor(productId), String.valueOf(stock));
        } catch (Exception ex) {
            log.warn("Failed to set stock cache for id {}: {}", productId, ex.getMessage());
        }
    }

    public void addStock(Long productId, int delta) {
        if (!enabled || productId == null) {
            return;
        }
        try {
            ensureStockCached(productId);
            redisTemplate.opsForValue().increment(keyFor(productId), delta);
        } catch (Exception ex) {
            log.warn("Failed to add stock cache for id {}: {}", productId, ex.getMessage());
        }
    }

    public void deleteStock(Long productId) {
        if (!enabled || productId == null) {
            return;
        }
        try {
            redisTemplate.delete(keyFor(productId));
        } catch (Exception ex) {
            log.warn("Failed to delete stock cache for id {}: {}", productId, ex.getMessage());
        }
    }

    /**
     * @return empty if Redis disabled/unavailable (caller should use MySQL);
     *         -1 if insufficient; otherwise remaining stock after deduct.
     */
    public Optional<Long> deductIfAvailable(Long productId, int quantity) {
        if (!enabled) {
            return Optional.empty();
        }
        try {
            ensureStockCached(productId);
            Long remaining = redisTemplate.execute(
                    deductStockScript,
                    List.of(keyFor(productId)),
                    String.valueOf(quantity));
            if (remaining == null || remaining == INSUFFICIENT_STOCK) {
                return Optional.of(INSUFFICIENT_STOCK);
            }
            return Optional.of(remaining);
        } catch (Exception ex) {
            log.warn("Redis unavailable for stock deduct id {}, fallback to DB: {}", productId, ex.getMessage());
            return Optional.empty();
        }
    }

    public void rollback(Long productId, int quantity) {
        if (!enabled || productId == null || quantity <= 0) {
            return;
        }
        try {
            redisTemplate.opsForValue().increment(keyFor(productId), quantity);
        } catch (Exception ex) {
            log.warn("Failed to rollback stock cache for id {}: {}", productId, ex.getMessage());
        }
    }

    private Map<Long, Integer> getStocksFromRedis(List<Long> idList) {
        List<String> keys = idList.stream().map(this::keyFor).toList();
        List<String> values = redisTemplate.opsForValue().multiGet(keys);

        Map<Long, Integer> result = new HashMap<>();
        List<Long> missedIds = new ArrayList<>();

        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            String value = values != null && i < values.size() ? values.get(i) : null;
            if (value != null && !value.isBlank()) {
                result.put(id, Integer.parseInt(value));
            } else {
                missedIds.add(id);
            }
        }

        if (!missedIds.isEmpty()) {
            Map<Long, Integer> fromDb = loadStocksFromDatabase(missedIds);
            for (Map.Entry<Long, Integer> entry : fromDb.entrySet()) {
                setStock(entry.getKey(), entry.getValue());
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    private void ensureStockCached(Long productId) {
        String key = keyFor(productId);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return;
        }
        int stock = loadStockFromDatabase(productId);
        redisTemplate.opsForValue().set(key, String.valueOf(stock));
    }

    private int loadStockFromDatabase(Long productId) {
        return inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId))
                .getStock();
    }

    private Map<Long, Integer> loadStocksFromDatabase(Collection<Long> productIds) {
        Map<Long, Integer> result = new HashMap<>();
        inventoryRepository.findAllById(productIds).forEach(inv ->
                result.put(inv.getId(), inv.getStock()));
        return result;
    }

    private String keyFor(Long productId) {
        return KEY_PREFIX + productId;
    }

    private static DefaultRedisScript<Long> createDeductScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/deduct_stock.lua")));
        script.setResultType(Long.class);
        return script;
    }
}
