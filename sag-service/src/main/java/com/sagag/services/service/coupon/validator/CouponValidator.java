package com.sagag.services.service.coupon.validator;

import com.sagag.services.service.coupon.CouponValidationData;

public interface CouponValidator {

  /**
   * Validates the coupon data.
   *
   * @param data
   * @return true if valid data, otherwise
   */
  boolean isInValid(CouponValidationData data);

}
