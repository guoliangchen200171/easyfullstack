package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.AdoptionRequest;
import net.fernandosalas.ems.enums.AdoptionRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {

    boolean existsByStudentIdAndStatus(Long studentId, AdoptionRequestStatus status);

    @EntityGraph(attributePaths = {"student", "pet"})
    Optional<AdoptionRequest> findByStudentIdAndStatus(Long studentId, AdoptionRequestStatus status);

    @EntityGraph(attributePaths = {"student", "pet"})
    Page<AdoptionRequest> findByStatusOrderByRequestedAtDesc(
            AdoptionRequestStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"student", "pet"})
    Page<AdoptionRequest> findAllByOrderByRequestedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"student", "pet"})
    Optional<AdoptionRequest> findWithDetailsById(Long id);
}
