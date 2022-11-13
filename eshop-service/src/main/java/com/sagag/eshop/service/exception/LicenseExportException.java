package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.ServiceException;

public class LicenseExportException extends ServiceException {

  private static final long serialVersionUID = -80716416541051982L;

  public LicenseExportException(String message, Throwable throwable) {
    super(message, throwable);
  }

  @Override
  public String getCode() {
    return "EE_ALE_001";
  }

  @Override
  public String getKey() {
    return "ERROR_ADMIN_LICENSE_EXPORT";
  }
}
