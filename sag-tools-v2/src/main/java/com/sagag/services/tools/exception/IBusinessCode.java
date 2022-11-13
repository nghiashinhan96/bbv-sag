package com.sagag.services.tools.exception;

/**
 * The business code interface.
 */
public interface IBusinessCode {

  /**
   * Returns the unique business error code for the exception.
   * 
   * @return the unique business error code.
   */
  String code();

  /**
   * Returns the error message key for the exception.
   * 
   * @return the multilingual error message key.
   */
  String key();
}
