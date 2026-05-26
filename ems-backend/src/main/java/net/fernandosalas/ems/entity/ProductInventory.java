package net.fernandosalas.ems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_inventory")
public class ProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock", nullable = false)
    private int stock = 0;

    @OneToOne(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductDetail detail;
}
