package com.sagag.eshop.service.validator.aad;

import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.domain.eshop.dto.AadAccountsDto;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AadAccoutRequiredFieldsValidator implements IDataValidator<AadAccountsDto> {

  @Override
  public boolean validate(AadAccountsDto dto) throws ValidationException {
    Assert.hasText(dto.getPersonalNumber(), "Personal number must not be empty");
    Assert.hasText(dto.getFirstName(), "First name must not be empty");
    Assert.hasText(dto.getLastName(), "Last name must not be empty");
    Assert.hasText(dto.getPrimaryContactEmail(), "Email must not be empty");
    return true;
  }
}
