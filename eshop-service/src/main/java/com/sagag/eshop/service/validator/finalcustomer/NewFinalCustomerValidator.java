package com.sagag.eshop.service.validator.finalcustomer;

import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.exception.FinalCustomerValidationException;
import com.sagag.eshop.service.exception.FinalCustomerValidationException.FinalCustomerErrorCase;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.validator.IDataValidator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NewFinalCustomerValidator implements IDataValidator<NewFinalCustomerDto> {

  @Autowired
  private UpdatingFinalCustomerValidator updatingFinalCustomerValidator;

  @Override
  public boolean validate(NewFinalCustomerDto finalCustomer) throws ValidationException {
    boolean isValid = Objects.nonNull(finalCustomer.getCustomerOrgId())
        && StringUtils.isNoneBlank(finalCustomer.getCollectionShortname())
        && updatingFinalCustomerValidator.validate(finalCustomer);
    if (!isValid) {
      throw new FinalCustomerValidationException(FinalCustomerErrorCase.FC_MSD_001,
          "Missing data for creating final customer");
    }
    return isValid;
  }
}
