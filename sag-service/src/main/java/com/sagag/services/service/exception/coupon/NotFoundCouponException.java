package com.sagag.services.service.exception.coupon;

/**
 * Exception class thrown when not found coupon code.
 */
public class NotFoundCouponException extends CouponValidationException {

  private static final long serialVersionUID = -2168263992904272068L;

  public NotFoundCouponException(String couponCode) {
    super(String.format("Not found any coupons with code = %s in system", couponCode));
    setCouponCode(couponCode);
  }

  @Override
  public String getCode() {
    return CouponErrorCase.CE_NFO_001.code();
  }

  @Override
  public String getKey() {
    return CouponErrorCase.CE_NFO_001.key();
  }

}
