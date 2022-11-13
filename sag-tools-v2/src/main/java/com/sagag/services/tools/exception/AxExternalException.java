package com.sagag.services.tools.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestClientResponseException;

@Data
@EqualsAndHashCode(callSuper = true)
public class AxExternalException extends RuntimeException {

  private static final long serialVersionUID = -2781092546293437630L;

  private String errorCode;

  private String messageKey;

  public AxExternalException(String msg) {
    super(msg);
  }

  public AxExternalException(IllegalArgumentException ex) {
    super(ex.getMessage());
    setErrorCode(StringUtils.EMPTY);
    setMessageKey(StringUtils.EMPTY);
  }

  public AxExternalException(RestClientResponseException ex) {
    super(ex.getMessage());
    setErrorCode(StringUtils.EMPTY);
    setMessageKey(StringUtils.EMPTY);
  }

}
