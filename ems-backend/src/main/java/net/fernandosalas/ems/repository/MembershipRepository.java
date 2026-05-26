package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
