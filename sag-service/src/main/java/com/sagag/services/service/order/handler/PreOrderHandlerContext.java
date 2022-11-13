package com.sagag.services.service.order.handler;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.CouponUseLogModel;
import com.sagag.services.service.order.model.KsoMode;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class PreOrderHandlerContext {

  @Autowired
  private List<CartTypeHandler> cartTypeHandlers;

  public CouponUseLogModel handleAppliedCoupon(final UserInfo user, final ApiRequestType apiType,
      Map<?, ShoppingCart> shoppingCartByType, Boolean forceDisabledKso) {
    final KsoMode ksoMode = translateToKsoModeFromUserSetting(user, forceDisabledKso);
    CartTypeHandler cartTypeHandler = getCartTypeHandler(apiType, ksoMode);
    return cartTypeHandler.handleAppliedCoupon(user, shoppingCartByType);
  }

  public CartTypeHandler getCartTypeHandler(ApiRequestType type, KsoMode ksoMode) {
    log.debug("Api request type = {}", type);
    return cartTypeHandlers.stream()
        .filter(handler -> handler.apiRequestType() == type && handler.withKsoMode() == ksoMode)
        .findFirst().orElseThrow(() -> new NoSuchElementException("Not support this api type yet"));
  }

  public Map<?, ShoppingCart> handleShoppingBasket(final UserInfo user,
      final CreateOrderRequestBodyV2 body, final ApiRequestType apiType, final ShopType shopType,
      Boolean forceDisableKsoFromEndpoint) {
    final boolean isAvailRequest = PreOrderHandlerContext.isRequestAvailabilitiesByUserInfo(user);
    final KsoMode ksoMode = translateToKsoModeFromUserSetting(user, forceDisableKsoFromEndpoint);
    CartTypeHandler cartTypeHandler =
        getCartTypeHandler(apiType, ksoMode);

    final ShoppingCart shoppingCart =
        cartTypeHandler.getShoppingCartInContext(user, shopType, isAvailRequest);
    shoppingCart.getItems()
        .forEach(item -> item.setReference(body.getAdditionalTextDocs().get(item.getCartKey())));

    Map<?, ShoppingCart> shoppingCartByType =
        cartTypeHandler.handleShoppingCart(shoppingCart, user,
            forceDisableKsoSplitInCase(forceDisableKsoFromEndpoint, body));

    return shoppingCartByType;
  }

  public static boolean isRequestAvailabilitiesByUserInfo(UserInfo user) {
    return !user.isFinalUserRole() || user.getSettings().hasWssDeliveryProfile();
  }

  private boolean forceDisableKsoSplitInCase(Boolean forceFromEndpoint,
      CreateOrderRequestBodyV2 body) {
    return BooleanUtils.isTrue(forceFromEndpoint) || forceDisableKsoSplitFromRequestInfo(body);
  }

  private boolean forceDisableKsoSplitFromRequestInfo(CreateOrderRequestBodyV2 body) {
    return StringUtils.isNotBlank(body.getMessage());
  }

  private final KsoMode translateToKsoModeFromUserSetting(final UserInfo user, Boolean forceDisabledKso) {
    if (forceDisabledKso == null) {
      return KsoMode.NOT_EFFECT;
    }
    if (forceDisabledKso) {
      return KsoMode.OFF;
    }

    boolean ksoEnabledUserSetting = user.getSettings().isKsoEnabled();
    return KsoMode.findByFlag(BooleanUtils.toBoolean(ksoEnabledUserSetting));
  }
}
