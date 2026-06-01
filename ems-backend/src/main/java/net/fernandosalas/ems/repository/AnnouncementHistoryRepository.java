package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.AnnouncementHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementHistoryRepository extends JpaRepository<AnnouncementHistory, Long> {

    Page<AnnouncementHistory> findAllByOrderByOperatedAtDesc(Pageable pageable);
}
