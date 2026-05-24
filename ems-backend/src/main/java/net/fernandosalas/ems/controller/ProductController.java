package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.StockQuantityRequest;
import net.fernandosalas.ems.dto.StockUpdateRequest;
import net.fernandosalas.ems.entity.Product;
import net.fernandosalas.ems.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long productId,
                                                 @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }

    @PutMapping("{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable("id") Long productId,
                                               @RequestBody StockUpdateRequest request) {
        return ResponseEntity.ok(productService.updateStock(productId, request.getStock()));
    }

    @PutMapping("{id}/stock/add")
    public ResponseEntity<Product> addStock(@PathVariable("id") Long productId,
                                            @RequestBody StockQuantityRequest request) {
        return ResponseEntity.ok(productService.addStock(productId, request.getQuantity()));
    }

    @PutMapping("{id}/stock/deduct")
    public ResponseEntity<Product> deductStock(@PathVariable("id") Long productId,
                                               @RequestBody StockQuantityRequest request) {
        return ResponseEntity.ok(productService.deductStock(productId, request.getQuantity()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Delete Product Successfully");
    }
}
