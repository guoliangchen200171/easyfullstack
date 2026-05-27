package net.fernandosalas.ems.service.implementation;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.StudentDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.entity.Department;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.entity.User;
import net.fernandosalas.ems.exception.EmailAlreadyExistsException;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.exception.StudentHasNoPetException;
import net.fernandosalas.ems.mapper.StudentMapper;
import net.fernandosalas.ems.repository.DepartmentRepository;
import net.fernandosalas.ems.repository.StudentRepository;
import net.fernandosalas.ems.service.MembershipRemoteService;
import net.fernandosalas.ems.service.PetService;
import net.fernandosalas.ems.service.StudentService;
import net.fernandosalas.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImplementation implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Autowired
    private MembershipRemoteService membershipRemoteService;
    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        if (studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new EmailAlreadyExistsException("该邮箱已被注册，请使用其他邮箱");
        }

        Student student = StudentMapper.mapToStudent(studentDto);

        Department department = departmentRepository.findById(studentDto.getDepartmentId())
                .orElseThrow(()-> new ResourceNotFoundException("Department was not found with id: "
                                    + studentDto.getDepartmentId()));
        student.setDepartment(department);
        student.setReturnCount(0);
        student.setDeposit(BigDecimal.ZERO);
        Student savedStudent = studentRepository.save(student);
        User user = userService.createStudentUser(savedStudent.getEmail());
        savedStudent.setUser(user);
        studentRepository.save(savedStudent);
        membershipRemoteService.createForUserId(user.getId());
        return StudentMapper.mapToStudentDto(savedStudent);
    }

    @Override
    public StudentDto getStudentById(Long studentId) {
       Student student = studentRepository.findByIdWithDetails(studentId).orElseThrow(()->
                new ResourceNotFoundException("Student was not found with given id: " + studentId));
        return StudentMapper.mapToStudentDto(student);
    }

    @Override
    public List<StudentDto> getAllStudents() {
       List<Student> studentList = studentRepository.findAllWithDetails();
        return studentList.stream()
                .map(StudentMapper::mapToStudentDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<StudentDto> getStudentsPage(int page, int size) {
        Page<Student> studentPage = studentRepository.findAll(
                PageRequest.of(page, size, Sort.by("id").ascending()));
        List<StudentDto> content = studentPage.getContent().stream()
                .map(StudentMapper::mapToStudentDto)
                .collect(Collectors.toList());
        return PageResponse.from(studentPage, content);
    }

    @Override
    public StudentDto updateStudent(Long studentId, StudentDto studentDto) {
        Student student = studentRepository.findByIdWithDetails(studentId).orElseThrow(()->
                new ResourceNotFoundException("Student was not found with given id: " + studentId));

        if (studentRepository.existsByEmailAndIdNot(studentDto.getEmail(), studentId)) {
            throw new EmailAlreadyExistsException("该邮箱已被注册，请使用其他邮箱");
        }

        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setEmail(studentDto.getEmail());

        Department department = departmentRepository.findById(studentDto.getDepartmentId())
                .orElseThrow(()-> new ResourceNotFoundException("Department was not found with id: "
                        + studentDto.getDepartmentId()));
        student.setDepartment(department);

        Student savedStudent = studentRepository.save(student);
        return StudentMapper.mapToStudentDto(savedStudent);
    }

    @Override
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findByIdWithDetails(studentId).orElseThrow(()->
                new ResourceNotFoundException("Student was not found with given id: " + studentId));
        if (student.getPet() != null) {
            petService.returnPet(student.getPet().getId(), false);
        }
        User linkedUser = student.getUser();
        studentRepository.deleteById(studentId);
        if (linkedUser != null) {
            membershipRemoteService.deleteByUserId(linkedUser.getId());
            userService.deleteById(linkedUser.getId());
        }
    }

    @Override
    public StudentDto returnPet(Long studentId) {
        Student student = studentRepository.findByIdWithDetails(studentId).orElseThrow(() ->
                new ResourceNotFoundException("Student was not found with given id: " + studentId));

        Pet pet = student.getPet();
        if (pet == null) {
            throw new StudentHasNoPetException("该学生没有宠物可送还");
        }

        petService.returnPet(pet.getId(), true);

        return StudentMapper.mapToStudentDto(studentRepository.findByIdWithDetails(studentId).orElseThrow());
    }

    @Override
    public StudentDto resetReturnCount(Long studentId) {
        Student student = studentRepository.findByIdWithDetails(studentId).orElseThrow(() ->
                new ResourceNotFoundException("Student was not found with given id: " + studentId));
        student.setReturnCount(0);
        Student savedStudent = studentRepository.save(student);
        return StudentMapper.mapToStudentDto(
                studentRepository.findByIdWithDetails(savedStudent.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public StudentDto addDeposit(String email, BigDecimal amount) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidSearchParameterException("邮箱不能为空");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSearchParameterException("存款金额必须大于 0");
        }
        String normalizedEmail = email.trim();
        Student student = studentRepository.findByEmail(normalizedEmail).orElseThrow(() ->
                new ResourceNotFoundException("未找到该邮箱对应的学生: " + normalizedEmail));
        BigDecimal currentDeposit = student.getDeposit() != null ? student.getDeposit() : BigDecimal.ZERO;
        student.setDeposit(currentDeposit.add(amount));
        Student savedStudent = studentRepository.save(student);
        return StudentMapper.mapToStudentDto(
                studentRepository.findByIdWithDetails(savedStudent.getId()).orElseThrow());
    }
}
