package net.fernandosalas.ems.repository;

import net.fernandosalas.ems.entity.RestockRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestockRecordRepository extends JpaRepository<RestockRecord, Long> {

    Page<RestockRecord> findAllByOrderByRestockedAtDesc(Pageable pageable);
}
