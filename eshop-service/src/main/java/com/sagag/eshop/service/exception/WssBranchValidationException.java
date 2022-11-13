package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Generic exception class for WSS branch validation.
 */
@Getter
public class WssBranchValidationException extends ValidationException {

  private static final long serialVersionUID = -3123857820574377528L;

  public WssBranchValidationException(WssBranchErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * WSS Branch error cases enumeration definition.
   */
  public enum WssBranchErrorCase implements IBusinessCode {
    WBE_DUB_001("DUPLICATED_BRANCH_NUMBER"),
    WBE_DUB_002("DUPLICATED_BRANCH_CODE"),
    WBE_BNE_001("BRANCH_NOT_EXIT"),
    WBE_IT_001("INVALID_TIME"),
    WBE_MOT_001("MISSING_OPENING_TIME"),
    WBE_IR_001("INVALID_REQUEST"),
    WBE_BU_001("BRANCH_IN_USED");

    private String key;

    WssBranchErrorCase(String key) {
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
