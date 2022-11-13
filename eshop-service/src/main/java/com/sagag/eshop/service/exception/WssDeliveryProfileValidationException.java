package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Generic exception class for WSS delivery profile validation.
 */
@Getter
public class WssDeliveryProfileValidationException extends ValidationException {

  private static final long serialVersionUID = -1555772314504211351L;

  public WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * WSS Delivery profile cases enumeration definition.
   */
  public enum WssDeliveryProfileErrorCase implements IBusinessCode {
    ODE_EMP_001("IMPORT_DATA_EMPTY"),
    ODE_EMP_002("DUPLICATE_DELIVERY_PROFILE_NAME"),
    ODE_EMP_003("CAN_NOT_FIND_EXISTED_DELIVERY_PROFILE"),
    ODE_EMP_004("VALIDATION_ERROR"),
    ODE_EMP_006("MINIMUM_TOUR_LIST_REQUIRED"),
    ODE_EMP_007("DUPLICATE_DELIVERY_PROFILE_NAME"),
    ODE_EMP_008("NON_EXISTED_DELIVERY_PROFILE_ID"),
    ODE_EMP_009("NON_EXISTED_DELIVERY_PROFILE_TOUR_ID"),
    ODE_EMP_010("NON_EXISTED_DELIVERY_PROFILE_BRANCH_ID"),
    ODE_DPU_001("WSS_DELIVERY_PROFILE_IN_USED"),
    ODE_EMP_005("DUPLICATE_ASSIGNMENT_TOUR");

    private String key;

    WssDeliveryProfileErrorCase(String key) {
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
