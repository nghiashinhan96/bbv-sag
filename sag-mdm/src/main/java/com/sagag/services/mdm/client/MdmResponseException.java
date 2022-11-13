package com.sagag.services.mdm.client;

import lombok.Getter;

/**
 * Class for handling throw MDM exception.
 *
 */
@Getter
public class MdmResponseException extends RuntimeException {

  private static final long serialVersionUID = 9152879817355294160L;
  private int errorCode;

  public MdmResponseException(String msg) {
    super(msg);
  }

  public MdmResponseException(int errorCode, String msg) {
    super(msg);
    this.errorCode = errorCode;
  }

}
