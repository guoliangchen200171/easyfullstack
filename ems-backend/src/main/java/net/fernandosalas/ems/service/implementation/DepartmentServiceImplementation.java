package net.fernandosalas.ems.service.implementation;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.DepartmentDto;
import net.fernandosalas.ems.dto.StudentDto;
import net.fernandosalas.ems.entity.Department;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.entity.User;
import net.fernandosalas.ems.exception.ResourceNotFoundException;
import net.fernandosalas.ems.mapper.DepartmentMapper;
import net.fernandosalas.ems.repository.DepartmentRepository;
import net.fernandosalas.ems.service.DepartmentService;
import net.fernandosalas.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentServiceImplementation implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserService userService;
    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = DepartmentMapper.mapToDepartment(departmentDto);
        Department savedDepartment = departmentRepository.save(department);
        return DepartmentMapper.mapToDepartmentDto(savedDepartment);
    }

    @Override
    public DepartmentDto getDepartmentById(Long departmentId) {
      Department department = departmentRepository.findById(departmentId)
               .orElseThrow(()-> new ResourceNotFoundException("Department was not found with id: " + departmentId));
      return DepartmentMapper.mapToDepartmentDto(department);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream().map(DepartmentMapper::mapToDepartmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto updateDepartment(Long departmentId, DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Department was not found with id: " + departmentId));
        department.setDepartmentName(departmentDto.getDepartmentName());
        department.setDepartmentDescription(departmentDto.getDepartmentDescription());
        Department updatedDepartment = departmentRepository.save(department);
        return DepartmentMapper.mapToDepartmentDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Department was not found with id: " + departmentId));
        User linkedUser = department.getUser();
        departmentRepository.deleteById(departmentId);
        if (linkedUser != null) {
            userService.deleteById(linkedUser.getId());
        }
    }

    @Override
    public List<StudentDto> getStudentsByDepartmentId(Long departmentId) {
        // 1. 查询部门
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department was not found with id: " + departmentId));

        // 2. 获取学生列表（LAZY）
        List<Student> students = department.getStudents();

        // 3. 包装成 DTO
        List<StudentDto> studentDtos = students.stream()
                .map(student -> {
                    StudentDto dto = new StudentDto();
                    dto.setId(student.getId());
                    dto.setFirstName(student.getFirstName());
                    dto.setLastName(student.getLastName());
                    dto.setEmail(student.getEmail());
                    // 只保留 departmentId，避免套娃
                    dto.setDepartmentId(department.getId());
                    return dto;
                })
                .collect(Collectors.toList());

        // 4. 返回 DTO 列表
        return studentDtos;
    }


}
