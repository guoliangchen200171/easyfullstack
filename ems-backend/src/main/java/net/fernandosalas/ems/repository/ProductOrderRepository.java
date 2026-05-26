package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    @EntityGraph(attributePaths = {"student", "product"})
    Page<ProductOrder> findAllByOrderByOrderedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"student", "product"})
    @Query("SELECT po FROM ProductOrder po")
    Page<ProductOrder> findAllOrders(Pageable pageable);

    @EntityGraph(attributePaths = {"student", "product"})
    Page<ProductOrder> findByProductNameContainingIgnoreCase(
            String productName, Pageable pageable);

    @EntityGraph(attributePaths = {"student", "product"})
    Page<ProductOrder> findByStudentId(Long studentId, Pageable pageable);

    @EntityGraph(attributePaths = {"student", "product"})
    Page<ProductOrder> findByStudentIdAndProductNameContainingIgnoreCase(
            Long studentId, String productName, Pageable pageable);

    @EntityGraph(attributePaths = {"student", "product"})
    @Query("""
            SELECT po FROM ProductOrder po
            WHERE po.student.id = :studentId
              AND (:name IS NULL OR LOWER(po.productName) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:fromAt IS NULL OR po.orderedAt >= :fromAt)
              AND (:toAt IS NULL OR po.orderedAt <= :toAt)
              AND (:minPrice IS NULL OR po.totalPrice >= :minPrice)
              AND (:maxPrice IS NULL OR po.totalPrice <= :maxPrice)
            """)
    Page<ProductOrder> searchByStudentWithFilters(
            @Param("studentId") Long studentId,
            @Param("name") String name,
            @Param("fromAt") LocalDateTime fromAt,
            @Param("toAt") LocalDateTime toAt,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    @EntityGraph(attributePaths = {"student", "product"})
    @Query("""
            SELECT po FROM ProductOrder po
            WHERE (:name IS NULL OR LOWER(po.productName) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:fromAt IS NULL OR po.orderedAt >= :fromAt)
              AND (:toAt IS NULL OR po.orderedAt <= :toAt)
              AND (:minPrice IS NULL OR po.totalPrice >= :minPrice)
              AND (:maxPrice IS NULL OR po.totalPrice <= :maxPrice)
            """)
    Page<ProductOrder> searchAllWithFilters(
            @Param("name") String name,
            @Param("fromAt") LocalDateTime fromAt,
            @Param("toAt") LocalDateTime toAt,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
}
