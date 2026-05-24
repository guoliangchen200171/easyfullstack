package net.fernandosalas.ems.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.DepartmentDto;
import net.fernandosalas.ems.dto.DepartmentStudentPortalDto;
import net.fernandosalas.ems.service.DepartmentPortalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/department/me")
@AllArgsConstructor
public class DepartmentPortalController {

    private final DepartmentPortalService departmentPortalService;

    @GetMapping
    public ResponseEntity<DepartmentDto> getDepartment() {
        return ResponseEntity.ok(departmentPortalService.getCurrentDepartment());
    }

    @GetMapping("/students")
    public ResponseEntity<List<DepartmentStudentPortalDto>> getStudents() {
        return ResponseEntity.ok(departmentPortalService.getCurrentDepartmentStudents());
    }
}
