package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponCustomerValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.CouponValidationException.CouponErrorCase;
import com.sagag.services.service.exception.coupon.InvalidCustomerCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponValidateCustomerStep extends AbstractCouponValidationStep {

  @Autowired
  private CouponCustomerValidator customerValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      final String couponsCode = data.getCouponCode();
      final String msg = String.format("The coupon %s is not applied for customer %s", couponsCode,
          data.getUser().getCustNrStr());
      final String code = CouponErrorCase.CE_ICU_002.code();
      final String key = CouponErrorCase.CE_ICU_002.key();
      throw new InvalidCustomerCouponException(couponsCode, code, key, msg);
    }
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return customerValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate customer step";
  }

}
