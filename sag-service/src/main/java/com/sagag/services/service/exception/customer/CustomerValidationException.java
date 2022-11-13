package com.sagag.services.service.exception.customer;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomerValidationException extends ValidationException {

  private static final long serialVersionUID = 3470222000875834508L;

  public CustomerValidationException(CustomerErrorCase error, String message) {
    super(message);
    setCode(error.code());
    setKey(error.key());
  }

  @AllArgsConstructor
  public enum CustomerErrorCase implements IBusinessCode {
    CE_IAF_001("INVALID_AFFILIATE"),
    CE_ICT_001("INVALID_CUSTOMER"),
    CE_ICU_001("INVALID_USERNAME_IN_CUSTOMER"),
    CE_IPC_001("INVALID_POST_CODE"),
    CE_CIN_001("CUSTOMER_INACTIVE"),
    CE_CBA_001("CUSTOMER_NOT_BELONG_TO_AFFILIATE"),
    CE_OTHER_001("OTHERS");

    private String key;

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
