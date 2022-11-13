package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponMaximumOrderAmountValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponReValidateMaximumOrderAmountStep extends AbstractCouponReValidationStep {

  @Autowired
  private CouponMaximumOrderAmountValidator maximumOrderAmountValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    return handleAsDefault(data);
  }

  @Override
  protected CouponValidator getValidator() {
    return maximumOrderAmountValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate maximum order amount step";
  }

}
