package net.fernandosalas.membership.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.membership.dto.MembershipLevelDto;
import net.fernandosalas.membership.service.MembershipLevelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal/membership-levels")
@AllArgsConstructor
public class InternalMembershipLevelController {

    private final MembershipLevelService membershipLevelService;

    @GetMapping
    public ResponseEntity<List<MembershipLevelDto>> list() {
        return ResponseEntity.ok(membershipLevelService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipLevelDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(membershipLevelService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MembershipLevelDto> create(@RequestBody MembershipLevelDto dto) {
        MembershipLevelDto created = membershipLevelService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipLevelDto> update(
            @PathVariable Long id,
            @RequestBody MembershipLevelDto dto) {
        return ResponseEntity.ok(membershipLevelService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        membershipLevelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
