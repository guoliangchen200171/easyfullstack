package net.fernandosalas.ems.service;

import java.math.BigDecimal;

public interface MembershipRemoteService {

    void createForUserId(Long userId);

    void addPointsForPurchase(Long userId, BigDecimal totalCost);

    long getPointsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
