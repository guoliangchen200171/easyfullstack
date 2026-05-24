package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.student WHERE p.id = :id")
    Optional<Pet> findByIdWithStudent(@Param("id") Long id);

    @Query("SELECT p FROM Pet p WHERE p.student.id = :studentId")
    Optional<Pet> findByStudentId(@Param("studentId") Long studentId);

    List<Pet> findByNameContainingIgnoreCase(String name);
}
