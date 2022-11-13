package com.sagag.services.elasticsearch.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResultOverLimitException extends RuntimeException {

  private static final long serialVersionUID = -7679285527176422927L;

  private static final String ERROR_CODE = "RESULT_OVER_LIMITATION";

  /** the business error code for specific case. */
  private String code;

  public ResultOverLimitException() {
    super("The result is over max limitation");
    setCode(ERROR_CODE);
  }

}
