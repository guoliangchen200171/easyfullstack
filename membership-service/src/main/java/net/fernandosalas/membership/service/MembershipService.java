package net.fernandosalas.membership.service;

import net.fernandosalas.membership.dto.MembershipPointsResponse;

import java.math.BigDecimal;

public interface MembershipService {

    void createForUserId(Long userId);

    void addPointsForPurchase(Long userId, BigDecimal totalCost);

    long getPointsByUserId(Long userId);

    MembershipPointsResponse getMembershipByUserId(Long userId);

    void deleteByUserId(Long userId);
}
