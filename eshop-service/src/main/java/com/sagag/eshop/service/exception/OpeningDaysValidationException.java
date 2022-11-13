package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Generic exception class for opening days calendar validation.
 */
@Getter
public class OpeningDaysValidationException extends ValidationException {

  private static final long serialVersionUID = -3123857820574377528L;

  public OpeningDaysValidationException(OpeningDaysErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * Opening days error cases enumeration definition.
   */
  public enum OpeningDaysErrorCase implements IBusinessCode {
    ODE_DUB_001("DUPLICATED_OPENING_DAYS"),
    ODE_DUB_002("IMPORT_DUPLICATED_OPENING_DAYS"),
    ODE_EMP_003("IMPORT_DATA_EMPTY");

    private String key;

    OpeningDaysErrorCase(String key) {
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
