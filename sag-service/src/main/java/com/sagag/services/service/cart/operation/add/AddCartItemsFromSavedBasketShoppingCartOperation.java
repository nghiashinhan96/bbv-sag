package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.BasketHistoryService;
import com.sagag.eshop.service.dto.BasketHistoryDto;
import com.sagag.eshop.service.dto.BasketHistoryItemDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.service.cart.operation.DefaultUpdateInfoShoppingCartOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddCartItemsFromSavedBasketShoppingCartOperation
    extends AbstractAddCartItemShoppingCartOperation<Long> {

  @Autowired
  private BasketHistoryService basketHistoryService;

  @Autowired
  private DefaultAddCartItemShoppingCartOperation addCartItemShopCartOperation;

  @Autowired
  private DefaultUpdateInfoShoppingCartOperation updateInfoShoppingCartOperation;

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, Long basketId, ShopType shopType,
      Object... additionals) {

    final BasketHistoryDto basketHistory = basketHistoryService.getBasketHistoryDetails(basketId)
        .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid basket %d", basketId)));

    final List<BasketHistoryItemDto> basketItems = basketHistory.getItems();
    if (basketItems.isEmpty()) {
      throw new IllegalArgumentException("Can not add empty basket history to the shopping cart");
    }
    final List<ShoppingCartRequestBody> shoppingCartItems = new ArrayList<>();
    basketItems.forEach(item -> shoppingCartItems.addAll(
            item.getArticles().stream().map(cartRequestConverter(item))
            .collect(Collectors.toList())));
    ShoppingCart shoppingCart =
        addCartItemShopCartOperation.execute(user, shoppingCartItems, shopType);
    updateInfoShoppingCartOperation.execute(user, shoppingCart, shopType, StringUtils.EMPTY,
        StringUtils.EMPTY, NO_UPDATE_AVAIL_REQUEST);
    couponBusService.validateCoupon(user, shoppingCart);

    return shoppingCart;
  }

}
