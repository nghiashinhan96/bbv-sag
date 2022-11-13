package com.sagag.services.service.gtmotive.impl;

import com.sagag.eshop.service.api.VinErrorLogService;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.VinErrorLogDto;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.request.EstimateIdOperationMode;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.gtmotive.utils.GtmotiveConstant;
import com.sagag.services.service.gtmotive.GtmotiveVehicleSearchRetryer;
import com.sagag.services.service.validator.GtmotiveVinValidator;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.Date;

@Component
@GtmotiveProfile
@Slf4j
public class GtmotiveVehicleSearchRetryerImpl implements GtmotiveVehicleSearchRetryer {

  private static final int ERROR_41 = 41;

  private static final int ERROR_43 = 43;

  private static final int ERROR_24 = 24;

  private static final int ERROR_14 = 14;

  private static final int ERROR_9 = 9;

  @Autowired
  private GtmotiveService gtmotiveService;

  @Autowired
  private GtmotiveVinValidator gtmotiveVinValidator;

  @Autowired
  private VinErrorLogService vinErrorLogService;

  @Override
  public GtmotiveVinSecurityCheckResponse retryWhenError(GtmotiveVinSecurityCheckResponse response,
      GtmotiveVinSecurityCheckCriteria criteria) throws GtmotiveXmlResponseProcessingException,
      SocketTimeoutException, ConnectTimeoutException, ValidationException {
    log.debug("The current error code = {}", response.getErrorCode());

    switch (response.getErrorCode()) {

      case ERROR_24:
        return handleRetryUpdateEstimateIdOperation(criteria);

      case ERROR_43:
        return handleRetryCheckVinSecurity(criteria);

      case ERROR_14:
        saveError14Log(criteria);
        return response;

      case ERROR_41:
        saveVinNotExistErrorLog(criteria);
        return response;

      case ERROR_9:
        saveVinDecodeErrorLog(criteria, response);
        return response;

      default:
        return response;
    }
  }

  private void saveError14Log(GtmotiveVinSecurityCheckCriteria criteria) {
    VinErrorLogDto vinError = VinErrorLogDto.builder().vin(criteria.getVin())
        .type(GtmotiveConstant.VIN_ERROR_CODE_14).createdDate(new Date()).build();
    vinErrorLogService.addVinErrorLog(vinError);
  }

  private void saveVinNotExistErrorLog(GtmotiveVinSecurityCheckCriteria criteria) {
    VinErrorLogDto vinError = VinErrorLogDto.builder().vin(criteria.getVin())
        .type(GtmotiveConstant.VIN_ERROR_VIN_NOT_EXIST).createdDate(new Date()).build();
    vinErrorLogService.addVinErrorLog(vinError);
  }

  private void saveVinDecodeErrorLog(GtmotiveVinSecurityCheckCriteria criteria,
      GtmotiveVinSecurityCheckResponse response) {
    VinErrorLogDto vinErrorLog = VinErrorLogDto.builder().vin(criteria.getVin())
        .umc(criteria.getUmc()).returnedData(SagJSONUtil.convertObjectToJson(response))
        .type(GtmotiveConstant.VIN_ERROR_VIN_DECODE_ERROR).createdDate(new Date()).build();
    vinErrorLogService.addVinErrorLog(vinErrorLog);
  }

  private GtmotiveVinSecurityCheckResponse handleRetryUpdateEstimateIdOperation(
      GtmotiveVinSecurityCheckCriteria criteria)
      throws ValidationException, GtmotiveXmlResponseProcessingException {
    if (gtmotiveVinValidator.validate(criteria)) {
      criteria.setShowGui(true);
      criteria.setUseIdCar(false);
    }
    criteria.setOperation(EstimateIdOperationMode.UPDATE);

    return handleRetryCheckVinSecurity(criteria);
  }

  private GtmotiveVinSecurityCheckResponse handleRetryCheckVinSecurity(
      GtmotiveVinSecurityCheckCriteria criteria) throws GtmotiveXmlResponseProcessingException {
    return gtmotiveService.checkVinSecurity(criteria);
  }
}
