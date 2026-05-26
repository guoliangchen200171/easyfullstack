package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    List<ProductDetail> findAllByProductIdIn(Collection<Long> productIds);
}
