package net.fernandosalas.ems.mapper;

import net.fernandosalas.ems.dto.ProductDto;
import net.fernandosalas.ems.entity.ProductDetail;
import net.fernandosalas.ems.entity.ProductInventory;
import net.fernandosalas.ems.dto.ProductDetailCacheDto;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductDto toDto(ProductInventory inventory, ProductDetail detail) {
        return new ProductDto(
                inventory.getId(),
                detail.getName(),
                detail.getDescription(),
                detail.getPrice(),
                inventory.getStock());
    }

    public static ProductDto toDto(ProductInventory inventory, ProductDetailCacheDto detail) {
        return toDto(inventory.getId(), detail, inventory.getStock());
    }

    public static ProductDto toDto(Long productId, ProductDetailCacheDto detail, int stock) {
        return new ProductDto(
                productId,
                detail.getName(),
                detail.getDescription(),
                detail.getPrice(),
                stock);
    }
}
