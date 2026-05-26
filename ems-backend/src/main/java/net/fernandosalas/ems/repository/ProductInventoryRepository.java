package net.fernandosalas.ems.repository;

import jakarta.persistence.LockModeType;
import net.fernandosalas.ems.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM ProductInventory i WHERE i.id = :id")
    Optional<ProductInventory> findByIdForUpdate(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE ProductInventory i SET i.stock = i.stock - :quantity " +
            "WHERE i.id = :productId AND i.stock >= :quantity")
    int deductStockIfAvailable(@Param("productId") Long productId,
                               @Param("quantity") int quantity);
}
