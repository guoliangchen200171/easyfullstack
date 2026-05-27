package net.fernandosalas.ems.service.implementation;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fernandosalas.ems.client.MembershipClient;
import net.fernandosalas.ems.client.dto.CreateMembershipRequest;
import net.fernandosalas.ems.client.dto.MembershipPointsResponse;
import net.fernandosalas.ems.client.dto.PurchasePointsRequest;
import net.fernandosalas.ems.exception.InvalidSearchParameterException;
import net.fernandosalas.ems.service.MembershipRemoteService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class MembershipRemoteServiceImplementation implements MembershipRemoteService {

    private final MembershipClient membershipClient;

    @Override
    public void createForUserId(Long userId) {
        if (userId == null) {
            return;
        }
        try {
            membershipClient.createMembership(new CreateMembershipRequest(userId));
        } catch (FeignException ex) {
            log.error("Failed to create membership for userId={}", userId, ex);
            throw new InvalidSearchParameterException("会员账户创建失败，请稍后重试");
        }
    }

    @Override
    public void addPointsForPurchase(Long userId, BigDecimal totalCost) {
        if (userId == null || totalCost == null || totalCost.signum() <= 0) {
            return;
        }
        try {
            membershipClient.addPurchasePoints(userId, new PurchasePointsRequest(totalCost));
        } catch (FeignException ex) {
            log.error("Failed to add membership points for userId={}, totalCost={}", userId, totalCost, ex);
            throw ex;
        }
    }

    @Override
    public long getPointsByUserId(Long userId) {
        if (userId == null) {
            return 0L;
        }
        try {
            MembershipPointsResponse response = membershipClient.getPoints(userId);
            return response != null ? response.getPoints() : 0L;
        } catch (FeignException ex) {
            log.warn("Failed to fetch membership points for userId={}, returning 0", userId, ex);
            return 0L;
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        if (userId == null) {
            return;
        }
        try {
            membershipClient.deleteByUserId(userId);
        } catch (FeignException ex) {
            log.warn("Failed to delete membership for userId={}", userId, ex);
        }
    }
}
