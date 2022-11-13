package com.sagag.services.service.exception;

import com.sagag.services.common.exception.ValidationException;

import java.util.Collections;
import java.util.Map;

/**
 * Exception class for version validation.
 */
public class VersionValidationException extends ValidationException {

  private static final long serialVersionUID = 7623052114092892174L;

  private String requestVersion;

  public VersionValidationException(String requestVersion) {
    super(String.format("Invalid version %s.", requestVersion));
    this.requestVersion = requestVersion;
    setMoreInfos(buildMoreInfos());
  }

  @Override
  public String getCode() {
    return "VE_IRV_001";
  }

  @Override
  public String getKey() {
    return "INVALID_VERSION";
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    return Collections.singletonMap("INFO_KEY_VERSION", requestVersion);
  }
}
