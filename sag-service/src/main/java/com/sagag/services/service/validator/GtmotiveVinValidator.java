package com.sagag.services.service.validator;

import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.domain.request.EstimateIdOperationMode;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleInfoCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVehicleInfoResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GtmotiveVinValidator implements IDataValidator<GtmotiveVinSecurityCheckCriteria> {

  @Autowired
  private GtmotiveService gtmotiveService;

  @Autowired
  private GtmotiveVehicleValidator gtmotiveVehicleValidator;

  @Override
  public boolean validate(GtmotiveVinSecurityCheckCriteria criteria) throws ValidationException {
    GtmotiveVehicleInfoCriteria gtmotiveCriteria = criteria.toGtmotiveVehicleInfoCriteria();
    gtmotiveCriteria.setOperation(EstimateIdOperationMode.READ);

    GtmotiveVehicleInfoResponse response = gtmotiveService.getVehicleInfo(gtmotiveCriteria);
    if (Objects.isNull(response)) {
      return false;
    }
    return gtmotiveVehicleValidator.validate(response);
  }
}
