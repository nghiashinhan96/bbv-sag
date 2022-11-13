package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponCodeValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.NotFoundCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponValidateCodeStep extends AbstractCouponValidationStep {

  @Autowired
  private CouponCodeValidator codeValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      throw new NotFoundCouponException(data.getCouponCode());
    }
    return data;
  }

  @Override
  protected CouponCodeValidator getValidator() {
    return codeValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate code step";
  }
}
