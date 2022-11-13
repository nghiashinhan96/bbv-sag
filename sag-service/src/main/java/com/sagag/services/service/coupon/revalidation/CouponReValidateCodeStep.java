package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponCodeValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponReValidateCodeStep extends AbstractCouponReValidationStep {

  @Autowired
  private CouponCodeValidator codeValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    if (getValidator().isInValid(data)) {
      return handleInvalidCaseAsDefault(data);
    }
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return codeValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate code step";
  }
}
