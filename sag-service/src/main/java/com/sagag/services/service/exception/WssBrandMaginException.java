package com.sagag.services.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Exception class for margin brand validation.
 */
@Getter
public class WssBrandMaginException extends ValidationException {

  private static final long serialVersionUID = 7623052114092892174L;

  public WssBrandMaginException(WssMarginBrandErrorCase error, String message) {
    super(message);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * Margin brand error cases enumeration definition.
   */
  public enum WssMarginBrandErrorCase implements IBusinessCode {
    WMBEC_001("DUPLICATED_MARGIN_BRAND"),
    WMBEC_002("NOT_EXISTED_MARGIN_BRAND"),
    WMBEC_003("OBJECT_VALIDATOR"),
    WMBEC_004("INVALID_INDEX_BRAND"),
    WMBEC_005("INVALID_BRAND_UPDATE"),
    WMBEC_006("NO_DEFAULT_BRAND_SETTING"),
    WMBEC_007("EMPTY_MARGIN_BRAND_IMPORT_LIST")
    ;

    private String key;

    WssMarginBrandErrorCase(String key) {
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
