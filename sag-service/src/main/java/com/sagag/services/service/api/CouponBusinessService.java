package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.exception.coupon.CouponValidationException;

/**
 * Interface to define services on Coupon.
 */
public interface CouponBusinessService {

  /**
   * Checks if the coupon is available.
   *
   * @param couponsCode the coupon code
   * @param user the user who requests
   * @param cart current shopping cart
   *
   * @return the shopping cart
   */
  ShoppingCart checkCouponsAvailable(String couponsCode, UserInfo user, ShoppingCart cart)
      throws CouponValidationException;

  /**
   * Validates the coupon code within the shopping basket.
   *
   * @param user the current user who checkouts the shopping basket.
   * @param shoppingCart the shopping basket.
   */
  void validateCoupon(UserInfo user, ShoppingCart shoppingCart);

  /**
   * Removes coupon from shopping cart.
   *
   * @param user the user who requests
   * @param cart current shopping cart
   *
   * @return the shopping cart
   */
  ShoppingCart removeExistingCoupon(UserInfo user, ShoppingCart cart);

}
