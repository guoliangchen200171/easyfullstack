package net.fernandosalas.ems.service;
import net.fernandosalas.ems.dto.StudentDto;
import net.fernandosalas.ems.dto.PageResponse;
import java.util.List;

public interface StudentService {
    StudentDto createStudent(StudentDto studentDto);
    StudentDto getStudentById(Long studentId);
    List<StudentDto> getAllStudents();
    PageResponse<StudentDto> getStudentsPage(int page, int size);
    StudentDto updateStudent(Long studentId, StudentDto studentDto);
    void deleteStudent(Long studentId);
    StudentDto returnPet(Long studentId);
    StudentDto resetReturnCount(Long studentId);
}
