package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponMaximumOrderAmountValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.MaximumTotalCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponValidateMaximumOrderAmountStep extends AbstractCouponValidationStep {

  @Autowired
  private CouponMaximumOrderAmountValidator maximumOrderAmountValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      throw new MaximumTotalCouponException(data.getCouponCode(),
          data.getCoupConditions().getMaximumDiscount());
    }
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return maximumOrderAmountValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate maximum order amount step";
  }
}
