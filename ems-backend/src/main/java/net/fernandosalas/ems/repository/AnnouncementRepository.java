package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    long countByActiveTrue();

    List<Announcement> findAllByOrderByUpdatedAtDesc();

    List<Announcement> findTop3ByActiveTrueOrderByUpdatedAtDesc();
}
