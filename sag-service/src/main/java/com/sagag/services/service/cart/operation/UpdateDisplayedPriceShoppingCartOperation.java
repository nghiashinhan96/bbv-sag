package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.request.UpdateDisplayedPriceRequestItem;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class UpdateDisplayedPriceShoppingCartOperation
    extends AbstractShoppingCartOperation<List<UpdateDisplayedPriceRequestItem>, ShoppingCart> {

  @Autowired
  private DefaultUpdateInfoShoppingCartOperation updateInfoShoppingCartOperation;

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, List<UpdateDisplayedPriceRequestItem> items,
      ShopType shopType, Object... additionals) {
    Asserts.check(CollectionUtils.isNotEmpty(items), "Body request items is empty!");
    final ShoppingCart shoppingCart = checkoutCart(user, shopType);
    log.debug("Updated items {}", items);
    Map<String, DisplayedPriceDto> displayedPriceMap = items.stream().collect(HashMap::new,
        (m, v) -> m.put(v.getCartKey(), v.getDisplayedPrice()), HashMap::putAll);

    shoppingCart.getItems().forEach(item -> {
      String cartKey = item.getCartKey();
      if (Objects.nonNull(item.getArticleItem()) && displayedPriceMap.containsKey(cartKey)) {
        item.getArticleItem().setDisplayedPrice(displayedPriceMap.get(cartKey));
      }
    });

    String vehileId = getVehIdForSingleOrMultipleUpdate(items);
    String idSagSys = getSagsysIdForSingleOrMultipleUpdate(items);
    updateInfoShoppingCartOperation.execute(user, shoppingCart, shopType, vehileId, idSagSys, true);
    couponBusService.validateCoupon(user, shoppingCart);

    return shoppingCart;
  }

  private static String getVehIdForSingleOrMultipleUpdate(
      List<UpdateDisplayedPriceRequestItem> items) {
    if (items.size() == 1) {
      return getCartItemParts(items.get(0).getCartKey())[2];
    }
    return StringUtils.EMPTY;
  }

  private static String getSagsysIdForSingleOrMultipleUpdate(
      List<UpdateDisplayedPriceRequestItem> items) {
    if (items.size() == 1) {
      return getCartItemParts(items.get(0).getCartKey())[3];
    }
    return StringUtils.EMPTY;
  }

  private static String[] getCartItemParts(String cartKey) {
    return cartKey.split(SagConstants.UNDERSCORE);
  }

}
