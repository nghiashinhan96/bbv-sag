package com.sagag.services.rest.controller.shopping;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.rest.authorization.annotation.CanUsedSubShoppingCartPreAuthorization;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.api.CouponBusinessService;
import com.sagag.services.service.cart.support.ShopTypeDefault;
import com.sagag.services.service.exception.coupon.CouponValidationException;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller class to propose service APIs for coupons.
 */
@RestController
@RequestMapping("/coupons")
@Api(tags = "Coupons APIs")
@CanUsedSubShoppingCartPreAuthorization
public class CouponsController {

  @Autowired
  private CartBusinessService cartBusinessService;

  @Autowired
  private CouponBusinessService couponBusService;

  @PostMapping(value = "/addToCart", produces = MediaType.APPLICATION_JSON_VALUE)
  public ShoppingCart addCouponCodeToCart(final HttpServletRequest request,
      @RequestParam("couponCode") final String couponCode,
      @ShopTypeDefault ShopType shopType,
      final OAuth2Authentication authed) throws CouponValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final ShoppingCart cart = cartBusinessService.checkoutShopCart(user, shopType, true, false);
    return couponBusService.checkCouponsAvailable(couponCode, user, cart);
  }

  @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
  public ShoppingCart removeExistingCoupon(final HttpServletRequest request,
      final OAuth2Authentication authed,
      @ShopTypeDefault ShopType shopType) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final ShoppingCart cart = cartBusinessService.checkoutShopCart(user, shopType, true, false);
    return couponBusService.removeExistingCoupon(user, cart);
  }
}
