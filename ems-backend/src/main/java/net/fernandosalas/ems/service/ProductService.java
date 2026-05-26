package net.fernandosalas.ems.service;

import net.fernandosalas.ems.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto product);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long productId);

    ProductDto updateProduct(Long productId, ProductDto product);

    ProductDto updateStock(Long productId, int stock);

    ProductDto addStock(Long productId, int quantity);

    ProductDto deductStock(Long productId, int quantity);

    void deleteProduct(Long productId);
}
