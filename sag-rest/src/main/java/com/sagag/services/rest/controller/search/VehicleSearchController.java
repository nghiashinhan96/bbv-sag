package com.sagag.services.rest.controller.search;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.dto.AdvanceVehicleModel;
import com.sagag.services.domain.eshop.dto.MakeItem;
import com.sagag.services.domain.eshop.dto.ModelItem;
import com.sagag.services.domain.eshop.dto.TypeItem;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;
import com.sagag.services.ivds.response.VehicleSearchResponseDto;
import com.sagag.services.rest.swagger.docs.ApiDesc;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Vehicle controller class.
 */
@RestController
@RequestMapping(value = "/search/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Vehicles Search APIs")
@Slf4j
public class VehicleSearchController {

  @Autowired
  private IvdsVehicleService ivdsVehicleService;

  /**
   * Returns the vehicles list by search request.
   *
   * @param criteria the object of {@link com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria}
   * @return the result of {@link com.sagag.services.elasticsearch.dto.VehicleSearchResponse}
   */
  @PostMapping
  public VehicleSearchResponseDto searchVehiclesByRequest(
      @RequestBody VehicleSearchCriteria criteria, @PageableDefault Pageable pageable)
          throws ResultNotFoundException {
    final VehicleSearchResponseDto response =
      ivdsVehicleService.searchVehiclesByCriteria(criteria, pageable);
    RestExceptionUtils.doSafelyReturnNotEmptyRecords(response.getVehicles());
    return response;
  }

  /**
   * Returns the vehicles list by search request.
   *
   * @param criteria the object of {@link com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria}
   * @return the result of {@link com.sagag.services.elasticsearch.dto.VehicleSearchResponse}
   */
  @PostMapping("/advance")
  public VehicleSearchResponseDto searchAdvanceTypes(
          @RequestBody VehicleSearchCriteria criteria, @PageableDefault Pageable pageable)
          throws ResultNotFoundException {
    final VehicleSearchResponseDto response =
            ivdsVehicleService.searchAdvanceVehiclesByCriteria(criteria, pageable);
    RestExceptionUtils.doSafelyReturnNotEmptyRecords(response.getVehicles());
    return response;
  }

  /**
   * Returns the vehicle by vehicle id.
   *
   * @param vehId the vehId should not be blank.
   * @return a vehicle
   */
  @ApiOperation(value = ApiDesc.VehicleSearch.SEARCH_VEHICLE_BY_VEHID_API_DESC,
    notes = ApiDesc.VehicleSearch.SEARCH_VEHICLE_BY_VEHID_API_NOTE)
  @GetMapping({ "/type/Id/{vehId:.+}", "/{vehId:.+}" })
  public VehicleDto searchVehicleByVehId(@PathVariable("vehId") final String vehId,
      final OAuth2Authentication authed) throws ResultNotFoundException {
    if (StringUtils.isBlank(vehId)) {
      final String errorMessage = "the vehicle Id should not be null or blank";
      log.error(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final Optional<VehicleDto> vehicle = ivdsVehicleService.searchVehicleByVehId(user, vehId);
    if (!vehicle.isPresent()) {
      throw new ResultNotFoundException(
          MessageFormat.format("The vehicle id {0} can not found.", vehId));
    }
    return vehicle.get();
  }

  @ApiOperation(value = "Search aggregation vehicle by request",
    notes = "The service will search aggregation by request")
  @PostMapping("/aggregation")
  public Map<AggregationVehicleMode, List<Object>> searchAggregationVehiclesByRequest(
      final OAuth2Authentication authed, @RequestBody VehicleMakeModelTypeSearchRequest request) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    request.setAffiliate(user.getSupportedAffiliate());
    return ivdsVehicleService.searchAggregationVehicles(request);
  }

  @ApiOperation(value = "Search advance aggregation vehicle by request",
      notes = "The service will search advance aggregation by request")
  @PostMapping("/advance-aggregation")
  public AdvanceVehicleModel searchAdvanceAggregationVehiclesByRequest(
      final OAuth2Authentication authed, @RequestBody VehicleMakeModelTypeSearchRequest request) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    request.setAffiliate(user.getSupportedAffiliate());
    return ivdsVehicleService.searchAdvanceAggregationVehicles(request);
  }

  /**
   * Returns the vehicle make list. The response is potentially a list of vehicle make.
   *
   * @return a list of {@link MakeItem}
   */
  @ApiOperation(value = ApiDesc.VehicleSearch.SEARCH_VEHICLE_MAKE_API_DESC,
    notes = ApiDesc.VehicleSearch.SEARCH_VEHICLE_MAKE_API_NOTE)
  @GetMapping(value = "/makes",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<MakeItem> getVehicleMakes() {
    final List<MakeItem> items = new ArrayList<>();
    ivdsVehicleService.searchAggregationVehicles(new VehicleMakeModelTypeSearchRequest())
      .get(AggregationVehicleMode.MAKE)
      .stream().forEach(item -> items.add((MakeItem) item));
    return items;
  }

  /**
   * Returns the vehicle model list by vehicle make. The response is potentially a list of vehicle
   * model.
   *
   * @param makeId the make should not be blank.
   * @return a list of {@link ModelItem}
   */
  @ApiOperation(value = ApiDesc.VehicleSearch.SEARCH_VEHICLE_MODEL_API_DESC,
    notes = ApiDesc.VehicleSearch.SEARCH_VEHICLE_MODEL_API_NOTE)
  @GetMapping(value = "/{make}/models",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ModelItem> getVehicleModels(@PathVariable("make") final String makeId,
      @RequestParam(name = "size", defaultValue = "0") final int size) {
    VehicleMakeModelTypeSearchRequest request = new VehicleMakeModelTypeSearchRequest();
    request.setMakeCode(makeId);
    final List<ModelItem> items = new ArrayList<>();
    ivdsVehicleService.searchAggregationVehicles(request).get(AggregationVehicleMode.MODEL).stream()
      .forEach(item -> items.add((ModelItem) item));
    return items;
  }

  /**
   * Returns the vehicle types list by vehicle make and model. The response is potentially a list of
   * vehicle type.
   *
   * @param makeId the make should not be blank.
   * @param modelId the model should not be blank.
   * @return a list of {@link TypeItem}
   */
  @ApiOperation(value = ApiDesc.VehicleSearch.SEARCH_VEHICLE_TYPE_API_DESC,
    notes = ApiDesc.VehicleSearch.SEARCH_VEHICLE_TYPE_API_NOTE)
  @GetMapping(value = "/types",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TypeItem> getVehicleTypes(@RequestParam("make") final String makeId,
      @RequestParam("model") final String modelId) {
    VehicleMakeModelTypeSearchRequest request = new VehicleMakeModelTypeSearchRequest();
    request.setModelCode(modelId);
    request.setMakeCode(makeId);
    final List<TypeItem> items = new ArrayList<>();
    ivdsVehicleService.searchAggregationVehicles(request).get(AggregationVehicleMode.TYPE).stream()
      .forEach(item -> items.add((TypeItem) item));
    return items;
  }

}
