package net.fernandosalas.membership.config;

import net.fernandosalas.membership.entity.MembershipLevel;
import net.fernandosalas.membership.repository.MembershipLevelRepository;
import net.fernandosalas.membership.service.MembershipLevelService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MembershipLevelDataLoader implements ApplicationRunner {

    private final MembershipLevelRepository membershipLevelRepository;
    private final MembershipLevelService membershipLevelService;

    public MembershipLevelDataLoader(
            MembershipLevelRepository membershipLevelRepository,
            MembershipLevelService membershipLevelService) {
        this.membershipLevelRepository = membershipLevelRepository;
        this.membershipLevelService = membershipLevelService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (membershipLevelRepository.count() == 0) {
            membershipLevelRepository.save(new MembershipLevel(
                    null, "BRONZE", "铜牌会员", 0L, "默认等级", 1, BigDecimal.ZERO));
            membershipLevelRepository.save(new MembershipLevel(
                    null, "SILVER", "银牌会员", 3000L, null, 2, new BigDecimal("0.0500")));
            membershipLevelRepository.save(new MembershipLevel(
                    null, "GOLD", "金牌会员", 10000L, null, 3, new BigDecimal("0.1000")));
        }
        if (membershipLevelRepository.count() > 0) {
            membershipLevelService.recalculateAllMemberLevels();
        }
    }
}
