package com.sagag.services.service.order.handler;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.KsoMode;
import com.sagag.services.service.order.model.OrderRequestType;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NonKsoOrderCartHandler extends OrderCartTypeHandler {

  @Override
  public ApiRequestType apiRequestType() {
    return ApiRequestType.ORDER;
  }

  @Override
  public KsoMode withKsoMode() {
    return KsoMode.OFF;
  }

  @Override
  public Map<OrderRequestType, ShoppingCart> handleShoppingCart(ShoppingCart shoppingCart,
      UserInfo user, boolean ksoForcedDisabled) {
    ShoppingCart shoppingCartWithoutNonReferenceItem = filterOutNonReferenceItem(shoppingCart);
    Map<OrderRequestType, ShoppingCart> cartNotAffectedByKsoMode = super.handleShoppingCart(
        shoppingCart, user, ksoForcedDisabled);

    if (MapUtils.isNotEmpty(cartNotAffectedByKsoMode)) {
      return cartNotAffectedByKsoMode;
    }
    if (user.isSaleOnBehalf()
        || isShoppingCartHasAllAvailabilityItems(shoppingCartWithoutNonReferenceItem)) {
      OrderRequestType type = typeForOrdering(shoppingCartWithoutNonReferenceItem, user);
      return buildShoppingCartByType(type, shoppingCart);
    }

    OrderRequestType type = typeForTranserBasket(shoppingCartWithoutNonReferenceItem, user);
    return buildShoppingCartByType(type, shoppingCart);
  }
}
