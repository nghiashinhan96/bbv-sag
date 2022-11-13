package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.BusinessLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessLogRepository extends JpaRepository<BusinessLog, Long> {
}
