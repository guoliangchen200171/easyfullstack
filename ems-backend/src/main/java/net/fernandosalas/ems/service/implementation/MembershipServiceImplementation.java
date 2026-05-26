package net.fernandosalas.ems.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.ems.entity.Membership;
import net.fernandosalas.ems.entity.User;
import net.fernandosalas.ems.repository.MembershipRepository;
import net.fernandosalas.ems.service.MembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MembershipServiceImplementation implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Override
    @Transactional
    public void createForUser(User user) {
        if (user == null || user.getId() == null) {
            return;
        }
        if (membershipRepository.existsByUserId(user.getId())) {
            return;
        }
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setPoints(0L);
        membershipRepository.save(membership);
    }

    @Override
    @Transactional
    public void addPointsForPurchase(User user, BigDecimal totalCost) {
        if (user == null || user.getId() == null || totalCost == null || totalCost.signum() <= 0) {
            return;
        }
        long pointsToAdd = totalCost.multiply(BigDecimal.TEN).longValue();
        if (pointsToAdd <= 0) {
            return;
        }
        Membership membership = membershipRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Membership not found for user id: " + user.getId()));
        membership.setPoints(membership.getPoints() + pointsToAdd);
        membershipRepository.save(membership);
    }

    @Override
    @Transactional(readOnly = true)
    public long getPointsByUserId(Long userId) {
        if (userId == null) {
            return 0L;
        }
        return membershipRepository.findByUserId(userId)
                .map(Membership::getPoints)
                .orElse(0L);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        if (userId != null) {
            membershipRepository.deleteByUserId(userId);
        }
    }
}
