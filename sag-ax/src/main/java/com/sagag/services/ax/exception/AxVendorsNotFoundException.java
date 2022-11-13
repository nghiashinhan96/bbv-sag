package com.sagag.services.ax.exception;

public class AxVendorsNotFoundException extends AxExternalException {

  private static final long serialVersionUID = 7896208386878094605L;

  public AxVendorsNotFoundException(Exception ex) {
    super(ex.getMessage());
    setErrorCode("EV_NFE_001");
    setMessageKey("EXTERNAL_VENDORS_NOT_FOUND");
  }
}
