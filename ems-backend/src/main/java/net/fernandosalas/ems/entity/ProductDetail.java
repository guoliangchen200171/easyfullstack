package net.fernandosalas.ems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_detail")
public class ProductDetail {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private ProductInventory inventory;

    @Column(name = "name", nullable = false,
            columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String name;

    @Column(name = "description",
            columnDefinition = "VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;
}
