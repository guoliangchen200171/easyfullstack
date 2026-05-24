package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.Department;
import net.fernandosalas.ems.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT s FROM Student s WHERE s.department.id = :departmentId")
    List<Student> findByDepartmentId(@Param("departmentId") Long departmentId);

    Optional<Department> findByUserId(Long userId);
}
