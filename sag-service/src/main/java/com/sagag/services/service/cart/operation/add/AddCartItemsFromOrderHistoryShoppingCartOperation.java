package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.sag.erp.ExternalOrderPosition;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.order.history.OrderHistoryHandler;
import com.sagag.services.service.utils.order.OrdersUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AddCartItemsFromOrderHistoryShoppingCartOperation
    extends AbstractAddCartItemShoppingCartOperation<Long> {

  @Autowired
  private OrderHistoryHandler orderHistoryHandler;

  @Autowired
  private AddArticlesByQuantityMapShoppingCartOperation addArticlesByQuantityMapShopCartOperation;

  @Autowired
  private AddCartItemsFromEshopOrderShoppingCartOperation addCartItemsFromEshopOrderShopCartOperation;

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, Long orderId, ShopType shopType, Object... additionals) {
    final String orderNumber = (String) additionals[0];
    final String basketItemSourceId =  (String) additionals[1];
    final String basketItemSourceDesc =  (String) additionals[2];
    ShoppingCart shoppingCart;
    if (!Objects.isNull(orderId)) {
      shoppingCart = addCartItemsFromEshopOrderShopCartOperation.execute(user,
          Collections.emptyMap(), shopType, orderId, basketItemSourceId, basketItemSourceDesc);
    } else if (!StringUtils.isBlank(orderNumber)) {
      shoppingCart = addExternalOrderToCart(user, orderNumber, shopType, basketItemSourceId, basketItemSourceDesc);
    } else {
      throw new IllegalArgumentException("orderId or orderNumber is required!");
    }
    return shoppingCart;
  }

  private ShoppingCart addExternalOrderToCart(final UserInfo user, final String orderNr,
      ShopType shopType, String basketItemSourceId, String basketItemSourceDesc) {
    if (StringUtils.isBlank(orderNr)) {
      throw new IllegalArgumentException("orderNr is required!");
    }
    long start = System.currentTimeMillis();
    final String companyName = user.getCompanyName();
    final String customerNr = user.getCustNrStr();
    final Optional<ExternalOrderPositions> orderPosition =
        orderHistoryHandler.handlelExternalOrderPosistionHistory(orderNr, companyName, customerNr);

    log.debug(
        "Perf:CartBusinessServiceImpl->addExternalOrderToCart-> Get external order positions {} ms",
        System.currentTimeMillis() - start);

    if (!orderPosition.isPresent() || Objects.isNull(orderPosition.get().getPositions())) {
      throw new IllegalArgumentException(
          String.format("External order position is not found for order %s", orderNr));
    }
    final Map<String, Integer> articleIdQuantityMap = orderPosition.get().getPositions().stream()
        .collect(Collectors.toMap(ExternalOrderPosition::getArticleId,
            ext -> OrdersUtils.getArticleAbsQuantity(ext.getQuantity()),
            (articleId1, articleId2) -> articleId1));

    return addArticlesByQuantityMapShopCartOperation.execute(user, articleIdQuantityMap,
        shopType, basketItemSourceId, basketItemSourceDesc);
  }

}
