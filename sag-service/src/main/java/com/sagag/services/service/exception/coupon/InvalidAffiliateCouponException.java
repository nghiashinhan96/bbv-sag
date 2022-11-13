package com.sagag.services.service.exception.coupon;

/**
 * Exception class thrown when invalid coupon code for affiliate.
 */
public class InvalidAffiliateCouponException extends CouponValidationException {

  private static final long serialVersionUID = 7699341222855976629L;

  public InvalidAffiliateCouponException(String couponCode, String code, String key,
      String message) {
    super(message);
    setCouponCode(couponCode);
    setCode(code);
    setKey(key);
  }

}
