package com.sagag.services.admin.exception;

import com.sagag.services.common.exception.ServiceException;

/**
 * Exception class for user export activity.
 */
public class UserExportException extends ServiceException {

  private static final long serialVersionUID = -6447385831314919220L;

  public UserExportException(final String msg, final Throwable cause) {
    super(msg, cause);
  }

  @Override
  public String getCode() {
    return "EE_AUE_001";
  }

  @Override
  public String getKey() {
    return "ERROR_ADMIN_USER_EXPORT";
  }
}
