package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.student WHERE p.id = :id")
    Optional<Pet> findByIdWithStudent(@Param("id") Long id);

    Optional<Pet> findByStudentId(Long studentId);
}
