package com.sagag.services.gtmotive.exception;

import com.sagag.services.common.exception.ServiceException;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * the GTMotive Xml response processing exception.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GtmotiveXmlResponseProcessingException extends ServiceException {

  private static final long serialVersionUID = 7264373804028737343L;

  public GtmotiveXmlResponseProcessingException(String message) {
    super(message);
  }

  public GtmotiveXmlResponseProcessingException(String message, Throwable cause) {
    super(message, cause);
  }

}
