package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.UserVehicleHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVehicleHistoryRepository extends JpaRepository<UserVehicleHistory, Long> {
}
