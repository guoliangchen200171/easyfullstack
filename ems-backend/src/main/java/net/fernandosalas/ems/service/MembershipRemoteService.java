package net.fernandosalas.ems.service;

import net.fernandosalas.ems.client.dto.MembershipPointsResponse;

import java.math.BigDecimal;

public interface MembershipRemoteService {

    void createForUserId(Long userId);

    void addPointsForPurchase(Long userId, BigDecimal totalCost);

    long getPointsByUserId(Long userId);

    MembershipPointsResponse getMembershipByUserId(Long userId);

    void deleteByUserId(Long userId);
}
