package com.sagag.services.oauth2.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnsupportedCompanyException extends OAuth2Exception {

  private static final long serialVersionUID = 1L;

  private final String company;

  public UnsupportedCompanyException(String company) {
    super("Does not support for company");
    this.company = company;
  }

  @Override
  public int getHttpErrorCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "unsupported_company";
  }
}
