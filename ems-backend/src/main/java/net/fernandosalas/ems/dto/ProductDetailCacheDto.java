package net.fernandosalas.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.fernandosalas.ems.entity.ProductDetail;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailCacheDto {

    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;

    public static ProductDetailCacheDto fromEntity(ProductDetail detail) {
        return new ProductDetailCacheDto(
                detail.getProductId(),
                detail.getName(),
                detail.getDescription(),
                detail.getPrice());
    }
}
