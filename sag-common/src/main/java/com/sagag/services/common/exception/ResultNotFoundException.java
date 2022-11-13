package com.sagag.services.common.exception;

public class ResultNotFoundException extends Exception {

  private static final long serialVersionUID = 7324662681836722216L;

  public ResultNotFoundException() {
    super();
  }

  public ResultNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResultNotFoundException(String message) {
    super(message);
  }

  public ResultNotFoundException(Throwable cause) {
    super(cause);
  }
}
