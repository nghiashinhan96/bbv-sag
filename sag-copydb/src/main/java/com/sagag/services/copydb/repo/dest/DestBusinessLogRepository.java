package com.sagag.services.copydb.repo.dest;

import com.sagag.services.copydb.domain.dest.DestBusinessLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DestBusinessLogRepository extends JpaRepository<DestBusinessLog, Long> {
}
