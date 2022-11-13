package com.sagag.services.gtmotive.api.impl;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.SagStringUtils;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.builder.GraphicalSelectedPartsResponseBuilder;
import com.sagag.services.gtmotive.builder.gtinterface.GtmotiveVehicleInfoResponseBuilder;
import com.sagag.services.gtmotive.builder.gtinterface.GtmotiveVinSecurityCheckResponseBuilder;
import com.sagag.services.gtmotive.client.GtmotiveInterfaceClient;
import com.sagag.services.gtmotive.client.GtmotiveMainModuleClient;
import com.sagag.services.gtmotive.client.GtmotiveVehicleClient;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.request.EstimateIdOperationMode;
import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import com.sagag.services.gtmotive.domain.request.GtmotiveRequestMode;
import com.sagag.services.gtmotive.domain.request.GtmotiveVehicleCriteria;
import com.sagag.services.gtmotive.domain.response.GtVehicleInfoResponse;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveMultiPartSearchRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleInfoCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByGtInfoRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByVinRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveMultiPartSearchResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotivePartInfoResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVehicleInfoResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionsResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsListResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.xml.xpath.XPathExpressionException;

/**
 * Service implementation class for Gtmotive.
 */
@Service
@GtmotiveProfile
@Slf4j
public class GtmotiveServiceImpl implements GtmotiveService {

  @Autowired
  private GtmotiveVehicleClient gtmotiveVehicleClient;

  @Autowired
  private GtmotiveInterfaceClient gtmotiveInterfaceClient;

  @Autowired
  private GtmotiveMainModuleClient gtmotiveMainModuleClient;

  @LogExecutionTime
  @Override
  public String getMakeCodeFromVinDecoder(final String vin) {
    final GtVehicleInfoResponse response = gtmotiveVehicleClient.getVinDecoder(
        GtmotiveUtils.modifyVinCode(vin));
    if (Objects.isNull(response)) {
      return StringUtils.EMPTY;
    }
    return StringUtils.defaultIfBlank(response.getMakeCode(), StringUtils.EMPTY);
  }

  @LogExecutionTime
  @Override
  public Optional<GtmotiveVehicleDto> getVehicleInfo(final String custNr,
      final GtmotiveVehicleCriteria vehCriteria) {
    Assert.hasText(custNr, "The given customer number must not be null");
    Assert.notNull(vehCriteria, "The vehicle criteria must not be null");
    Assert.isTrue(vehCriteria.isValidMode(), "The request mode must not be valid");

    final BiFunction<String, GtmotiveVehicleCriteria, GtmotiveCriteria> converter;
    if (vehCriteria.isVinMode()) {
      converter = vinRequestConverter();
    } else {
      converter = serviceScheduleRequestConverter();
    }

    final GtmotiveCriteria criteria = converter.apply(custNr, vehCriteria);
    if (criteria == null) {
      return Optional.empty();
    }

    GtmotiveVehicleInfoResponse response = getVehicleInfo(criteria);
    return GtmotiveVehicleDto.converter().apply(response);
  }

  private BiFunction<String, GtmotiveVehicleCriteria, GtmotiveCriteria> vinRequestConverter() {
    return (custNr, vehCriteria) -> {
      final String makeCode = getMakeCodeFromVinDecoder(vehCriteria.getModifiedVin());
      if (StringUtils.isBlank(makeCode)) {
        return null;
      }
      GtmotiveCriteria criteria = new GtmotiveCriteria();
      criteria.setVin(vehCriteria.getModifiedVin());
      criteria.setEstimateId(vehCriteria.getEstimateId());
      criteria.setRequestMode(GtmotiveRequestMode.VIN);
      return criteria;
    };
  }

  private BiFunction<String, GtmotiveVehicleCriteria,
      GtmotiveCriteria> serviceScheduleRequestConverter() {
    return (custNr, vehCriteria) -> {
      GtmotiveCriteria criteria = new GtmotiveCriteria();
      criteria.setGtDrv(SagStringUtils.toEmptyIfNullValue(vehCriteria.getGtDrv()));
      criteria.setGtEng(SagStringUtils.toEmptyIfNullValue(vehCriteria.getGtEng()));
      criteria.setGtMod(SagStringUtils.toEmptyIfNullValue(vehCriteria.getGtMod()));
      criteria.setUmc(SagStringUtils.toEmptyIfNullValue(vehCriteria.getUmc()));
      criteria.setEstimateId(vehCriteria.getEstimateId());
      criteria.setRequestMode(GtmotiveRequestMode.SERVICE_SCHEDULE);
      return criteria;
    };
  }

  private GtmotiveVehicleInfoResponse getVehicleInfo(GtmotiveCriteria criteria) {
    gtmotiveInterfaceClient.getGraphicalIFrameInfoXml(criteria);
    try {
      final String unescapseXmlData = gtmotiveInterfaceClient.getVehicleInfo(criteria);
      return new GraphicalSelectedPartsResponseBuilder().unescapseXmlData(unescapseXmlData).build();
    } catch (GtmotiveXmlResponseProcessingException ex) {
      return null;
    }
  }

  @LogExecutionTime
  @Override
  public GtmotivePartsThreeResponse searchReferencesByPartCode(
      GtmotivePartsThreeSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    log.debug("Started GtmotiveServiceImpl -> searchReferencesByPartCode()");
    return gtmotiveMainModuleClient.searchReferencesByPartCode(searchRequest);
  }

  @LogExecutionTime
  @Override
  public GtmotivePartsListResponse searchVehiclePartsList(
      GtmotivePartsListSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    log.debug("Started GtmotiveServiceImpl -> searchPartsList()");
    return gtmotiveMainModuleClient.searchVehiclePartList(searchRequest);
  }

  @LogExecutionTime
  @Override
  public Optional<GtmotiveVehicleDto> searchVehicleInfoByGtInfo(final String custNr,
      final GtmotiveVehicleSearchByGtInfoRequest request) {
    final GtmotiveVehicleInfoCriteria criteria = GtmotiveVehicleInfoCriteria.builder()
        .gtDrv(SagStringUtils.toEmptyIfNullValue(request.getGtDrv()))
        .gtEng(SagStringUtils.toEmptyIfNullValue(request.getGtEng()))
        .gtMod(SagStringUtils.toEmptyIfNullValue(request.getGtMod()))
        .umc(SagStringUtils.toEmptyIfNullValue(request.getUmc()))
        .estimateId(GtmotiveUtils.generateEstimateId(custNr))
        .operation(EstimateIdOperationMode.CREATE).requestMode(GtmotiveRequestMode.SERVICE_SCHEDULE)
        .build();
    GtmotiveVehicleInfoResponse response =
        getVehicleInfo(gtmotiveInterfaceClient.registerNewEstimateId(criteria));
    return GtmotiveVehicleDto.converter().apply(response);
  }

  @Override
  public Optional<GtmotiveVehicleDto> searchVehicleByVin(
      GtmotiveVehicleSearchByVinRequest request) {
    final String vin = request.getVin();
    final String makeCode = getMakeCodeFromVinDecoder(GtmotiveUtils.modifyVinCode(vin));
    if (StringUtils.isBlank(makeCode)) {
      return Optional.empty();
    }

    GtmotiveVehicleInfoCriteria criteria = new GtmotiveVehicleInfoCriteria();
    criteria.setVin(vin);
    criteria.setEstimateId(request.getEstimateId());
    criteria.setOperation(EstimateIdOperationMode.READ);
    criteria.setRequestMode(GtmotiveRequestMode.VIN);


    GtmotiveVehicleInfoResponse response = getVehicleInfo(criteria);
    return GtmotiveVehicleDto.converter().apply(response);
  }

  @Override
  public GtmotiveVehicleInfoResponse getVehicleInfo(GtmotiveVehicleInfoCriteria criteria) {
    try {
      final String unescapseXmlData = gtmotiveInterfaceClient.getVehicleInfo(criteria);
      return new GtmotiveVehicleInfoResponseBuilder().unescapseXmlData(unescapseXmlData).build();
    } catch (GtmotiveXmlResponseProcessingException ex) {
      return null;
    }
  }

  @LogExecutionTime
  @Override
  public GtmotiveVinSecurityCheckResponse checkVinSecurity(
      final GtmotiveVinSecurityCheckCriteria criteria)
      throws GtmotiveXmlResponseProcessingException {
    log.debug("Started GtmotiveServiceImpl -> checkVinSecurity()");
    final String unescapseXmlData = gtmotiveInterfaceClient.getVinSecurityCheck(criteria);
    return new GtmotiveVinSecurityCheckResponseBuilder().unescapseXmlData(unescapseXmlData)
        .estimateId(criteria.getEstimateId()).vin(criteria.getModifiedVin()).build();
  }

  @Override
  public GtmotiveMultiPartSearchResponse searchMultiPart(GtmotiveMultiPartSearchRequest request)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {

    gtmotiveInterfaceClient.partUpdate(request.getPartUpdateRequest());
    GtmotivePartInfoResponse partInfoResponse =
        gtmotiveInterfaceClient.getPartInfo(request.getPartInfoRequest());
    GtmotiveMultiPartSearchResponse multipartSearchResponse = new GtmotiveMultiPartSearchResponse();
    multipartSearchResponse.setOperations(partInfoResponse.getOperations());
    return multipartSearchResponse;
  }

  @Override
  public void registerNewEstimateId(GtmotiveVehicleInfoCriteria criteria) {
    gtmotiveInterfaceClient.registerNewEstimateId(criteria);
  }

  @Override
  public GtmotiveEquipmentOptionsResponse searchEquipmentOptions(
      GtmotiveEquipmentOptionsSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    return gtmotiveMainModuleClient.searchEquipmentOptions(searchRequest);
  }
}
