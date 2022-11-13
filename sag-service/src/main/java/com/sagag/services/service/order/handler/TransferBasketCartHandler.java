package com.sagag.services.service.order.handler;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.KsoMode;
import com.sagag.services.service.order.model.OrderRequestType;

import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TransferBasketCartHandler extends DefaultCartTypeHander {

  @Override
  public ApiRequestType apiRequestType() {
    return ApiRequestType.TRANSFER_BASKET;
  }

  @Override
  public Map<OrderRequestType, ShoppingCart> handleShoppingCart(ShoppingCart shoppingCart,
      UserInfo user, boolean isAvailabilityReq) {
    return Stream
        .of(new AbstractMap.SimpleEntry<>(typeForTranserBasket(shoppingCart, user), shoppingCart))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public KsoMode withKsoMode() {
    return KsoMode.NOT_EFFECT;
  }

}
