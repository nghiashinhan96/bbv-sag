package com.sagag.eshop.repo.api.userhistory;

import com.sagag.eshop.repo.entity.user_history.VUserVehicleHistory;
import com.sagag.services.domain.eshop.dto.VehicleHistoryDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VUserVehicleHistoryRepository
  extends JpaRepository<VUserVehicleHistory, Long>, JpaSpecificationExecutor<VUserVehicleHistory> {

  @Query(value = "SELECT new com.sagag.services.domain.eshop.dto."
      + "VehicleHistoryDto(vuvh.vehicleId, vuvh.vehicleName, vuvh.vehicleClass, vuvh.selectDate, vuvh.searchTerm, "
      + "vuvh.searchMode, vuvh.fullName, vuvh.fromSource) "
      + "FROM VUserVehicleHistory vuvh WHERE vuvh.userId = :userId "
      + "ORDER BY vuvh.selectDate DESC")
  Page<VehicleHistoryDto> findTopVehicleHistories(@Param("userId") long userId, Pageable pageable);

}
