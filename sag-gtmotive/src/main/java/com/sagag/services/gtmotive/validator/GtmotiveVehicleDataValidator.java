package com.sagag.services.gtmotive.validator;

import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleInfoCriteria;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@GtmotiveProfile
public class GtmotiveVehicleDataValidator implements IDataValidator<GtmotiveVehicleInfoCriteria> {

  @Override
  public boolean validate(GtmotiveVehicleInfoCriteria criteria) throws ValidationException {
    Assert.notNull(criteria.getUmc(), "The given umc must not be null.");
    Assert.notNull(criteria.getGtMod(), "The given model code must not be null.");
    Assert.notNull(criteria.getGtEng(), "The given engine code must not be null.");
    Assert.notNull(criteria.getGtDrv(), "The given transmision code must not be null.");
    return true;
  }

}
