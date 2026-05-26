package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.ProductDto;
import net.fernandosalas.ems.dto.StockQuantityRequest;
import net.fernandosalas.ems.dto.StockUpdateRequest;
import net.fernandosalas.ems.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
@Tag(name = "商品管理", description = "管理员：商品 CRUD 与库存调整")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "创建商品", description = "需要 ADMIN 角色")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto product) {
        ProductDto created = productService.createProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "查询商品列表", description = "需要 ADMIN 角色")
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "按 ID 查询商品", description = "需要 ADMIN 角色")
    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @Operation(summary = "更新商品", description = "需要 ADMIN 角色")
    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long productId,
                                                    @RequestBody ProductDto product) {
        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }

    @Operation(summary = "设置商品库存", description = "需要 ADMIN 角色，将库存设为指定值")
    @PutMapping("{id}/stock")
    public ResponseEntity<ProductDto> updateStock(@PathVariable("id") Long productId,
                                                 @RequestBody StockUpdateRequest request) {
        return ResponseEntity.ok(productService.updateStock(productId, request.getStock()));
    }

    @Operation(summary = "增加商品库存", description = "需要 ADMIN 角色")
    @PutMapping("{id}/stock/add")
    public ResponseEntity<ProductDto> addStock(@PathVariable("id") Long productId,
                                             @RequestBody StockQuantityRequest request) {
        return ResponseEntity.ok(productService.addStock(productId, request.getQuantity()));
    }

    @Operation(summary = "扣减商品库存", description = "需要 ADMIN 角色")
    @PutMapping("{id}/stock/deduct")
    public ResponseEntity<ProductDto> deductStock(@PathVariable("id") Long productId,
                                                @RequestBody StockQuantityRequest request) {
        return ResponseEntity.ok(productService.deductStock(productId, request.getQuantity()));
    }

    @Operation(summary = "删除商品", description = "需要 ADMIN 角色")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Delete Product Successfully");
    }
}
