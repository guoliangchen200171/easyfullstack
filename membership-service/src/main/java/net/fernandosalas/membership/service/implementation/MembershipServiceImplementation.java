package net.fernandosalas.membership.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.membership.entity.Membership;
import net.fernandosalas.membership.repository.MembershipRepository;
import net.fernandosalas.membership.service.MembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MembershipServiceImplementation implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Override
    @Transactional
    public void createForUserId(Long userId) {
        if (userId == null) {
            return;
        }
        if (membershipRepository.existsByUserId(userId)) {
            return;
        }
        Membership membership = new Membership();
        membership.setUserId(userId);
        membership.setPoints(0L);
        membershipRepository.save(membership);
    }

    @Override
    @Transactional
    public void addPointsForPurchase(Long userId, BigDecimal totalCost) {
        if (userId == null || totalCost == null || totalCost.signum() <= 0) {
            return;
        }
        long pointsToAdd = totalCost.multiply(BigDecimal.TEN).longValue();
        if (pointsToAdd <= 0) {
            return;
        }
        Membership membership = membershipRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "Membership not found for user id: " + userId));
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
