package com.sagag.services.service.exception.coupon;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception class thrown when minimum total coupon code.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MinimumTotalCouponException extends CouponValidationException {

  private static final long serialVersionUID = -3709686512302151941L;

  private static final String MIN_ORDER_AMOUNT = "MIN_ORDER_AMOUNT";

  private Double minimumOrderAmount;

  public MinimumTotalCouponException(String couponCode, Double minimumOrderAmount) {
    super(String.format("The coupon %s has total quantity is less than minimum amount is %s ",
        couponCode, minimumOrderAmount));
    setCouponCode(couponCode);
    setMinimumOrderAmount(minimumOrderAmount);
    final Map<String, Object> moreInfos = new HashMap<>();
    moreInfos.put(MIN_ORDER_AMOUNT, minimumOrderAmount);
    setMoreInfos(moreInfos);
  }

  @Override
  public String getCode() {
    return CouponErrorCase.CE_MIT_001.code();
  }

  @Override
  public String getKey() {
    return CouponErrorCase.CE_MIT_001.key();
  }
}
