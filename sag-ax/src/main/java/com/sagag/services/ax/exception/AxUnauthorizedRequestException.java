package com.sagag.services.ax.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class AxUnauthorizedRequestException extends AxExternalException {

  private static final long serialVersionUID = 5268941460902206670L;

  public AxUnauthorizedRequestException(String msg) {
    super(msg);
  }
}
