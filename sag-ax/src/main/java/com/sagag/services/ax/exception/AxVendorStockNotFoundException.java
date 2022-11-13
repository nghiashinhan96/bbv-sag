package com.sagag.services.ax.exception;

public class AxVendorStockNotFoundException extends AxExternalException {

  private static final long serialVersionUID = -3778846858179158010L;

  public AxVendorStockNotFoundException(Exception ex) {
    super(ex.getMessage());
    setErrorCode("EVS_NFE_001");
    setMessageKey("EXTERNAL_VENDOR_STOCK_NOT_FOUND");
  }
}
