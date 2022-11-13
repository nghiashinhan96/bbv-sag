package com.sagag.services.ax.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestClientResponseException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = true)
public class AxExternalException extends RuntimeException {

  private static final long serialVersionUID = -2781092546293437630L;

  private String errorCode;

  private String messageKey;

  public AxExternalException(String msg) {
    super(msg);
    setErrorCode(StringUtils.EMPTY);
    setMessageKey(StringUtils.EMPTY);
  }

  public AxExternalException(final AxServerError errorEnum, String msg) {
    super(msg);
    setErrorCode(errorEnum.name());
    setMessageKey(errorEnum.getMsgKey());
  }

  public AxExternalException(IllegalArgumentException ex) {
    super(ex.getMessage());
    setErrorCode(AxServerError.AX.name());
    setMessageKey(AxServerError.AX.getMsgKey());
  }

  public AxExternalException(RestClientResponseException ex) {
    super(ex.getMessage());
    setErrorCode(AxServerError.AX.name());
    setMessageKey(AxServerError.AX.getMsgKey());
  }

  public static AxExternalException axInternalServerError(String msg) {
    return new AxExternalException(AxServerError.AX, msg);
  }

  public static AxExternalException connectInternalServerError(String msg) {
    return new AxExternalException(AxServerError.CONNECT, msg);
  }

  @Getter
  @AllArgsConstructor
  enum AxServerError {
    AX("AX_INTERNAL_SERVER_ERROR"), CONNECT("CONNECT_INTERNAL_SERVER_ERROR");
    private String msgKey;
  }

}
