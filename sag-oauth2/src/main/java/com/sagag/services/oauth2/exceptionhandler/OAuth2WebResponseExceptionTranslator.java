package com.sagag.services.oauth2.exceptionhandler;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

@Slf4j
public class OAuth2WebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

  @Override
  public ResponseEntity<OAuth2Exception> translate(Exception ex) throws Exception {
    log.error("Exception while authorize request", ex);
    return super.translate(ex);
  }

}
