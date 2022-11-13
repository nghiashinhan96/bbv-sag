package com.sagag.services.ivds.api.impl;

import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.utils.SagStringUtils;
import com.sagag.services.domain.eshop.dto.AdvanceVehicleModel;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.api.LicensePlateSearchService;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.criteria.VehicleFilteringTerms;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.elasticsearch.domain.vehicle.licenseplate.LicensePlateDoc;
import com.sagag.services.elasticsearch.dto.FreetextVehicleResponse;
import com.sagag.services.elasticsearch.enums.FreetextSearchOption;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.converter.VehicleConverters;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleBuilder;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;
import com.sagag.services.ivds.response.VehicleSearchResponseDto;
import com.sagag.services.ivds.validator.PlateNumberValidator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IvdsVehicleServiceImpl implements IvdsVehicleService {

  @Autowired
  private PlateNumberValidator plateNumberValidator;

  @Autowired
  private VehicleSearchService vehicleSearchService;

  @Autowired
  private LicensePlateSearchService licensePlateService;

  @Autowired
  private List<AggregationVehicleBuilder> aggregationVehicleBuilders;

  @Autowired
  private EshopFavoriteService eshopFavoriteService;

  @Override
  public Optional<VehicleDto> searchVehicleByVehId(final String vehId) {
    log.debug("Searching vehicle by vehId {}", vehId);
    if (StringUtils.isBlank(vehId)) {
      return Optional.empty();
    }

    return vehicleSearchService.searchVehicleByVehId(vehId)
        .map(VehicleConverters.optionalVehicleConverter());
  }

  @Override
  public Optional<VehicleDto> searchVehicleByVehId(UserInfo userInfo, String vehId) {
    Optional<VehicleDto> result = vehicleSearchService.searchVehicleByVehId(vehId)
        .map(VehicleConverters.optionalVehicleConverter());

    result.ifPresent(vehicleDto -> eshopFavoriteService.updateFavoriteFlagVehicle(userInfo,
        Arrays.asList(vehicleDto)));
    return result;
  }

  @Override
  public Optional<FreetextVehicleResponse> searchFreetext(final FreetextSearchOption option,
      final FreetextSearchRequest request) {
    log.debug("Searching vehicle by free text");
    final String text = request.getText();
    final Pageable page = request.getPageRequest();
    final Optional<VehicleFilteringTerms> filtering = request.getFitering();
    return vehicleSearchService.searchVehiclesByFreetext(option, text, filtering, page);
  }

  @Override
  public Optional<VehicleDto> searchGtmotiveVehicle(final String umcCode,
      final List<String> modelCodes, final List<String> engineCodes,
      final List<String> transmisionCodes, final String selectedVehId) {

    // #1174: If matched then continue to use the current vehID
    if (StringUtils.isNotBlank(selectedVehId)) {
      return searchVehicleByVehId(selectedVehId);
    }

    // #1174: If not-matched (as the user could change the engine inside GTMotive),
    // then search as with VIN case for best-match
    final String umc = SagStringUtils.handleElasticBlank(umcCode);
    final Optional<VehicleDoc> gtmotiveVehicle = vehicleSearchService.searchGtmotiveVehicle(
        umc, modelCodes, engineCodes, transmisionCodes);
    return gtmotiveVehicle.map(VehicleConverters.optionalVehicleConverter());
  }

  @Override
  public VehicleSearchResponseDto searchVehiclesByCriteria(VehicleSearchCriteria criteria,
      Pageable pageable) {
    Assert.notNull(criteria, "The given vehicle search request must not be null");
    Assert.notNull(criteria.getSearchTerm(), "The given search term must not be null");

    final VehicleSearchTermCriteria searchTerm = criteria.getSearchTerm();
    if (!searchTerm.hasVehData()) {
      return VehicleSearchResponseDto
          .map(vehicleSearchService.searchVehiclesByCriteria(criteria, pageable));
    }

    final String formattedVehData = searchTerm.getFormattedVehData();
    if (plateNumberValidator.validate(formattedVehData)) {
      // Search for plate number
      // #3219: Only for Swiss number plates a search exists that provides the vehicle
      final List<String> vehicleCodes =
          licensePlateService.searchLicensePlateByText(formattedVehData).stream()
              .map(LicensePlateDoc::getTsn).collect(Collectors.toList());
      criteria.updateVehicleCodes(vehicleCodes);
    } else {
      criteria.updateFormattedVehicleData(formattedVehData);
    }
    return VehicleSearchResponseDto
        .map(vehicleSearchService.searchVehiclesByCriteria(criteria, pageable));
  }

  @Override
  public VehicleSearchResponseDto searchAdvanceVehiclesByCriteria(VehicleSearchCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "The given vehicle search request must not be null");
    Assert.notNull(criteria.getSearchTerm(), "The given search term must not be null");
    return VehicleSearchResponseDto
            .map(vehicleSearchService.searchAdvanceTypesByMakeModel(criteria, pageable));
  }

  @Override
  public Map<String, VehicleDto> searchVehicles(String... vehicleIds) {
    final List<VehicleDoc> vehicles = vehicleSearchService.searchVehiclesByVehIds(vehicleIds);
    if (CollectionUtils.isEmpty(vehicles)) {
      return Collections.emptyMap();
    }
    return vehicles.stream().map(VehicleConverters.optionalVehicleConverter())
        .collect(Collectors.toMap(VehicleDto::getId, Function.identity()));
  }

  @Override
  public Map<AggregationVehicleMode, List<Object>> searchAggregationVehicles(
    VehicleMakeModelTypeSearchRequest request) {
    return aggregationVehicleBuilders.stream().filter(builder -> builder.isValid(request))
      .findFirst().map(builder -> builder.buildAggregation(request))
      .orElseGet(Collections::emptyMap);
  }

  @Override
  public AdvanceVehicleModel searchAdvanceAggregationVehicles(VehicleMakeModelTypeSearchRequest request) {
    return aggregationVehicleBuilders.stream().filter(builder -> builder.isValid(request))
        .findFirst().map(builder -> builder.buildAdvanceAggregation(request))
        .orElse(AdvanceVehicleModel.builder().build());
  }

}
