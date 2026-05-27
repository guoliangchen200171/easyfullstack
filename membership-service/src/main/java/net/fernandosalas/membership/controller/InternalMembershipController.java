package net.fernandosalas.membership.controller;

import lombok.AllArgsConstructor;
import net.fernandosalas.membership.dto.CreateMembershipRequest;
import net.fernandosalas.membership.dto.MembershipPointsResponse;
import net.fernandosalas.membership.dto.PurchasePointsRequest;
import net.fernandosalas.membership.service.MembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/memberships")
@AllArgsConstructor
public class InternalMembershipController {

    private final MembershipService membershipService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateMembershipRequest request) {
        membershipService.createForUserId(request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteByUser(@PathVariable Long userId) {
        membershipService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/points")
    public ResponseEntity<MembershipPointsResponse> getPoints(@PathVariable Long userId) {
        long points = membershipService.getPointsByUserId(userId);
        return ResponseEntity.ok(new MembershipPointsResponse(userId, points));
    }

    @PostMapping("/users/{userId}/points/purchase")
    public ResponseEntity<MembershipPointsResponse> addPurchasePoints(
            @PathVariable Long userId,
            @RequestBody PurchasePointsRequest request) {
        membershipService.addPointsForPurchase(userId, request.getTotalCost());
        long points = membershipService.getPointsByUserId(userId);
        return ResponseEntity.ok(new MembershipPointsResponse(userId, points));
    }
}
