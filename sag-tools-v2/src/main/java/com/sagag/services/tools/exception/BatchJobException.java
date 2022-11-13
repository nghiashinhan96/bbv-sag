package com.sagag.services.tools.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * General batch job exception.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BatchJobException extends Exception {

  private static final long serialVersionUID = -7552502793980527440L;

  private String message;

  public BatchJobException() {
    super();
    this.message = "Job has exception";
  }

  public BatchJobException(String msg) {
    super(msg);
    this.message = msg;
  }

  public BatchJobException(Throwable cause) {
    super(cause);
  }

  public BatchJobException(String msg, Throwable cause) {
    super(msg, cause);
    this.message = msg;
  }
}
