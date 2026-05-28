package net.fernandosalas.membership.service.implementation;

import lombok.AllArgsConstructor;
import net.fernandosalas.membership.dto.MembershipPointsResponse;
import net.fernandosalas.membership.entity.Membership;
import net.fernandosalas.membership.repository.MembershipRepository;
import net.fernandosalas.membership.service.MembershipLevelService;
import net.fernandosalas.membership.service.MembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MembershipServiceImplementation implements MembershipService {

    private static final String DEFAULT_LEVEL_CODE = "BRONZE";

    private final MembershipRepository membershipRepository;
    private final MembershipLevelService membershipLevelService;

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
        membership.setMemberLevel(membershipLevelService.resolveLevelCode(0L));
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
        Membership membership = findOrCreateMembership(userId);
        long newPoints = membership.getPoints() + pointsToAdd;
        membership.setPoints(newPoints);
        membership.setMemberLevel(membershipLevelService.resolveLevelCode(newPoints));
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
    public MembershipPointsResponse getMembershipByUserId(Long userId) {
        if (userId == null) {
            return new MembershipPointsResponse(null, 0L, DEFAULT_LEVEL_CODE, "铜牌会员");
        }
        Membership membership = findOrCreateMembership(userId);
        return toResponse(membership);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        if (userId != null) {
            membershipRepository.deleteByUserId(userId);
        }
    }

    private Membership findOrCreateMembership(Long userId) {
        return membershipRepository.findByUserId(userId)
                .orElseGet(() -> {
                    createForUserId(userId);
                    return membershipRepository.findByUserId(userId)
                            .orElseThrow(() -> new IllegalStateException(
                                    "Membership not found for user id: " + userId));
                });
    }

    private MembershipPointsResponse toResponse(Membership membership) {
        String levelCode = membershipLevelService.resolveLevelCode(membership.getPoints());
        if (!levelCode.equals(membership.getMemberLevel())) {
            membership.setMemberLevel(levelCode);
            membershipRepository.save(membership);
        }
        String levelName = membershipLevelService.resolveLevelName(levelCode);
        return new MembershipPointsResponse(
                membership.getUserId(),
                membership.getPoints(),
                levelCode,
                levelName);
    }
}
