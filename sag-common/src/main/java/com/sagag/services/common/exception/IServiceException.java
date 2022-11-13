package com.sagag.services.common.exception;

import java.util.Map;

/**
 * Interface to define the common building method for service exception.
 */
public interface IServiceException {

  /**
   * Builds more information for service exception instance.
   * 
   * @return the key-value information
   */
  Map<String, Object> buildMoreInfos();

  /**
   * Translate the exception to log String message.
   * 
   * @return log message
   */
  String toLogMessage();
}
