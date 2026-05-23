package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.AdoptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdoptionHistoryRepository extends JpaRepository<AdoptionHistory, Long> {

    @Query("SELECT h FROM AdoptionHistory h JOIN FETCH h.student JOIN FETCH h.pet ORDER BY h.adoptedAt DESC")
    List<AdoptionHistory> findAllWithDetailsOrderByAdoptedAtDesc();

    @Query("SELECT h FROM AdoptionHistory h JOIN FETCH h.student JOIN FETCH h.pet WHERE h.student.id = :studentId ORDER BY h.adoptedAt DESC")
    List<AdoptionHistory> findByStudentIdWithDetailsOrderByAdoptedAtDesc(Long studentId);

    Optional<AdoptionHistory> findFirstByStudentIdAndReturnedAtIsNullOrderByAdoptedAtDesc(Long studentId);
}
