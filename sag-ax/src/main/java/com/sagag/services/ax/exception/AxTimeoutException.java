package com.sagag.services.ax.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class AxTimeoutException extends AxExternalException {

  private static final long serialVersionUID = 7469585696247329429L;

  public AxTimeoutException(AxTimeoutErrorCode errorCode, String msg) {
    super(msg);
    setErrorCode(errorCode.name());
    setMessageKey(errorCode.getMsgKey());
  }

  public static AxTimeoutException axGatewayTimeoutException(String msg) {
    return new AxTimeoutException(AxTimeoutErrorCode.GATEWAY_TIMEOUT, msg);
  }

  public static AxTimeoutException connectionTimeoutException(String msg) {
    return new AxTimeoutException(AxTimeoutErrorCode.CONNECTION, msg);
  }

  public static AxTimeoutException requestTimeoutException(String msg) {
    return new AxTimeoutException(AxTimeoutErrorCode.REQUEST_TIMEOUT, msg);
  }

  @Getter
  @AllArgsConstructor
  enum AxTimeoutErrorCode{
    CONNECTION("CONNECTION_TIMEOUT"), GATEWAY_TIMEOUT("AX_GATEWAY_TIMEOUT"),
    REQUEST_TIMEOUT("REQUEST_TIMEOUT");

    private String msgKey;
  }

}
