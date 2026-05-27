package net.fernandosalas.membership.service;

import java.math.BigDecimal;

public interface MembershipService {

    void createForUserId(Long userId);

    void addPointsForPurchase(Long userId, BigDecimal totalCost);

    long getPointsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
