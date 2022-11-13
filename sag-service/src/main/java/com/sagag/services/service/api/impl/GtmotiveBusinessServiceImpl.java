package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.request.GtmotiveVehicleCriteria;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveMultiPartSearchRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByGtInfoRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByVinRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveReferenceSearchByPartCodesCriteria;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveMultiPartSearchResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.ivds.api.IvdsGtmotiveSearchService;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationRequest;
import com.sagag.services.ivds.response.GtmotiveResponse;
import com.sagag.services.service.api.GtmotiveBusinessService;
import com.sagag.services.service.resource.gtmotive.GtmotiveVehicleResource;
import com.sagag.services.service.response.gtmotive.GtmotivePartsListSearchResponse;
import com.sagag.services.service.response.gtmotive.GtmotiveReferenceSearchResponse;
import com.sagag.services.service.response.gtmotive.GtmotiveVehicleSearchResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.xml.xpath.XPathExpressionException;

@Service
@GtmotiveProfile
public class GtmotiveBusinessServiceImpl extends AbstractGtmotiveBusinessService
    implements GtmotiveBusinessService {

  @Autowired
  @Qualifier("ivdsGtmotiveSearchServiceImpl")
  protected IvdsGtmotiveSearchService ivdsGtmotiveSearchService;

  @Override
  public Optional<GtmotiveVehicleResource> searchVehicleByGtmotive(final String custNr,
      final GtmotiveVehicleCriteria criteria) {

    // RPB#355: GTMotive VinQuery API- Add method to accept with EstimateID
    updateExistedOrDefaultEstimateId(criteria, custNr);

    final Optional<GtmotiveVehicleDto> gtVehDto = gtmotiveService.getVehicleInfo(custNr, criteria);
    if (!gtVehDto.isPresent()) {
      return Optional.empty();
    }
    final GtmotiveVehicleDto gtResponse = gtVehDto.get();
    final String umc = gtResponse.getUmc();
    final List<String> models = gtResponse.getModelCodes();
    final List<String> engines = gtResponse.getEngineCodes();
    final List<String> transmisions = gtResponse.getTransmisionCodes();
    final Optional<VehicleDto> vehicle = ivdsVehicleService.searchGtmotiveVehicle(umc, models,
        engines, transmisions, StringUtils.EMPTY);
    if (!vehicle.isPresent()) {
      return Optional.empty();
    }
    return Optional.of(GtmotiveVehicleResource.of(gtResponse, vehicle.get()));
  }

  @Override
  public GtmotiveResponse searchArticlesByGtOperations(final UserInfo user,
      final GtmotiveOperationRequest request) {
    final GtmotiveResponse responseData =
        ivdsGtmotiveSearchService.searchArticlesByGtOperations(user, request);
    return processResponseData(request.getEstimateId(), request.getVin(), request.getVehicleCode())
        .apply(user, responseData);
  }

  @Override
  public GtmotiveReferenceSearchResponse searchReferencesByPartCode(
      GtmotiveReferenceSearchByPartCodesCriteria criteria)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    return gtmotiveReferenceSearchProcessor.process(criteria);
  }

  @Override
  public GtmotiveVehicleSearchResponse searchVehicleByGtInfo(String custNr,
      GtmotiveVehicleSearchByGtInfoRequest request) {
    Optional<GtmotiveVehicleDto> gtVehDto =
        gtmotiveService.searchVehicleInfoByGtInfo(custNr, request);
    if (!gtVehDto.isPresent()) {
      return null;
    }
    final GtmotiveVehicleDto gtResponse = gtVehDto.get();
    final String umc = gtResponse.getUmc();
    final List<String> models = gtResponse.getModelCodes();
    final List<String> engines = gtResponse.getEngineCodes();
    final List<String> transmisions = gtResponse.getTransmisionCodes();

    final Optional<VehicleDto> vehicleOpt = ivdsVehicleService.searchGtmotiveVehicle(umc, models,
        engines, transmisions, StringUtils.EMPTY);
    if (!vehicleOpt.isPresent()) {
      return null;
    }

    final GtmotiveVehicleDto gtVehicle = excludeSpecialEquipments(gtResponse);
    return GtmotiveVehicleSearchResponse.of(gtVehicle, vehicleOpt.get(),
        gtResponse.getEstimateId());
  }

  @Transactional
  @Override
  public GtmotiveVehicleSearchResponse searchVehicleByVin(String custNr,
      GtmotiveVehicleSearchByVinRequest request)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {

    final Optional<GtmotiveVehicleDto> gtVehDto = gtmotiveService.searchVehicleByVin(request);
    if (!gtVehDto.isPresent()) {
      return null;
    }

    final GtmotiveVehicleDto gtResponse =
        gtmotiveLastVehicleFinder.find(gtVehDto.get(), request, custNr);
    if (Objects.isNull(gtResponse)) {
      return null;
    }

    final String umc = gtResponse.getUmc();
    final List<String> models = gtResponse.getModelCodes();
    final List<String> engines = gtResponse.getEngineCodes();
    final List<String> transmisions = gtResponse.getTransmisionCodes();

    final Optional<VehicleDto> vehicleOpt = ivdsVehicleService.searchGtmotiveVehicle(umc, models,
        engines, transmisions, StringUtils.EMPTY);
    if (!vehicleOpt.isPresent()) {
      return null;
    }
    vehicleOpt.ifPresent(vehicle -> {
      vehicle.setVinSearch(true);
      vehicle.setVin(gtVehDto.get().getVin());
    });

    final GtmotiveVehicleDto gtVehicle = excludeSpecialEquipments(gtResponse);
    final VehicleDto vehicle = findMatchedModel(vehicleOpt.get(), umc);
    return GtmotiveVehicleSearchResponse.of(gtVehicle, vehicle, gtResponse.getEstimateId());
  }

  @Override
  public GtmotivePartsListSearchResponse searchGtPartsList(GtmotivePartsListSearchRequest request)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {

    return new GtmotivePartsListSearchResponse(gtmotiveService.searchVehiclePartsList(request));
  }

  @Override
  public GtmotiveVinSecurityCheckResponse checkVinSecurity(final Long userId, final String custNr,
      final GtmotiveVinSecurityCheckCriteria criteria)
      throws GtmotiveXmlResponseProcessingException, SocketTimeoutException,
      ConnectTimeoutException, ValidationException {
    return gtmotiveVinSecurityChecker.check(userId, custNr, criteria);
  }

  @Override
  public GtmotiveMultiPartSearchResponse searchMultiPart(GtmotiveMultiPartSearchRequest request)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    return gtmotiveService.searchMultiPart(request);
  }
}
