package net.fernandosalas.ems.controller;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.DepartmentDto;
import net.fernandosalas.ems.dto.StudentDto;
import net.fernandosalas.ems.entity.Student;
import net.fernandosalas.ems.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
       DepartmentDto newDepartment =  departmentService.createDepartment(departmentDto);
       return new ResponseEntity<>(newDepartment, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable("id") Long departmentId) {
       DepartmentDto departmentDto = departmentService.getDepartmentById(departmentId);
       return new ResponseEntity<>(departmentDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllDepartments(
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "10") int size) {
        if (page == null) {
            return new ResponseEntity<>(departmentService.getAllDepartments(), HttpStatus.OK);
        }
        return new ResponseEntity<>(departmentService.getDepartmentsPage(page, size), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable("id") Long departmentId,
                                                          @RequestBody DepartmentDto departmentDto) {
       DepartmentDto updatedDepartment = departmentService.updateDepartment(departmentId, departmentDto);
       return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable("id") Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return new ResponseEntity<>("Delete Department Successfully", HttpStatus.OK);
    }
    @GetMapping("{id}/students")
    public ResponseEntity<List<StudentDto>> getStudentsByDepartment(@PathVariable("id") Long departmentId) {
        List<StudentDto> students = departmentService.getStudentsByDepartmentId(departmentId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
}
