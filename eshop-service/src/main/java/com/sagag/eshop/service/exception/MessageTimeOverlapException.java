package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.ServiceException;

/**
 * This exception handle for the case message is overlap.
 *
 */
public class MessageTimeOverlapException extends ServiceException {

  private static final long serialVersionUID = 5891395137692531551L;

  public MessageTimeOverlapException(String message) {
    super(message);
  }

  @Override
  public String getCode() {
    return "MS_TOL_001";
  }

  @Override
  public String getKey() {
    return "MESSAGE_TIME_OVERLAP";
  }

}
