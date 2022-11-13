package com.sagag.services.copydb.batch;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * General batch job exception.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BatchJobException extends Exception {

  private static final String DEFAULT_EXCEPTION_MSG = "Job has exception";

  private static final long serialVersionUID = -7552502793980527440L;

  private final String message;

  public BatchJobException() {
    super();
    this.message = DEFAULT_EXCEPTION_MSG;
  }

  public BatchJobException(String msg) {
    super(msg);
    this.message = msg;
  }

  public BatchJobException(Throwable cause) {
    super(cause);
    this.message = DEFAULT_EXCEPTION_MSG;
  }

  public BatchJobException(String msg, Throwable cause) {
    super(msg, cause);
    this.message = msg;
  }
}
