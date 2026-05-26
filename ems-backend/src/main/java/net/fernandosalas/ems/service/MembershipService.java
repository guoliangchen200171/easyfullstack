package net.fernandosalas.ems.service;

import net.fernandosalas.ems.entity.User;

import java.math.BigDecimal;

public interface MembershipService {

    void createForUser(User user);

    void addPointsForPurchase(User user, BigDecimal totalCost);

    long getPointsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
