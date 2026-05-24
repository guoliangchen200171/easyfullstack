package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.DepartmentDto;
import net.fernandosalas.ems.dto.DepartmentStudentPortalDto;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.enums.Role;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.mapper.DepartmentMapper;
import net.fernandosalas.ems.repository.DepartmentRepository;
import net.fernandosalas.ems.repository.StudentRepository;
import net.fernandosalas.ems.security.SecurityUtils;
import net.fernandosalas.ems.security.UserPrincipal;
import net.fernandosalas.ems.service.DepartmentPortalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentPortalServiceImplementation implements DepartmentPortalService {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

    @Override
    public DepartmentDto getCurrentDepartment() {
        Long departmentId = requireDepartmentId();
        return departmentRepository.findById(departmentId)
                .map(DepartmentMapper::mapToDepartmentDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department was not found with id: " + departmentId));
    }

    @Override
    public List<DepartmentStudentPortalDto> getCurrentDepartmentStudents() {
        Long departmentId = requireDepartmentId();
        return studentRepository.findByDepartmentIdWithDetails(departmentId).stream()
                .map(this::toPortalDto)
                .toList();
    }

    private DepartmentStudentPortalDto toPortalDto(Student student) {
        Pet pet = student.getPet();
        return new DepartmentStudentPortalDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                pet != null ? pet.getName() : null,
                student.getReturnCount(),
                pet != null ? pet.getAdoptionCount() : 0,
                pet != null ? pet.getReturnCount() : 0);
    }

    private Long requireDepartmentId() {
        UserPrincipal principal = SecurityUtils.getCurrentUser();
        if (principal.getRole() != Role.DEPARTMENT) {
            throw new ResourceNotFoundException("当前用户不是部门账号");
        }
        return departmentRepository.findByUserId(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department was not found for current user"))
                .getId();
    }
}
