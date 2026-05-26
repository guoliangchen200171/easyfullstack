package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionHistoryDto;
import net.fernandosalas.ems.service.AdoptionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adoption-history")
@AllArgsConstructor
@Tag(name = "领养历史", description = "管理员：领养历史记录查询")
public class AdoptionHistoryController {

    @Autowired
    private AdoptionHistoryService adoptionHistoryService;

    @Operation(summary = "查询领养历史", description = "需要 ADMIN；不传 page 返回全量，传 page 则分页")
    @GetMapping
    public ResponseEntity<?> getAllHistory(
            @Parameter(description = "页码，从 0 开始；不传则返回全部") @RequestParam(required = false) Integer page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size) {
        if (page == null) {
            return new ResponseEntity<>(adoptionHistoryService.getAllHistory(), HttpStatus.OK);
        }
        return new ResponseEntity<>(adoptionHistoryService.getHistoryPage(page, size), HttpStatus.OK);
    }

    @Operation(summary = "按学生 ID 查询领养历史", description = "需要 ADMIN 角色")
    @GetMapping("student/{studentId}")
    public ResponseEntity<List<AdoptionHistoryDto>> getHistoryByStudentId(
            @PathVariable("studentId") Long studentId) {
        return new ResponseEntity<>(adoptionHistoryService.getHistoryByStudentId(studentId), HttpStatus.OK);
    }
}
