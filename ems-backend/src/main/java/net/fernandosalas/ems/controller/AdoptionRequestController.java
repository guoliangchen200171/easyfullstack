package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.AdoptionRequestDto;
import net.fernandosalas.ems.enums.AdoptionRequestStatus;
import net.fernandosalas.ems.service.AdoptionRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adoption-requests")
@AllArgsConstructor
@Tag(name = "领养审批", description = "管理员：领养申请列表、通过、拒绝")
public class AdoptionRequestController {

    private final AdoptionRequestService adoptionRequestService;

    @Operation(summary = "分页查询领养申请", description = "需要 ADMIN 角色，可按状态筛选")
    @GetMapping
    public ResponseEntity<?> getRequests(
            @Parameter(description = "页码，从 0 开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数，默认 10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "申请状态：PENDING、APPROVED、DENIED") @RequestParam(required = false) AdoptionRequestStatus status) {
        return new ResponseEntity<>(
                adoptionRequestService.getRequestsPage(page, size, status), HttpStatus.OK);
    }

    @Operation(summary = "通过领养申请", description = "需要 ADMIN 角色")
    @PutMapping("/{id}/approve")
    public ResponseEntity<AdoptionRequestDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionRequestService.approve(id));
    }

    @Operation(summary = "拒绝领养申请", description = "需要 ADMIN 角色")
    @PutMapping("/{id}/deny")
    public ResponseEntity<AdoptionRequestDto> deny(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionRequestService.deny(id));
    }
}
