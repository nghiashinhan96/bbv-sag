package com.sagag.services.service.validator;

import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.service.exception.customer.CustomerValidationException;
import com.sagag.services.service.validator.criteria.AxCustomerCriteria;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class CustomerValidator implements IDataValidator<AxCustomerCriteria> {

  @Autowired
  protected VUserDetailRepository vUserDetailRepo;

  @Override
  public abstract boolean validate(AxCustomerCriteria criteria) throws CustomerValidationException;

}
