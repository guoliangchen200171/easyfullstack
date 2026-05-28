package net.fernandosalas.ems.client;

import net.fernandosalas.ems.client.dto.CreateMembershipRequest;
import net.fernandosalas.ems.client.dto.MembershipLevelDto;
import net.fernandosalas.ems.client.dto.MembershipPointsResponse;
import net.fernandosalas.ems.client.dto.PurchasePointsRequest;

import java.util.List;
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

    @GetMapping("/api/internal/membership-levels")
    List<MembershipLevelDto> listMembershipLevels();

    @GetMapping("/api/internal/membership-levels/{id}")
    MembershipLevelDto getMembershipLevel(@PathVariable("id") Long id);

    @PostMapping("/api/internal/membership-levels")
    MembershipLevelDto createMembershipLevel(@RequestBody MembershipLevelDto dto);

    @PutMapping("/api/internal/membership-levels/{id}")
    MembershipLevelDto updateMembershipLevel(
            @PathVariable("id") Long id,
            @RequestBody MembershipLevelDto dto);

    @DeleteMapping("/api/internal/membership-levels/{id}")
    void deleteMembershipLevel(@PathVariable("id") Long id);
}
