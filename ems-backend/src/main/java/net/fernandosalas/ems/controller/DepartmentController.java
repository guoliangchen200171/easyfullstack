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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
@Tag(name = "部门管理", description = "管理员：部门 CRUD 与部门学生列表")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @Operation(summary = "创建部门", description = "需要 ADMIN 角色")
    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
       DepartmentDto newDepartment =  departmentService.createDepartment(departmentDto);
       return new ResponseEntity<>(newDepartment, HttpStatus.CREATED);
    }

    @Operation(summary = "按 ID 查询部门", description = "需要 ADMIN 角色")
    @GetMapping("{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable("id") Long departmentId) {
       DepartmentDto departmentDto = departmentService.getDepartmentById(departmentId);
       return new ResponseEntity<>(departmentDto, HttpStatus.OK);
    }

    @Operation(summary = "查询部门列表", description = "公开接口；不传 page 返回全量，传 page 则分页，默认每页 10 条")
    @GetMapping
    @SecurityRequirements
    public ResponseEntity<?> getAllDepartments(
            @Parameter(description = "页码，从 0 开始；不传则返回全部") @RequestParam(required = false) Integer page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size) {
        if (page == null) {
            return new ResponseEntity<>(departmentService.getAllDepartments(), HttpStatus.OK);
        }
        return new ResponseEntity<>(departmentService.getDepartmentsPage(page, size), HttpStatus.OK);
    }

    @Operation(summary = "更新部门", description = "需要 ADMIN 角色")
    @PutMapping("{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable("id") Long departmentId,
                                                          @RequestBody DepartmentDto departmentDto) {
       DepartmentDto updatedDepartment = departmentService.updateDepartment(departmentId, departmentDto);
       return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    @Operation(summary = "删除部门", description = "需要 ADMIN 角色")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable("id") Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return new ResponseEntity<>("Delete Department Successfully", HttpStatus.OK);
    }
    @Operation(summary = "查询部门学生列表", description = "需要 ADMIN 角色")
    @GetMapping("{id}/students")
    public ResponseEntity<List<StudentDto>> getStudentsByDepartment(@PathVariable("id") Long departmentId) {
        List<StudentDto> students = departmentService.getStudentsByDepartmentId(departmentId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
}
