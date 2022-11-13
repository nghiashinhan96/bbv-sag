package com.sagag.services.oauth2.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class BlockedCustomerException extends OAuth2Exception {

  private static final long serialVersionUID = -3808635221498123585L;

  public BlockedCustomerException() {
    super(StringUtils.EMPTY);
  }

  @Override
  public int getHttpErrorCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "blocked_customer";
  }

}
