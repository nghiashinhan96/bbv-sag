package com.sagag.eshop.service.validator.aad;

import com.sagag.eshop.service.api.AxAccountService;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.validator.IDataValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AadAccoutDuplicatedEmailValidator implements IDataValidator<String> {


  @Autowired
  private AxAccountService axAccountService;

  @Override
  public boolean validate(String email) throws ValidationException {
    return !axAccountService.searchSaleAccount(email).isPresent();
  }
}
