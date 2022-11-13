package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Generic exception class for branch validation.
 */
@Getter
public class BranchValidationException extends ValidationException {

  private static final long serialVersionUID = -3123857820574377528L;

  public BranchValidationException(BranchErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * Branch error cases enumeration definition.
   */
  public enum BranchErrorCase implements IBusinessCode {
    BE_DUB_001("DUPLICATED_BRANCH_NUMBER"),
    BE_DUB_002("DUPLICATED_BRANCH_CODE"),
    BE_BNE_001("BRANCH_NOT_EXIT"),
    BE_IT_001("INVALID_TIME"),
    BE_MOT_001("MISSING_OPENING_TIME"),
    BE_IR_001("INVALID_REQUEST");

    private String key;

    BranchErrorCase(String key) {
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
