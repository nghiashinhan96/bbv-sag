package com.sagag.services.service.validator;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.common.contants.EshopUserConstants;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;

import org.apache.commons.lang3.StringUtils;

public class AbstractUserAdminValidator implements IDataValidator<UserRegistrationDto> {

  @Override
  public boolean validate(UserRegistrationDto criteria) throws UserValidationException {
    final String custNumber = criteria.getCustomerNumber();
    if (StringUtils.isBlank(custNumber) || !StringUtils.isNumeric(custNumber)) {
      throw new UserValidationException(UserErrorCase.UE_ICT_001,
          "Customer number is not valid: " + custNumber);
    }

    if (StringUtils.equalsIgnoreCase(criteria.getUserName(),
        EshopUserConstants.ON_BEHALF_AGENT_PREFIX + custNumber)) {
      throw new UserValidationException(UserErrorCase.UE_ICU_001,
          "User name input must not be the same with virtual on behalf admin user convention name !"
              + " It will be follow up created after user admin creation.");
    }
    return true;
  }

}
