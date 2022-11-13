package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VehicleHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Vehicle history JPA Repository interface.
 */
public interface VehicleHistoryRepository extends JpaRepository<VehicleHistory, Long> {

  Optional<VehicleHistory> findByVehId(@Param("vehId") String vehId);
}
