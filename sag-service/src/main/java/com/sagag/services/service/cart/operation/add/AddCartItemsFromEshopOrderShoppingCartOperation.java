package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.api.OrderHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.order.OrderInfoDto;
import com.sagag.services.hazelcast.domain.order.OrderItemDetailDto;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.service.cart.operation.DefaultUpdateInfoShoppingCartOperation;
import com.sagag.services.service.order.history.OrderHistoryInformationBeautifier;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AddCartItemsFromEshopOrderShoppingCartOperation
  extends AbstractAddCartItemShoppingCartOperation<Map<String, Integer>> {

  @Autowired
  private OrderHistoryService orderHistoryService;

  @Autowired
  private LicenseService licenseService;

  @Autowired
  private DefaultAddCartItemShoppingCartOperation addCartItemShopCartOperation;

  @Autowired
  private DefaultUpdateInfoShoppingCartOperation updateInfoShoppingCartOperation;

  @Autowired
  private OrderHistoryInformationBeautifier orderHistoryInfoBeautifier;

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, Map<String, Integer> articleIdQuantityMap,
      ShopType shopType, Object... additionals) {
    final Long orderId = (Long) additionals[0];
    final String basketItemSourceId =  (String) additionals[1];
    final String basketItemSourceDesc =  (String) additionals[2];
    return addEshopOrderToCart(user, orderId, articleIdQuantityMap, shopType, basketItemSourceId, basketItemSourceDesc);
  }

  private ShoppingCart addEshopOrderToCart(final UserInfo user, final Long orderId,
      final Map<String, Integer> articleIdQuantityMap, ShopType shopType, String basketItemSourceId, String basketItemSourceDesc) {
    final long start = System.currentTimeMillis();
    final OrderHistory orderHistory = orderHistoryService.getOrderDetail(orderId)
        .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid order %d", orderId)));
    log.debug(
        "Perf:CartBusinessServiceImpl->addEshopOrderToCart-> Get db order history detail {} ms",
        System.currentTimeMillis() - start);

    Assert.hasText(orderHistory.getOrderInfoJson(),
        String.format("Invalid json data for order %d", orderId));

    final OrderInfoDto orderInfo =
        OrderInfoDto.createOrderInfoDtoFromJson(orderHistory.getOrderInfoJson());
    orderHistoryInfoBeautifier.beautify(user, orderInfo, true);
    final List<OrderItemDetailDto> orderItems = orderInfo.getItems();
    Assert.notEmpty(orderItems, String.format("Order %d should have item", orderId));

    if (!MapUtils.isEmpty(articleIdQuantityMap)) {
      orderItems.removeIf(item -> !articleIdQuantityMap.containsKey(item.getArticle().getIdSagsys()));
      orderItems.forEach(updateQuantityForOrderItem(articleIdQuantityMap));
    }

    final List<ShoppingCartRequestBody> cartItems = orderItems.stream()
        .map(item -> buildShoppingCartRequest(item, basketItemSourceId, basketItemSourceDesc)).collect(Collectors.toList());

    final boolean isAvaiReq = user.isSaleOnBehalf();

    ShoppingCart shoppingCart = addCartItemShopCartOperation.execute(user, cartItems, shopType);
    updateInfoShoppingCartOperation.execute(user, shoppingCart, shopType, StringUtils.EMPTY,
        StringUtils.EMPTY, isAvaiReq);
    couponBusService.validateCoupon(user, shoppingCart);

    return shoppingCart;
  }

  private static Consumer<OrderItemDetailDto> updateQuantityForOrderItem(
      final Map<String, Integer> articleIdQuantityMap) {
    return item -> Optional.ofNullable(articleIdQuantityMap.get(item.getArticle().getIdSagsys()))
        .ifPresent(quantity -> item.getArticle().setAmountNumber(quantity));
  }

  private ShoppingCartRequestBody buildShoppingCartRequest(OrderItemDetailDto item, String basketItemSourceId, String basketItemSourceDesc) {
    final ArticleDocDto articleDto = item.getArticle();
    final ShoppingCartRequestBody cartRequest = new ShoppingCartRequestBody();
    cartRequest.setArticle(articleDto);
    cartRequest.setVehicle(item.getVehicle());
    cartRequest.setQuantity(articleDto.getAmountNumber());
    cartRequest.setCategory(item.getCategory());
    cartRequest.setBasketItemSourceDesc(basketItemSourceDesc);
    cartRequest.setBasketItemSourceId(basketItemSourceId);
    if (item.isVin()) {
      final Optional<LicenseSettingsDto> license =
          licenseService.getLicenseSettingsByArticleId(Long.parseLong(articleDto.getArtid()));
      Assert.isTrue(license.isPresent(), "License setting is not found for vin item");
      cartRequest.setLicense(license.get());
    }
    return cartRequest;
  }

}
