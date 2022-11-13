package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponLimitUsedValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponReValidateLimitUsedStep extends AbstractCouponReValidationStep {

  @Autowired
  private CouponLimitUsedValidator limitUsedValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    return handleAsDefault(data);
  }

  @Override
  protected CouponValidator getValidator() {
    return limitUsedValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate limit used step";
  }
}
