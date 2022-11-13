package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.criteria.user_history.UserVehicleHistorySearchCriteria;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.VehicleHistoryDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserVehicleHistoryService {

  /**
   * Returns the vehicle histories search by criteria.
   *
   * @param criteria
   * @param pageable
   * @return the page of vehicle histories
   */
  Page<VehicleHistoryDto> searchVehicleHistories(UserVehicleHistorySearchCriteria criteria,
      Pageable pageable);

  /**
   * Get top vehicle historical data for a user.
   *
   * @param userId the user to get his selected vehicle history
   * @return a page of vehicle history.
   */
  Page<VehicleHistoryDto> getLastestVehicleHistory(long userId);

  /**
   * Get top vehicle historical data for a user by vehicle class.
   *
   * @param userId the user to get his selected vehicle history
   * @param vehicleClass vehicle class could be pc(car) or mb (motorbike)
   * @return a page of vehicle history.
   */
  Page<VehicleHistoryDto> filterLastestVehicleHistoryByVehicleClass(long userId, String vehicleClass);

  /**
   * Adds a vehicle as a historical data of a user.
   *
   * @param userId the user identification who selected the vehicle
   * @param vehicle the vehicle to save.
   * @param searchTerm
   * @param searchMode
   * @param fromSource
   */
  void addVehicleHistory(long userId, VehicleDto vehicle, String searchTerm, String searchMode,
      UserHistoryFromSource fromSource, boolean fromOffer);
}
