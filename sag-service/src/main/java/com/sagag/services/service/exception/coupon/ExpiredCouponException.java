package com.sagag.services.service.exception.coupon;

/**
 * Exception class thrown when expired coupon code.
 */
public class ExpiredCouponException extends CouponValidationException {

  private static final long serialVersionUID = 1358908691138868810L;

  public ExpiredCouponException(String couponCode) {
    super(String.format("The coupon %s is expired", couponCode));
    setCouponCode(couponCode);
  }

  @Override
  public String getCode() {
    return CouponErrorCase.CE_EXP_001.code();
  }

  @Override
  public String getKey() {
    return CouponErrorCase.CE_EXP_001.key();
  }

}
