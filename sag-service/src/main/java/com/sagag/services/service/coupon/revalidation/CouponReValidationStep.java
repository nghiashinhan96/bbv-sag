package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.service.coupon.CouponValidationData;

public interface CouponReValidationStep {

  boolean shouldHandle(CouponValidationData data);

  CouponValidationData handle(CouponValidationData data);
}
