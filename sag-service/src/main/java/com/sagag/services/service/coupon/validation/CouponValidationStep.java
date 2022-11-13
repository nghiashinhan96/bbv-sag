package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.exception.coupon.CouponValidationException;

public interface CouponValidationStep {
  CouponValidationData handle(CouponValidationData data) throws CouponValidationException;
}
