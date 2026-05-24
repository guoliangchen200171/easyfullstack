package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
