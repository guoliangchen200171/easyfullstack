package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.DepartmentRegisterRequest;
import net.fernandosalas.ems.dto.StudentRegisterRequest;
import net.fernandosalas.ems.entity.Department;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.entity.User;
import net.fernandosalas.ems.enums.Role;
import net.fernandosalas.ems.exception.EmailAlreadyExistsException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.exception.UsernameAlreadyExistsException;
import net.fernandosalas.ems.repository.DepartmentRepository;
import net.fernandosalas.ems.repository.StudentRepository;
import net.fernandosalas.ems.service.AuthRegistrationService;
import net.fernandosalas.ems.service.MembershipRemoteService;
import net.fernandosalas.ems.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthRegistrationServiceImplementation implements AuthRegistrationService {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final MembershipRemoteService membershipRemoteService;

    @Override
    @Transactional
    public void registerDepartment(DepartmentRegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("该用户名已被注册");
        }

        User user = userService.createUser(
                request.getUsername(),
                request.getPassword(),
                Role.DEPARTMENT);

        Department department = new Department();
        department.setDepartmentName(request.getDepartmentName());
        department.setDepartmentDescription(request.getDepartmentDescription());
        department.setUser(user);
        departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void registerStudent(StudentRegisterRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("该邮箱已被注册，请使用其他邮箱");
        }
        if (userService.existsByUsername(request.getEmail())) {
            throw new UsernameAlreadyExistsException("该邮箱已被注册");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department was not found with id: " + request.getDepartmentId()));

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setDepartment(department);
        student.setReturnCount(0);
        Student savedStudent = studentRepository.save(student);

        User user = userService.createStudentUser(savedStudent.getEmail());
        savedStudent.setUser(user);
        studentRepository.save(savedStudent);
        membershipRemoteService.createForUserId(user.getId());
    }
}
