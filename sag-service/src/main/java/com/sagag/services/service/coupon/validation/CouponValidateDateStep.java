package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponDateValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.ExpiredCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponValidateDateStep extends AbstractCouponValidationStep {

  @Autowired
  private CouponDateValidator dateValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      throw new ExpiredCouponException(data.getCoupConditions().getCouponsCode());
    }
    return data;
  }

  @Override
  protected CouponDateValidator getValidator() {
    return dateValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate date step";
  }
}
