package com.sagag.services.ivds.filter.vehicles;

import com.sagag.services.domain.eshop.dto.AdvanceVehicleModel;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;

import java.util.List;
import java.util.Map;

public interface AggregationVehicleBuilder {

  /**
   * Builds the aggregation by request.
   *
   * @return the map of aggregation.
   */
  Map<AggregationVehicleMode, List<Object>> buildAggregation(
    VehicleMakeModelTypeSearchRequest request);

  /**
   * Builds the advance aggregation by request.
   *
   * @return the map of aggregation.
   */
  default AdvanceVehicleModel buildAdvanceAggregation(VehicleMakeModelTypeSearchRequest request) {
    return AdvanceVehicleModel.builder().build();
  }

  /**
   * Verifies the request is valid or not.
   *
   * @return true if valid, otherwise
   */
  boolean isValid(VehicleMakeModelTypeSearchRequest request);
}
