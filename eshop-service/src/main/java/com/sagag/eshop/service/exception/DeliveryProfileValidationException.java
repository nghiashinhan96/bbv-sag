package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Generic exception class for delivery profile validation.
 */
@Getter
public class DeliveryProfileValidationException extends ValidationException {

  private static final long serialVersionUID = 1132145919098943009L;

  public DeliveryProfileValidationException(DeliveryProfileErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * Delivery profile cases enumeration definition.
   */
  public enum DeliveryProfileErrorCase implements IBusinessCode {
    ODE_EMP_001("IMPORT_DATA_EMPTY"), ODE_EMP_002("DUPLICATE DATA"), ODE_EMP_003(
        "CAN_NOT_FIND_EXISTED_DELIVERY_PROFILE"), ODE_EMP_004("VALIDATION_ERROR"), ODE_EMP_005(
            "DELIVERY_PROFILE_IS_USED_BY_EXTERNAL_VENDOR"), ODE_EMP_006(
                "DUPLICATE_DELIVERY_BRANCH"), ODE_EMP_007(
                    "DUPLICATE_DELIVERY_PROFILE_NAME"), ODE_EMP_008(
                        "NON_EXISTED_DELIVERY_PROFILE_ID");

    private String key;

    DeliveryProfileErrorCase(String key) {
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
