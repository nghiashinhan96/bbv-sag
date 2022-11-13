package com.sagag.services.rest.app;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.service.exception.VersionValidationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Version validator configuration class.
 */
@Component
public class VersionValidator {

  @Value("${app.version}")
  private String appVersion;

  public boolean validate(HttpServletRequest request) throws VersionValidationException {
    final String requestVersion = request.getHeader(SagConstants.HEADER_X_VERSION);
    if (requestVersion == null || StringUtils.equals(requestVersion, appVersion)) {
      return true;
    } else {
      throw new VersionValidationException(requestVersion);
    }
  }
}
