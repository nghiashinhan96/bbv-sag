package com.sagag.services.service.exception.coupon;

/**
 * Exception class thrown when limit used coupon code.
 */
public class LimitUsedCouponException extends CouponValidationException {

  private static final long serialVersionUID = -4236534664613502436L;

  public LimitUsedCouponException(String couponCode) {
    super(String.format("The coupon %s is used by this customer before", couponCode));
    setCouponCode(couponCode);
  }

  @Override
  public String getCode() {
    return CouponErrorCase.CE_LUS_001.code();
  }

  @Override
  public String getKey() {
    return CouponErrorCase.CE_LUS_001.key();
  }
}
