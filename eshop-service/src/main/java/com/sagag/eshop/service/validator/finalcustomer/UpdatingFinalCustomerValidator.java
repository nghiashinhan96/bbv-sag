package com.sagag.eshop.service.validator.finalcustomer;

import com.sagag.eshop.service.dto.finalcustomer.SavingFinalCustomerDto;
import com.sagag.eshop.service.exception.FinalCustomerValidationException;
import com.sagag.eshop.service.exception.FinalCustomerValidationException.FinalCustomerErrorCase;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.validator.IDataValidator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class UpdatingFinalCustomerValidator implements IDataValidator<SavingFinalCustomerDto> {

  @Override
  public boolean validate(SavingFinalCustomerDto finalCustomer) throws ValidationException {
    boolean isValid = Stream
        .of(finalCustomer.getIsActive(), finalCustomer.getShowNetPrice(),
            finalCustomer.getCustomerNr(), finalCustomer.getDeliveryId())
        .allMatch(Objects::nonNull)
        && StringUtils.isNoneBlank(finalCustomer.getCustomerName(), finalCustomer.getCustomerType(),
            finalCustomer.getSalutation(), finalCustomer.getSurName(), finalCustomer.getFirstName(),
            finalCustomer.getPostcode(), finalCustomer.getPlace());
    if (!isValid) {
      throw new FinalCustomerValidationException(FinalCustomerErrorCase.FU_MSD_001,
          "Missing data for updating final customer");
    }
    return isValid;
  }

}
