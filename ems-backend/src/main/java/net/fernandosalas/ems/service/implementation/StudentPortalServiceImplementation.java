package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
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
import net.fernandosalas.ems.service.AdoptionRequestService;
import net.fernandosalas.ems.service.StudentPortalService;
import net.fernandosalas.ems.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentPortalServiceImplementation implements StudentPortalService {

    private final StudentRepository studentRepository;
    private final AdoptionRequestService adoptionRequestService;
    private final StudentService studentService;
    private final AdoptionHistoryService adoptionHistoryService;

    @Override
    public StudentProfileDto getCurrentStudentProfile() {
        Student student = getCurrentStudentEntity();
        return toProfileDto(student);
    }

    @Override
    public AdoptionRequestDto applyForAdoptionForCurrentStudent(Long petId) {
        return adoptionRequestService.applyForAdoption(requireStudentId(), petId);
    }

    @Override
    public AdoptionRequestDto getCurrentStudentPendingRequest() {
        return adoptionRequestService.getMyPendingRequest(requireStudentId());
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
        if (principal.getRole() != Role.STUDENT) {
            throw new ResourceNotFoundException("当前用户不是学生账号");
        }
        return studentRepository.findByUserId(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student was not found for current user"))
                .getId();
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
