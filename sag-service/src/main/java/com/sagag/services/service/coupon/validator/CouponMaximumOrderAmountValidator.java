package com.sagag.services.service.coupon.validator;

import com.sagag.services.service.coupon.CouponValidationData;

import org.springframework.stereotype.Component;

@Component
public class CouponMaximumOrderAmountValidator implements CouponValidator {

  @Override
  public boolean isInValid(CouponValidationData input) {
    final Double maximumDiscount = input.getCoupConditions().getMaximumDiscount();
    final Double total = input.getCart().getSubTotalWithNet();
    return maximumDiscount != null && total > maximumDiscount;
  }
}
