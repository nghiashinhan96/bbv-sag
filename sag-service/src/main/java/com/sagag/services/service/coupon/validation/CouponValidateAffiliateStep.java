package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponAffiliateValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.CouponValidationException.CouponErrorCase;
import com.sagag.services.service.exception.coupon.InvalidCustomerCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponValidateAffiliateStep extends AbstractCouponValidationStep {

  @Autowired
  private CouponAffiliateValidator affiliateValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      final String couponsCode = data.getCouponCode();
      final String msg =
          String.format("The coupon %s is not applied for any affiliate", couponsCode);
      final String code = CouponErrorCase.CE_ICU_001.code();
      final String key = CouponErrorCase.CE_ICU_001.key();
      throw new InvalidCustomerCouponException(couponsCode, code, key, msg);
    }
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return affiliateValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate affiliate step";
  }
}
