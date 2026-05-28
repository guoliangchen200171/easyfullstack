package net.fernandosalas.membership.repository;

import net.fernandosalas.membership.entity.MembershipLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipLevelRepository extends JpaRepository<MembershipLevel, Long> {

    List<MembershipLevel> findAllByOrderByMinPointsAsc();

    List<MembershipLevel> findAllByOrderByMinPointsDesc();

    Optional<MembershipLevel> findByLevelCode(String levelCode);

    boolean existsByLevelCode(String levelCode);

    boolean existsByLevelCodeAndIdNot(String levelCode, Long id);

    long countByMinPoints(long minPoints);
}
