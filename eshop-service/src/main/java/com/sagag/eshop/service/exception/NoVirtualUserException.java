package com.sagag.eshop.service.exception;

import javax.validation.ValidationException;

public class NoVirtualUserException extends ValidationException {

  private static final long serialVersionUID = 1305964749317743328L;

  public NoVirtualUserException(String msg) {
    super(msg);
  }

  @Override
  public String getMessage() {
    return "NO_VIRTUAL_DVSE_USER_AVAILABLE";
  }
}
