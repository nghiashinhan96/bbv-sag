package com.sagag.services.service.order.handler;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.KsoMode;

import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class OfferCartTypeHandler extends DefaultCartTypeHander {

  private static final boolean CACHE_MODE = false;

  private static final boolean QUICK_VIEW_MODE = false;

  @Override
  public ApiRequestType apiRequestType() {
    return ApiRequestType.OFFER;
  }

  @Override
  public ShoppingCart getShoppingCartInContext(UserInfo user, ShopType shopType,
      boolean isAvailabilityReq) {
    return cartBusinessService.checkoutShopCart(user, shopType, CACHE_MODE, QUICK_VIEW_MODE);
  }

  @Override
  public KsoMode withKsoMode() {
    return KsoMode.NOT_EFFECT;
  }

  @Override
  public Map<?, ShoppingCart> handleShoppingCart(ShoppingCart shoppingCart, UserInfo user,
      boolean ksoDisabled) {
    return Stream.of(new AbstractMap.SimpleEntry<>(typeForOffer(shoppingCart, user), shoppingCart))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

}
