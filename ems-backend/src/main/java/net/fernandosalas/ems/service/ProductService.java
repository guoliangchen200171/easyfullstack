package net.fernandosalas.ems.service;

import net.fernandosalas.ems.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Product updateProduct(Long productId, Product product);

    Product updateStock(Long productId, int stock);

    Product addStock(Long productId, int quantity);

    Product deductStock(Long productId, int quantity);

    void deleteProduct(Long productId);
}
