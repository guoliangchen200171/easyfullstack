package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.StudentProfileDto;
import net.fernandosalas.ems.entity.Department;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.enums.Role;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.repository.StudentRepository;
import net.fernandosalas.ems.security.SecurityUtils;
import net.fernandosalas.ems.security.UserPrincipal;
import net.fernandosalas.ems.service.AdoptionHistoryService;
import net.fernandosalas.ems.service.PetService;
import net.fernandosalas.ems.service.StudentPortalService;
import net.fernandosalas.ems.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentPortalServiceImplementation implements StudentPortalService {

    private final StudentRepository studentRepository;
    private final PetService petService;
    private final StudentService studentService;
    private final AdoptionHistoryService adoptionHistoryService;

    @Override
    public StudentProfileDto getCurrentStudentProfile() {
        Student student = getCurrentStudentEntity();
        return toProfileDto(student);
    }

    @Override
    public Pet adoptPetForCurrentStudent(Long petId) {
        Long studentId = requireStudentId();
        return petService.adoptPet(petId, studentId);
    }

    @Override
    public StudentProfileDto returnPetForCurrentStudent() {
        Long studentId = requireStudentId();
        studentService.returnPet(studentId);
        return getCurrentStudentProfile();
    }

    @Override
    public List<AdoptionHistoryDto> getCurrentStudentHistory() {
        return adoptionHistoryService.getHistoryByStudentId(requireStudentId());
    }

    private Long requireStudentId() {
        UserPrincipal principal = SecurityUtils.getCurrentUser();
        if (principal.getRole() != Role.STUDENT || principal.getStudentId() == null) {
            throw new ResourceNotFoundException("当前用户不是学生账号");
        }
        return principal.getStudentId();
    }

    private Student getCurrentStudentEntity() {
        return studentRepository.findByIdWithDetails(requireStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student was not found"));
    }

    private StudentProfileDto toProfileDto(Student student) {
        Department department = student.getDepartment();
        Pet pet = student.getPet();
        return new StudentProfileDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                department != null ? department.getId() : null,
                department != null ? department.getDepartmentName() : null,
                pet != null ? pet.getId() : null,
                pet != null ? pet.getName() : null,
                student.getReturnCount());
    }
}
