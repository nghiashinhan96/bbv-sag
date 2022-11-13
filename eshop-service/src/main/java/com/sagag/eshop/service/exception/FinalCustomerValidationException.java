package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

@Getter
public class FinalCustomerValidationException extends ValidationException {

  private static final long serialVersionUID = 8610111762982809754L;

  public FinalCustomerValidationException(FinalCustomerErrorCase error, String message) {
    super(message);
    setCode(error.code());
    setKey(error.key());
  }

  public enum FinalCustomerErrorCase implements IBusinessCode {
    FC_MSD_001("MISSING_DATA_FOR_CREATING"), FU_MSD_001("MISSING_DATA_FOR_UPDATING"), FC_HIP_001(
        "HAS_ORDER_IN_PROGRESS"), FC_MP_001(
            "MISS_PRICE_TYPE");

    private String key;

    FinalCustomerErrorCase(String key) {
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
