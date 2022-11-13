package com.sagag.services.ivds.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.AdvanceVehicleModel;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.dto.FreetextVehicleResponse;
import com.sagag.services.elasticsearch.enums.FreetextSearchOption;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;
import com.sagag.services.ivds.response.VehicleSearchResponseDto;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface class for IVDS vehicle services.
 */
public interface IvdsVehicleService {

  /**
   * Returns the optional of vehicle by vehicle id.
   *
   * @param vehId
   * @return the optional of {@link VehicleDto}
   */
  Optional<VehicleDto> searchVehicleByVehId(final String vehId);
		
  /**
   * Returns the optional of vehicle by vehicle id include update flag Favorite if existed.
   *
   * @param vehId
   * @return the optional of {@link VehicleDto}
   */
  Optional<VehicleDto> searchVehicleByVehId(UserInfo userInfo, final String vehId);
		
		/**
   * Returns the vehicles and its aggregation dropdown list from free text search.
   *
   * @param request the vehicle free text search request
   * @return the {@link FreetextVehicleResponse}
   */
  Optional<FreetextVehicleResponse> searchFreetext(FreetextSearchOption option, final FreetextSearchRequest request);

  /**
   * Returns the optional of vehicle from GTMotive criteria.
   *
   * @param umcCode
   * @param modelCodes
   * @param engineCodes
   * @param transmisionCodes
   * @param selectedVehId
   * @return the optional of {@link VehicleDto}
   */
  Optional<VehicleDto> searchGtmotiveVehicle(String umcCode, List<String> modelCodes,
      List<String> engineCodes, List<String> transmisionCodes, String selectedVehId);

  /**
   * Returns the vehicles by vehicle criteria.
   *
   * @param criteria the vehicle search criteria
   * @param pageable the pagination
   * @return the page of {@link com.sagag.services.elasticsearch.dto.VehicleSearchResponse}
   */
  VehicleSearchResponseDto searchVehiclesByCriteria(VehicleSearchCriteria criteria, Pageable pageable);

  /**
   * Returns the advance vehicles by vehicle criteria.
   *
   * @param criteria the vehicle search criteria
   * @param pageable the pagination
   * @return the page of {@link com.sagag.services.elasticsearch.dto.VehicleSearchResponse}
   */
  VehicleSearchResponseDto searchAdvanceVehiclesByCriteria(VehicleSearchCriteria criteria, Pageable pageable);

  /**
   * Returns the found map of vehicles by vehicle id list.
   *
   * @param vehicleIds the list of vehicle id
   * @return the map of <code>VehicleDto</code>
   */
  Map<String, VehicleDto> searchVehicles(String... vehicleIds);

  /**
   * Returns the aggregation of vehicles by request
   *
   * @param request the vehicle request
   * @return the map of aggregation object
   */
  Map<AggregationVehicleMode, List<Object>> searchAggregationVehicles(
      VehicleMakeModelTypeSearchRequest request);

  /**
   * Returns the advance aggregation of vehicles by request.
   *
   * @param request the vehicle request
   * @return the advance aggregation object
   */
  AdvanceVehicleModel searchAdvanceAggregationVehicles(VehicleMakeModelTypeSearchRequest request);
}
