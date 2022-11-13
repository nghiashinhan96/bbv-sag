package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.CouponUseLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUseLogRepository extends JpaRepository<CouponUseLog, Long> {
}
