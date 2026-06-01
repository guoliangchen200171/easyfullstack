package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.dto.RestockRecordDto;
import net.fernandosalas.ems.service.RestockRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restock-records")
@AllArgsConstructor
@Tag(name = "补货记录", description = "管理员：查询补货历史记录")
public class RestockRecordController {

    private final RestockRecordService restockRecordService;

    @Operation(summary = "分页查询补货记录", description = "需要 ADMIN 角色；按补货时间倒序排列")
    @GetMapping
    public ResponseEntity<PageResponse<RestockRecordDto>> getRestockRecords(
            @Parameter(description = "页码，从 0 开始，默认 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                restockRecordService.getRestockRecordsPage(page, size),
                HttpStatus.OK);
    }
}
