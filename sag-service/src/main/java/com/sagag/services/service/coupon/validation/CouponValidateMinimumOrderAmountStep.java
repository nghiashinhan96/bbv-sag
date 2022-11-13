package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponMinimumOrderAmountValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.MinimumTotalCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponValidateMinimumOrderAmountStep extends AbstractCouponValidationStep {

  @Autowired
  private CouponMinimumOrderAmountValidator minimumOrderAmountValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      throw new MinimumTotalCouponException(data.getCouponCode(),
          data.getCoupConditions().getMinimumOrderAmount());
    }
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return minimumOrderAmountValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate minimum order amount step";
  }
}
