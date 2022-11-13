package com.sagag.services.service.exception;

import com.sagag.services.common.exception.ServiceException;

/**
 * Exception class for offer export activity.
 */
public class OfferExportException extends ServiceException {

  private static final long serialVersionUID = -8411414094027942640L;

  public OfferExportException(final String msg, final Throwable cause) {
    super(msg, cause);
  }

  @Override
  public String getCode() {
    return "OE_OEE_001";
  }

  @Override
  public String getKey() {
    return "ERROR_OFFER_EXPORT";
  }

}
