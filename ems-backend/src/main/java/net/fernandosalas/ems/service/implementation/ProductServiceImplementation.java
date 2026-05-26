package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.ProductDetailCacheDto;
import net.fernandosalas.ems.dto.ProductDto;
import net.fernandosalas.ems.entity.ProductDetail;
import net.fernandosalas.ems.entity.ProductInventory;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.mapper.ProductMapper;
import net.fernandosalas.ems.repository.ProductDetailRepository;
import net.fernandosalas.ems.repository.ProductInventoryRepository;
import net.fernandosalas.ems.service.ProductDetailCacheService;
import net.fernandosalas.ems.service.ProductService;
import net.fernandosalas.ems.service.ProductStockCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductServiceImplementation implements ProductService {

    private final ProductInventoryRepository inventoryRepository;
    private final ProductDetailRepository detailRepository;
    private final ProductDetailCacheService detailCacheService;
    private final ProductStockCacheService stockCacheService;

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto product) {
        validateProduct(product);
        validateStock(product.getStock());
        validatePrice(product.getPrice());

        ProductInventory inventory = new ProductInventory();
        inventory.setStock(product.getStock());

        ProductDetail detail = new ProductDetail();
        detail.setName(product.getName());
        detail.setDescription(product.getDescription());
        detail.setPrice(product.getPrice());
        detail.setInventory(inventory);
        inventory.setDetail(detail);

        ProductInventory saved = inventoryRepository.save(inventory);
        ProductDetailCacheDto cached = ProductDetailCacheDto.fromEntity(saved.getDetail());
        detailCacheService.put(cached);
        stockCacheService.setStock(saved.getId(), saved.getStock());
        return ProductMapper.toDto(saved.getId(), cached, saved.getStock());
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductInventory> inventories = inventoryRepository.findAll();
        if (inventories.isEmpty()) {
            return List.of();
        }
        List<Long> ids = inventories.stream().map(ProductInventory::getId).toList();
        Map<Long, ProductDetailCacheDto> detailsById = detailCacheService.getByProductIds(ids);
        Map<Long, Integer> stocksById = stockCacheService.getStocks(ids);
        return inventories.stream()
                .map(inv -> toDtoOrThrow(inv, detailsById.get(inv.getId()), stocksById))
                .toList();
    }

    @Override
    public ProductDto getProductById(Long productId) {
        if (!inventoryRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product was not found with id: " + productId);
        }
        ProductDetailCacheDto detail = detailCacheService.getByProductId(productId);
        int stock = stockCacheService.getStock(productId);
        return ProductMapper.toDto(productId, detail, stock);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto product) {
        validateProduct(product);
        validateStock(product.getStock());
        validatePrice(product.getPrice());

        ProductInventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId));
        ProductDetail detail = detailRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product detail was not found with id: " + productId));

        detail.setName(product.getName());
        detail.setDescription(product.getDescription());
        detail.setPrice(product.getPrice());
        inventory.setStock(product.getStock());
        detailRepository.save(detail);
        inventoryRepository.save(inventory);
        detailCacheService.evict(productId);
        stockCacheService.setStock(productId, product.getStock());
        return getProductById(productId);
    }

    @Override
    @Transactional
    public ProductDto updateStock(Long productId, int stock) {
        validateStock(stock);
        ProductInventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId));
        inventory.setStock(stock);
        inventoryRepository.save(inventory);
        stockCacheService.setStock(productId, stock);
        return getProductById(productId);
    }

    @Override
    @Transactional
    public ProductDto addStock(Long productId, int quantity) {
        validateQuantity(quantity);
        ProductInventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId));
        inventory.setStock(inventory.getStock() + quantity);
        inventoryRepository.save(inventory);
        stockCacheService.setStock(productId, inventory.getStock());
        return getProductById(productId);
    }

    @Override
    @Transactional
    public ProductDto deductStock(Long productId, int quantity) {
        validateQuantity(quantity);
        ProductInventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId));
        if (inventory.getStock() < quantity) {
            throw new InvalidSearchParameterException("库存不足");
        }
        inventory.setStock(inventory.getStock() - quantity);
        inventoryRepository.save(inventory);
        stockCacheService.setStock(productId, inventory.getStock());
        return getProductById(productId);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        ProductInventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId));
        inventoryRepository.delete(inventory);
        detailCacheService.evict(productId);
        stockCacheService.deleteStock(productId);
    }

    private ProductDto toDtoOrThrow(
            ProductInventory inventory,
            ProductDetailCacheDto detail,
            Map<Long, Integer> stocksById) {
        if (detail == null) {
            throw new ResourceNotFoundException(
                    "Product detail was not found with id: " + inventory.getId());
        }
        Integer stock = stocksById.get(inventory.getId());
        if (stock == null) {
            stock = inventory.getStock();
        }
        return ProductMapper.toDto(inventory.getId(), detail, stock);
    }

    private void validateProduct(ProductDto product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new InvalidSearchParameterException("商品名称不能为空");
        }
    }

    private void validateStock(int stock) {
        if (stock < 0) {
            throw new InvalidSearchParameterException("库存不能为负数");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSearchParameterException("价格必须大于 0");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidSearchParameterException("数量必须大于 0");
        }
    }
}
