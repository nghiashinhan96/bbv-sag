package com.sagag.services.service.exception.coupon;

/**
 * Exception class thrown when invalid coupon code for customer.
 */
public class InvalidCustomerCouponException extends CouponValidationException {

  private static final long serialVersionUID = 574150991342801685L;

  public InvalidCustomerCouponException(String couponCode, String code, String key,
      String message) {
    super(message);
    setCouponCode(couponCode);
    setCode(code);
    setKey(key);
  }

}
