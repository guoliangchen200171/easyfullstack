package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    @EntityGraph(attributePaths = {"student", "product"})
    Page<ProductOrder> findAllByOrderByOrderedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"student", "product"})
    Page<ProductOrder> findByStudentId(Long studentId, Pageable pageable);
}
