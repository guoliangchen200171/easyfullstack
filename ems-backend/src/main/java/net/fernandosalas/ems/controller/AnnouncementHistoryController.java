package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AnnouncementHistoryDto;
import net.fernandosalas.ems.dto.PageResponse;
import net.fernandosalas.ems.service.AnnouncementHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/announcement-history")
@AllArgsConstructor
@Tag(name = "公告历史", description = "管理员：查询公告操作历史记录")
public class AnnouncementHistoryController {

    private final AnnouncementHistoryService announcementHistoryService;

    @Operation(summary = "分页查询公告历史", description = "需要 ADMIN 角色；按操作时间倒序排列")
    @GetMapping
    public ResponseEntity<PageResponse<AnnouncementHistoryDto>> getAnnouncementHistory(
            @Parameter(description = "页码，从 0 开始，默认 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                announcementHistoryService.getHistoryPage(page, size),
                HttpStatus.OK);
    }
}
