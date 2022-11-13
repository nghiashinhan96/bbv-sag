package com.sagag.services.oauth2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class WholesalerPermisisonAccessDeniedException extends OAuth2Exception {

  private static final long serialVersionUID = -6947270218979551394L;

  public WholesalerPermisisonAccessDeniedException(String msg) {
    super(msg);
  }

  @Override
  public int getHttpErrorCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "wholesaler_permission_access_denied";
  }

}
