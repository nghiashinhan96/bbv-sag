package com.sagag.services.service.exception.coupon;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception class thrown when maximum total coupon code.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaximumTotalCouponException extends CouponValidationException {

  private static final long serialVersionUID = -1241353552619744432L;

  private static final String MAX_ORDER_AMOUNT = "MAX_ORDER_AMOUNT";

  private Double maximumOrderAmount;

  public MaximumTotalCouponException(String couponCode, Double maximumOrderAmount) {
    super(String.format("The coupon %s has total quantiy is greater than maximum amount",
        couponCode));
    setCouponCode(couponCode);
    setMaximumOrderAmount(maximumOrderAmount);
    final Map<String, Object> moreInfos = new HashMap<>();
    moreInfos.put(MAX_ORDER_AMOUNT, maximumOrderAmount);
    setMoreInfos(moreInfos);
  }

  @Override
  public String getCode() {
    return CouponErrorCase.CE_MAT_001.code();
  }

  @Override
  public String getKey() {
    return CouponErrorCase.CE_MAT_001.key();
  }
}
