package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.entity.Product;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.repository.ProductRepository;
import net.fernandosalas.ems.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        validateProduct(product);
        validateStock(product.getStock());
        validatePrice(product.getPrice());
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product was not found with id: " + productId));
    }

    @Override
    @Transactional
    public Product updateProduct(Long productId, Product product) {
        Product existing = getProductById(productId);
        validateProduct(product);
        validateStock(product.getStock());
        validatePrice(product.getPrice());
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        return productRepository.save(existing);
    }

    @Override
    @Transactional
    public Product updateStock(Long productId, int stock) {
        validateStock(stock);
        Product existing = getProductById(productId);
        existing.setStock(stock);
        return productRepository.save(existing);
    }

    @Override
    @Transactional
    public Product addStock(Long productId, int quantity) {
        validateQuantity(quantity);
        Product existing = getProductById(productId);
        existing.setStock(existing.getStock() + quantity);
        return productRepository.save(existing);
    }

    @Override
    @Transactional
    public Product deductStock(Long productId, int quantity) {
        validateQuantity(quantity);
        Product existing = getProductById(productId);
        if (existing.getStock() < quantity) {
            throw new InvalidSearchParameterException("库存不足");
        }
        existing.setStock(existing.getStock() - quantity);
        return productRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product existing = getProductById(productId);
        productRepository.delete(existing);
    }

    private void validateProduct(Product product) {
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
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSearchParameterException("价格不能为负数");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidSearchParameterException("数量必须大于 0");
        }
    }
}
