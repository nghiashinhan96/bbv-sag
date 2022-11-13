package com.sagag.services.oauth2.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnsupportedAffiliateException extends OAuth2Exception {

  private static final long serialVersionUID = 132294758194419267L;

  private final String affiliate;

  public UnsupportedAffiliateException(String affiliate) {
    super("User does not match with request affiliate");
    this.affiliate = affiliate;
  }

  @Override
  public int getHttpErrorCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "unsupported_affiliate";
  }

}
