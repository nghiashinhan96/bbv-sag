package com.sagag.services.ax.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class AxBadRequestException extends AxExternalException {

  private static final long serialVersionUID = 5192578969288001816L;

  public AxBadRequestException(String msg) {
    super(msg);
  }

}
