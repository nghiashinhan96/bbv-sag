package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

import java.util.function.Supplier;

/**
 * Generic exception class for WSS Tour validation.
 */
@Getter
public class WssTourValidationException extends ValidationException {

  private static final long serialVersionUID = 6999311739246449063L;

  public WssTourValidationException(WssTourErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  public static Supplier<WssTourValidationException> wssTourNotFound() {
    return () -> new WssTourValidationException(WssTourErrorCase.WTE_BNE_001,
        "cannot found wss tour");
  }

  /**
   * WSS Tour error cases enumeration definition.
   */
  public enum WssTourErrorCase implements IBusinessCode {
    WTE_DUB_001("DUPLICATED_TOUR_NAME"),
    WTE_DUB_002("DUPLICATED_WEEK_DAY"),
    WTE_BNE_001("WSS_TOUR_NOT_EXIST"),
    WTE_IT_001("INVALID_TIME"),
    WTE_IR_001("INVALID_REQUEST"),
    WTE_TU_001("TOUR_IN_USED");

    private String key;

    WssTourErrorCase(String key) {
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
