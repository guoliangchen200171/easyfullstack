package net.fernandosalas.ems.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.fernandosalas.ems.dto.MembershipLevelDto;
import net.fernandosalas.ems.service.MembershipLevelRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membership-levels")
@AllArgsConstructor
@Tag(name = "会员等级", description = "管理员：会员等级 CRUD")
public class MembershipLevelController {

    private final MembershipLevelRemoteService membershipLevelRemoteService;

    @Operation(summary = "查询全部会员等级", description = "需要 ADMIN 角色")
    @GetMapping
    public ResponseEntity<List<MembershipLevelDto>> list() {
        return ResponseEntity.ok(membershipLevelRemoteService.findAll());
    }

    @Operation(summary = "按 ID 查询会员等级", description = "需要 ADMIN 角色")
    @GetMapping("/{id}")
    public ResponseEntity<MembershipLevelDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(membershipLevelRemoteService.findById(id));
    }

    @Operation(summary = "创建会员等级", description = "需要 ADMIN 角色")
    @PostMapping
    public ResponseEntity<MembershipLevelDto> create(@RequestBody MembershipLevelDto dto) {
        MembershipLevelDto created = membershipLevelRemoteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "更新会员等级", description = "需要 ADMIN 角色")
    @PutMapping("/{id}")
    public ResponseEntity<MembershipLevelDto> update(
            @PathVariable Long id,
            @RequestBody MembershipLevelDto dto) {
        return ResponseEntity.ok(membershipLevelRemoteService.update(id, dto));
    }

    @Operation(summary = "删除会员等级", description = "需要 ADMIN 角色")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        membershipLevelRemoteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
