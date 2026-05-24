package net.fernandosalas.ems.repository;
import net.fernandosalas.ems.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.pet LEFT JOIN FETCH s.department")
    List<Student> findAllWithDetails();

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.pet LEFT JOIN FETCH s.department WHERE s.id = :id")
    Optional<Student> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.pet LEFT JOIN FETCH s.department WHERE s.department.id = :departmentId")
    List<Student> findByDepartmentIdWithDetails(@Param("departmentId") Long departmentId);

    Optional<Student> findByUserId(Long userId);
}
