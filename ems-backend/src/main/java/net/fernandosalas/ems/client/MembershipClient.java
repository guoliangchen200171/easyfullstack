package net.fernandosalas.ems.client;

import net.fernandosalas.ems.client.dto.CreateMembershipRequest;
import net.fernandosalas.ems.client.dto.MembershipPointsResponse;
import net.fernandosalas.ems.client.dto.PurchasePointsRequest;
import net.fernandosalas.ems.config.MembershipFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "membership-service", configuration = MembershipFeignConfig.class)
public interface MembershipClient {

    @PostMapping("/api/internal/memberships")
    void createMembership(@RequestBody CreateMembershipRequest request);

    @DeleteMapping("/api/internal/memberships/users/{userId}")
    void deleteByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/api/internal/memberships/users/{userId}/points")
    MembershipPointsResponse getPoints(@PathVariable("userId") Long userId);

    @PostMapping("/api/internal/memberships/users/{userId}/points/purchase")
    MembershipPointsResponse addPurchasePoints(
            @PathVariable("userId") Long userId,
            @RequestBody PurchasePointsRequest request);
}
