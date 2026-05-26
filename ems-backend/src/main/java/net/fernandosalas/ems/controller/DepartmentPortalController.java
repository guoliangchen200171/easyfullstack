package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.DepartmentDto;
import net.fernandosalas.ems.dto.DepartmentStudentPortalDto;
import net.fernandosalas.ems.service.DepartmentPortalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/department/me")
@AllArgsConstructor
@Tag(name = "部门门户", description = "部门账号：本部门信息与部门学生列表")
public class DepartmentPortalController {

    private final DepartmentPortalService departmentPortalService;

    @Operation(summary = "获取本部门信息", description = "需要 DEPARTMENT 角色")
    @GetMapping
    public ResponseEntity<DepartmentDto> getDepartment() {
        return ResponseEntity.ok(departmentPortalService.getCurrentDepartment());
    }

    @Operation(summary = "获取本部门学生列表", description = "需要 DEPARTMENT 角色")
    @GetMapping("/students")
    public ResponseEntity<List<DepartmentStudentPortalDto>> getStudents() {
        return ResponseEntity.ok(departmentPortalService.getCurrentDepartmentStudents());
    }
}
