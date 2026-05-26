package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.DepositAmountRequest;
import net.fernandosalas.ems.dto.StudentDto;
import net.fernandosalas.ems.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
@Tag(name = "学生管理", description = "管理员：学生 CRUD、存款、归还次数")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Operation(summary = "创建学生", description = "需要 ADMIN 角色")
    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto studentDto) {
        StudentDto savedStudent = studentService.createStudent(studentDto);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @Operation(summary = "按 ID 查询学生", description = "需要 ADMIN 角色")
    @GetMapping("{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Long studentId) {
        StudentDto studentDto = studentService.getStudentById(studentId);
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }

    @Operation(summary = "查询学生列表", description = "需要 ADMIN；不传 page 返回全量，传 page 则分页，默认每页 10 条")
    @GetMapping
    public ResponseEntity<?> getAllStudents(
            @Parameter(description = "页码，从 0 开始；不传则返回全部") @RequestParam(required = false) Integer page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size) {
        if (page == null) {
            return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
        }
        return new ResponseEntity<>(studentService.getStudentsPage(page, size), HttpStatus.OK);
    }

    @Operation(summary = "更新学生", description = "需要 ADMIN 角色")
    @PutMapping("{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable("id") Long studentId,
                                                    @RequestBody StudentDto studentDto) {
        StudentDto newStudentDto = studentService.updateStudent(studentId, studentDto);
        return new ResponseEntity<>(newStudentDto, HttpStatus.OK);
    }

    @Operation(summary = "管理员代为归还宠物", description = "需要 ADMIN 角色，指定学生归还其宠物")
    @PutMapping("{studentId}/return-pet")
    public ResponseEntity<StudentDto> returnPet(@PathVariable("studentId") Long studentId) {
        StudentDto studentDto = studentService.returnPet(studentId);
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }

    @Operation(summary = "按邮箱为学生充值", description = "需要 ADMIN 角色，增加学生存款余额")
    @PutMapping("deposit/add")
    public ResponseEntity<StudentDto> addDeposit(@RequestBody DepositAmountRequest request) {
        StudentDto studentDto = studentService.addDeposit(request.getEmail(), request.getAmount());
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }

    @Operation(summary = "重置学生归还次数", description = "需要 ADMIN 角色")
    @PutMapping("{id}/reset-return-count")
    public ResponseEntity<StudentDto> resetReturnCount(@PathVariable("id") Long studentId) {
        StudentDto studentDto = studentService.resetReturnCount(studentId);
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }

    @Operation(summary = "删除学生", description = "需要 ADMIN 角色")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") Long studentId) {
        studentService.deleteStudent(studentId);
        return new ResponseEntity<>("Student was successfully deleted", HttpStatus.OK);
    }
}
