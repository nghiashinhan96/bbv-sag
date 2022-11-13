package com.sagag.services.service.gtmotive.impl;

import com.sagag.eshop.repo.api.VinLoggingRepository;
import com.sagag.eshop.repo.entity.VinLogging;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.SagStringUtils;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.request.EstimateIdOperationMode;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;
import com.sagag.services.gtmotive.validator.GtmotiveVehicleDataValidator;
import com.sagag.services.service.gtmotive.GtmotiveVinSecurityChecker;
import com.sagag.services.service.validator.GtmotiveVinDecoderValidator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.Objects;

@Component
@GtmotiveProfile
public class GtmotiveVinSecurityCheckerImpl implements GtmotiveVinSecurityChecker {

  @Autowired
  private GtmotiveVinDecoderValidator gtmotiveVinDecoderValidator;
  @Autowired
  private GtmotiveService gtmotiveService;
  @Autowired
  private VinLoggingRepository vinLoggingRepo;
  @Autowired
  private GtmotiveVehicleSearchRetryerImpl gtmotiveVehicleSearchRetryer;
  @Autowired
  private GtmotiveVehicleDataValidator gtmotiveVehicleDataValidator;

  @Override
  public GtmotiveVinSecurityCheckResponse check(final Long userId, final String custNr,
    final GtmotiveVinSecurityCheckCriteria criteria) throws GtmotiveXmlResponseProcessingException,
      SocketTimeoutException, ConnectTimeoutException, ValidationException {

    if (criteria.getRequestMode().isVin() && !gtmotiveVinDecoderValidator.validate(criteria)) {
      return GtmotiveVinSecurityCheckResponse.unSupportedVinMakeCode(criteria.getMakeCode());
    }

    // #870: Get exist estimate id or generate new once
    updateExistedOrDefaultEstimateId(criteria, custNr);
    criteria.setUseIdCar(true);
    if (criteria.getRequestMode().isTabClicking()) {
      gtmotiveVehicleDataValidator.validate(criteria);
      // Process "null" text to EMPTY string
      criteria.setUmc(SagStringUtils.toEmptyIfNullValue(criteria.getUmc()));
      criteria.setGtMod(SagStringUtils.toEmptyIfNullValue(criteria.getGtMod()));
      criteria.setGtEng(SagStringUtils.toEmptyIfNullValue(criteria.getGtEng()));
      criteria.setGtDrv(SagStringUtils.toEmptyIfNullValue(criteria.getGtDrv()));
    }

    GtmotiveVinSecurityCheckResponse response = gtmotiveService.checkVinSecurity(criteria);
    if (Objects.isNull(response.getErrorCode())) {
      return response;
    }
    return gtmotiveVehicleSearchRetryer.retryWhenError(response, criteria);
  }

  /**
   * Updates the existed estimate id or generate new once.
   *
   * @param criteria the gtmotive criteria
   * @param customerNr the customer number
   * @return a existed or new estimate id {@link String}
   */
  private void updateExistedOrDefaultEstimateId(final GtmotiveVinSecurityCheckCriteria criteria,
    final String customerNr) {
    String estimatedId = StringUtils.EMPTY;
    final Long custNr = Long.valueOf(customerNr);
    if (criteria.isVinRequest()) {
      estimatedId = vinLoggingRepo.findVinLogByEstimateUsed(criteria.getModifiedVin(), custNr)
        .map(VinLogging::getEstimateID).orElse(StringUtils.EMPTY);
    }
    if (StringUtils.isNotBlank(estimatedId)) {
      criteria.setEstimateId(estimatedId);
      criteria.setOperation(EstimateIdOperationMode.UPDATE);
    } else {
      criteria.setEstimateId(GtmotiveUtils.generateEstimateId(customerNr));
      criteria.setOperation(EstimateIdOperationMode.CREATE);
    }
  }
}
