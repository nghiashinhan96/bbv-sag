package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Generic exception class for external vendor validation.
 */
@Getter
public class ExternalVendorValidationException extends ValidationException {

  private static final long serialVersionUID = 64507419898747973L;

  public ExternalVendorValidationException(ExternalVendorErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * External vendor error cases enumeration definition.
   */
  public enum ExternalVendorErrorCase implements IBusinessCode {
    ODE_EMP_001("IMPORT_DATA_EMPTY"), ODE_EMP_003(
        "CAN_NOT_FIND_EXISTED_EXTERNAL_VENDOR"), ODE_EMP_005("BRAND_NOT_EXISTED"),
    EVE_IVI_001("INVALID_VENDOR_ID");

    private String key;

    ExternalVendorErrorCase(String key) {
      this.key = key;
    }

    @Override
    public String code() {
      return this.name();
    }

    @Override
    public String key() {
      return this.key;
    }

  }
}
