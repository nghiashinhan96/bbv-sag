package com.sagag.services.rest.controller.shopping;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.rest.authorization.annotation.CanUsedSubShoppingCartPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.cart.support.ShopTypeDefault;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/cart/v2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags = "Sub Shopping Cart APIs Version 2.0")
@CanUsedSubShoppingCartPreAuthorization
public class CartControllerV2 {

  @Autowired
  private CartBusinessService cartBusinessService;

  /**
   * Views the {@link ShoppingCart}.
   *
   * @param authed the authenticated user
   * @return the updated {@link ShoppingCart} instance.
   */
  @ApiOperation(value = ApiDesc.Cart.VIEW_ARTICLE_TO_SHOPPING_CART_API_DESC,
      notes = ApiDesc.Cart.VIEW_ARTICLE_TO_SHOPPING_CART_NOTE)
  @GetMapping("/checkout")
  public ShoppingCart checkoutShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @RequestParam(value = "cache") boolean cacheMode,
      @ShopTypeDefault ShopType shopType,
      @RequestParam(value = "quickView") boolean quickViewMode) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusinessService.checkoutShopCart(user, shopType, cacheMode, quickViewMode);
  }

  /**
   * Adds the final customer order id to shopping cart.
   *
   * @param request
   * @param authed
   * @param finalCustomerOrderId
   * @return the updated {@link ShoppingCart}.
   */
  @ApiOperation(value = ApiDesc.Cart.ADD_FINAL_CUSTOMER_ORDER_ID_TO_SHOPPING_CART_API_DESC)
  @PostMapping(value = "/add/{finalCustomerOrderId}/final-customer",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart addFinalCustomerOrderToShoppingCart(final HttpServletRequest request,
      final OAuth2Authentication authed, @PathVariable final Long finalCustomerOrderId,
      @ShopTypeDefault ShopType shopType,
      @RequestParam(required = false) String basketItemSourceId,
      @RequestParam(required = false) String basketItemSourceDesc) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusinessService.addFinalCustomerOrderToShoppingCart(user, finalCustomerOrderId,
        shopType, basketItemSourceId, basketItemSourceDesc);
  }
}
