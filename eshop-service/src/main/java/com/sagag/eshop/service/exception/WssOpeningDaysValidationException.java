package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Generic exception class for WSS opening days calendar validation.
 */
@Getter
public class WssOpeningDaysValidationException extends ValidationException {

  private static final long serialVersionUID = -3123857820574377528L;

  public WssOpeningDaysValidationException(WssOpeningDaysErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * WSS Opening days error cases enumeration definition.
   */
  public enum WssOpeningDaysErrorCase implements IBusinessCode {
    WODE_DUB_001("DUPLICATED_OPENING_DAYS"),
    WODE_DUB_002("IMPORT_DUPLICATED_OPENING_DAYS"),
    WODE_EMP_003("IMPORT_DATA_EMPTY");

    private String key;

    WssOpeningDaysErrorCase(String key) {
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
