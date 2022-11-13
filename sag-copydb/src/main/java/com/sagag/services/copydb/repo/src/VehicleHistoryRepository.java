package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.VehicleHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleHistoryRepository extends JpaRepository<VehicleHistory, Long> {
}
