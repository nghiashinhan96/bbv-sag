package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponDiscountAmountValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponReValidateDiscountAmountStep extends AbstractCouponReValidationStep {

  @Autowired
  private CouponDiscountAmountValidator discountAmountValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    return handleAsDefault(data);
  }

  @Override
  protected CouponValidator getValidator() {
    return discountAmountValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate discount amount step";
  }
}
