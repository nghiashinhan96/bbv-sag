package com.sagag.services.service.exception.coupon;

/**
 * Exception class thrown when full usage coupon code.
 */
public class FullUsageCouponException extends CouponValidationException {

  private static final long serialVersionUID = 4791580894987975808L;

  public FullUsageCouponException(String couponCode) {
    super(String.format("The coupon %s is full usage", couponCode));
    setCouponCode(couponCode);
  }

  @Override
  public String getCode() {
    return CouponErrorCase.CE_FUS_001.code();
  }

  @Override
  public String getKey() {
    return CouponErrorCase.CE_FUS_001.key();
  }

}
