package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.UserVehicleHistory;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.domain.eshop.dto.VehicleHistoryDto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * User Vehicle history JPA repository.
 */
public interface UserVehicleHistoryRepository extends JpaRepository<UserVehicleHistory, Long> {

  Optional<UserVehicleHistory> findOneById(long id);

  /**
   * Returns pageable list of a users selected vehicle history.
   *
   * @param userId the user to select his selected vehicle.
   * @return a list of selected historical vehicle
   */
  @Deprecated
  @Query(
      value = "SELECT "
          + "new com.sagag.services.domain.eshop.dto.VehicleHistoryDto(veh.vehId, veh.vehName, veh.vehClass, userVeh.selectDate) "
          + "FROM UserVehicleHistory userVeh INNER JOIN userVeh.vehicleHistory veh "
          + "WHERE userVeh.userId = :userId ORDER BY userVeh.selectDate DESC")
  List<VehicleHistoryDto> searchVehicleHistories(@Param("userId") long userId, Pageable pageable);

  @Deprecated
  Optional<UserVehicleHistory> findByUserAndVehHistoryId(@Param("userid") long userid,
      @Param("vehicleHistoryId") long vehHistoryId);

  @Query(value = "SELECT g FROM UserVehicleHistory g "
      + "WHERE g.userId = :userid AND g.vehicleHistory.id = :vehicleHistoryId "
      + "AND (:searchTerm is null or g.searchTerm = :searchTerm) AND g.fromSource = :fromSource")
  List<UserVehicleHistory> findExistingUserVehicleHistory(
      @Param("userid") long userid, @Param("vehicleHistoryId") long vehHistoryId,
      @Param("searchTerm") String searchTerm,
      @Param("fromSource") UserHistoryFromSource fromSource);

  /**
   * Removes user vehicle history by userId.
   *
   * @param userIds list of identified id
   */
  @Modifying
  @Query(value = "DELETE FROM USER_VEHICLE_HISTORY WHERE USER_ID IN :userIds", nativeQuery = true)
  void removeVehicleHistoryByUserIds(@Param("userIds") List<Long> userIds);
}
