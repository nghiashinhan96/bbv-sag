package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponMinimumOrderAmountValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponReValidateMinimumOrderAmountStep extends AbstractCouponReValidationStep {

  @Autowired
  private CouponMinimumOrderAmountValidator minimumOrderAmountValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    return handleAsDefault(data);
  }

  @Override
  protected CouponValidator getValidator() {
    return minimumOrderAmountValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate minimum order amount step";
  }

}
