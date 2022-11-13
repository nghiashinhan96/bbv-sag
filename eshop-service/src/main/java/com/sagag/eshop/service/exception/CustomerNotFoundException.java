package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.ServiceException;

public class CustomerNotFoundException extends ServiceException {

  private static final long serialVersionUID = 7369851738182820726L;

  public CustomerNotFoundException(String message) {
    super(message);
  }

  @Override
  public String getCode() {
    return "CE_NF_001";
  }

  @Override
  public String getKey() {
    return "CUSTOMER_NOT_FOUND";
  }
}
