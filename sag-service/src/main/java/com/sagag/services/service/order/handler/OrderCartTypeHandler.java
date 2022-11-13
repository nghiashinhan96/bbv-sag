package com.sagag.services.service.order.handler;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.OrderRequestType;

import java.util.EnumMap;
import java.util.Map;

public abstract class OrderCartTypeHandler extends DefaultCartTypeHander {

  @Override
  public ApiRequestType apiRequestType() {
    return ApiRequestType.ORDER;
  }

  @Override
  public Map<OrderRequestType, ShoppingCart> handleShoppingCart(ShoppingCart shoppingCart,
      UserInfo user, boolean ksoForcedDisabled) {
    ShoppingCart shoppingCartWithoutNonReferenceItem = filterOutNonReferenceItem(shoppingCart);
    if (user.isFinalUserRole()) {
      return buildShoppingCartByType(OrderRequestType.PLACE_ORDER, shoppingCart);
    }

    if (ksoForcedDisabled) {
      if (user.isSaleOnBehalf() || isShoppingCartHasAllAvailabilityItems(shoppingCartWithoutNonReferenceItem)) {
        OrderRequestType type = typeForOrdering(shoppingCartWithoutNonReferenceItem, user);
        return buildShoppingCartByType(type, shoppingCart);
      }
      OrderRequestType type = typeForTranserBasket(shoppingCartWithoutNonReferenceItem, user);
      return buildShoppingCartByType(type, shoppingCart);
    }

    return new EnumMap<>(OrderRequestType.class);
  }

  protected static Map<OrderRequestType, ShoppingCart> buildShoppingCartByType(
      OrderRequestType type, ShoppingCart shoppingCart) {
    Map<OrderRequestType, ShoppingCart> result = new EnumMap<>(OrderRequestType.class);
    result.put(type, shoppingCart);
    return result;
  }

}
