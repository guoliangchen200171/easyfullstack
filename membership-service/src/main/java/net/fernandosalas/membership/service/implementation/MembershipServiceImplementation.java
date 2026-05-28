package net.fernandosalas.membership.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.membership.entity.Membership;
import net.fernandosalas.membership.repository.MembershipRepository;
import net.fernandosalas.membership.service.MembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.fernandosalas.membership.dto.MembershipPointsResponse;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MembershipServiceImplementation implements MembershipService {

    private final MembershipRepository membershipRepository;

    // 等级阈值集中在此方法，修改这里的数字即可调整等级划分
    private String computeLevel(long points) {
        if (points >= 10000) return "GOLD";
        if (points >= 3000)  return "SILVER";
        return "BRONZE";
    }

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
        membership.setMemberLevel(computeLevel(0L));
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
        long newPoints = membership.getPoints() + pointsToAdd;
        membership.setPoints(newPoints);
        membership.setMemberLevel(computeLevel(newPoints));
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
    @Transactional(readOnly = true)
    public MembershipPointsResponse getMembershipByUserId(Long userId) {
        if (userId == null) {
            return new MembershipPointsResponse(null, 0L, "BRONZE");
        }
        return membershipRepository.findByUserId(userId)
                .map(m -> new MembershipPointsResponse(m.getUserId(), m.getPoints(), m.getMemberLevel()))
                .orElse(new MembershipPointsResponse(userId, 0L, "BRONZE"));
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        if (userId != null) {
            membershipRepository.deleteByUserId(userId);
        }
    }
}
