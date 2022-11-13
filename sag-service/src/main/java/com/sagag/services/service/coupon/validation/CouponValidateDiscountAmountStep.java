package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponDiscountAmountValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.CouponValidationException.CouponErrorCase;
import com.sagag.services.service.exception.coupon.InvalidAffiliateCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponValidateDiscountAmountStep extends AbstractCouponValidationStep {

  @Autowired
  private CouponDiscountAmountValidator discountAmountValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      final String couponCode = data.getCouponCode();
      final String msg =
          String.format("The coupon %s has price is less than or equals zero", couponCode);
      final String code = CouponErrorCase.CE_IAF_004.code();
      final String key = CouponErrorCase.CE_IAF_004.key();
      throw new InvalidAffiliateCouponException(couponCode, code, key, msg);
    }
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return discountAmountValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate discount amount step";
  }
}
