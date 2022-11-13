package com.sagag.services.service.coupon.validator;

import com.sagag.services.service.coupon.CouponValidationData;

import org.springframework.stereotype.Component;

@Component
public class CouponMinimumOrderAmountValidator implements CouponValidator {

  @Override
  public boolean isInValid(CouponValidationData input) {
    final Double minimumOrderAmount = input.getCoupConditions().getMinimumOrderAmount();
    final Double total = input.getCart().getSubTotalWithNet();
    return minimumOrderAmount != null && total < minimumOrderAmount;
  }

}
