package com.sagag.services.service.validator;

import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.validator.IDataValidator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OnBehalfUserValidator implements IDataValidator<String> {

  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Override
  public boolean validate(String onBehalfUsername) throws UserValidationException {
    if (StringUtils.isBlank(onBehalfUsername)) {
      return false;
    }
    return vUserDetailRepo.existsOnBehalfUserName(onBehalfUsername);
  }
}
