package net.fernandosalas.ems.service.implementation;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.StudentDto;
import net.fernandosalas.ems.entity.Department;
import net.fernandosalas.ems.entity.Pet;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.exception.EmailAlreadyExistsException;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.exception.StudentHasNoPetException;
import net.fernandosalas.ems.mapper.StudentMapper;
import net.fernandosalas.ems.repository.DepartmentRepository;
import net.fernandosalas.ems.repository.StudentRepository;
import net.fernandosalas.ems.service.PetService;
import net.fernandosalas.ems.service.StudentService;
import net.fernandosalas.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Student savedStudent =  studentRepository.save(student);
        userService.createStudentUser(savedStudent.getEmail(), savedStudent.getId());
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
        studentRepository.deleteById(studentId);
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
}
