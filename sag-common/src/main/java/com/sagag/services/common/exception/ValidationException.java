package com.sagag.services.common.exception;

/**
 * The generic exception class for Validation service.
 * <p>
 * Since all validation checks are from Service layer, then this Validation is a Service exception.
 */
public abstract class ValidationException extends ServiceException {

  private static final long serialVersionUID = 3317373789240612084L;

  public ValidationException(String message) {
    super(message);
  }

}
