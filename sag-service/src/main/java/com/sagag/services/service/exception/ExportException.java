package com.sagag.services.service.exception;

import com.sagag.services.common.exception.ServiceException;

/**
 * Exception class for export activity.
 */
public class ExportException extends ServiceException {

  private static final long serialVersionUID = -8411414094027942640L;

  public ExportException(final String msg, final Throwable cause) {
    super(msg, cause);
  }
}
