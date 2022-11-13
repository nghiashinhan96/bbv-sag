package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponLimitUsedValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.LimitUsedCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponValidateLimitUsedStep extends AbstractCouponValidationStep {

  @Autowired
  private CouponLimitUsedValidator limitUsedValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      throw new LimitUsedCouponException(data.getCouponCode());
    }
    return data;
  }

  @Override
  protected CouponLimitUsedValidator getValidator() {
    return limitUsedValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate limit used step";
  }
}
