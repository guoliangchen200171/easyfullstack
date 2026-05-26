package net.fernandosalas.ems.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.fernandosalas.ems.dto.ProductDetailCacheDto;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductDetailCacheService {

    private static final String KEY_PREFIX = "ems:product:detail:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final ProductDetailRepository detailRepository;
    private final Duration ttl;

    public ProductDetailCacheService(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            ProductDetailRepository detailRepository,
            @Value("${app.cache.product-detail.ttl-seconds:600}") long ttlSeconds) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.detailRepository = detailRepository;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    public ProductDetailCacheDto getByProductId(Long productId) {
        Map<Long, ProductDetailCacheDto> found = getByProductIds(List.of(productId));
        ProductDetailCacheDto dto = found.get(productId);
        if (dto == null) {
            throw new ResourceNotFoundException(
                    "Product detail was not found with id: " + productId);
        }
        return dto;
    }

    public Map<Long, ProductDetailCacheDto> getByProductIds(Collection<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Map.of();
        }
        List<Long> idList = productIds.stream().distinct().toList();
        try {
            return getByProductIdsFromRedis(idList);
        } catch (Exception ex) {
            log.warn("Redis unavailable, falling back to database for product details: {}", ex.getMessage());
            return loadFromDatabase(idList);
        }
    }

    public void put(ProductDetailCacheDto dto) {
        if (dto == null || dto.getProductId() == null) {
            return;
        }
        try {
            String key = keyFor(dto.getProductId());
            redisTemplate.opsForValue().set(key, toJson(dto), ttl);
        } catch (Exception ex) {
            log.warn("Failed to write product detail cache for id {}: {}", dto.getProductId(), ex.getMessage());
        }
    }

    public void evict(Long productId) {
        if (productId == null) {
            return;
        }
        try {
            redisTemplate.delete(keyFor(productId));
        } catch (Exception ex) {
            log.warn("Failed to evict product detail cache for id {}: {}", productId, ex.getMessage());
        }
    }

    public void evictAll(Collection<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }
        try {
            List<String> keys = productIds.stream()
                    .map(this::keyFor)
                    .toList();
            redisTemplate.delete(keys);
        } catch (Exception ex) {
            log.warn("Failed to evict product detail cache keys: {}", ex.getMessage());
        }
    }

    private Map<Long, ProductDetailCacheDto> getByProductIdsFromRedis(List<Long> idList)
            throws JsonProcessingException {
        List<String> keys = idList.stream().map(this::keyFor).toList();
        List<String> cachedValues = redisTemplate.opsForValue().multiGet(keys);

        Map<Long, ProductDetailCacheDto> result = new HashMap<>();
        List<Long> missedIds = new ArrayList<>();

        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            String json = cachedValues != null && i < cachedValues.size() ? cachedValues.get(i) : null;
            if (json != null && !json.isBlank()) {
                result.put(id, fromJson(json));
            } else {
                missedIds.add(id);
            }
        }

        if (!missedIds.isEmpty()) {
            Map<Long, ProductDetailCacheDto> fromDb = loadFromDatabase(missedIds);
            for (Map.Entry<Long, ProductDetailCacheDto> entry : fromDb.entrySet()) {
                put(entry.getValue());
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    private Map<Long, ProductDetailCacheDto> loadFromDatabase(Collection<Long> productIds) {
        return detailRepository.findAllByProductIdIn(productIds).stream()
                .map(ProductDetailCacheDto::fromEntity)
                .collect(Collectors.toMap(ProductDetailCacheDto::getProductId, dto -> dto));
    }

    private String keyFor(Long productId) {
        return KEY_PREFIX + productId;
    }

    private String toJson(ProductDetailCacheDto dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    private ProductDetailCacheDto fromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<>() {});
    }
}
