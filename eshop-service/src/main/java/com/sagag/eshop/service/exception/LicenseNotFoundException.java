package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.ValidationException;

import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.Map;

/**
 * Exception class for license validation.
 */
@EqualsAndHashCode(callSuper = false)
public class LicenseNotFoundException extends ValidationException {

  private static final long serialVersionUID = 5746674115771303464L;

  private final String packName;

  public LicenseNotFoundException(String packName) {
    super(String.format("License settings for packName %s not found", packName));
    this.packName = packName;
    setMoreInfos(buildMoreInfos());
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    return Collections.singletonMap("INFO_KEY_PACKNAME", packName);
  }

  @Override
  public String getCode() {
    return "LE_NFO_001";
  }

  @Override
  public String getKey() {
    return "LICENSE_NOT_FOUND";
  }
}
