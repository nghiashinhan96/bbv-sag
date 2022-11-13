package com.sagag.services.rest.authorization.impl;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.services.service.cart.support.ShopTypeDefaultHandlerMethodArgumentResolver;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CanUsedSubShoppingBasketAuthorizationImpl extends AbstractAuthorization {

  @Override
  public String authorizeType() {
    return "canUsedSubShoppingCart";
  }

  @Override
  protected boolean hasPermission(Authentication authed, Object targetDomainObject) {
    final HttpServletRequest request = (HttpServletRequest) targetDomainObject;
    ShopType shopType = ShopType.defaultValueOf(request.getParameter(
        ShopTypeDefaultHandlerMethodArgumentResolver.DEFAULT_SHOP_TYPE_PARAMETER));
    if (shopType.isDefaultShoppingCart()) {
      return true;
    }
    return getUserInfo(authed).hasWholesalerPermission();
  }

}
