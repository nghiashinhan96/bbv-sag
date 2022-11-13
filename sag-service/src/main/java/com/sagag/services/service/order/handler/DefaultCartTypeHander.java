package com.sagag.services.service.order.handler;

import com.sagag.eshop.repo.api.order.OrderHistoryRepository;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.order.model.OrderRequestType;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DefaultCartTypeHander extends CartTypeHandler {

  @Autowired
  protected CartBusinessService cartBusinessService;

  @Autowired
  protected OrderHistoryRepository orderHistoryRepo;

  @Autowired
  protected ContextService contextService;

  @Autowired
  protected CouponCacheService couponCacheService;

  protected final OrderRequestType typeForOrdering(ShoppingCart shoppingCart, UserInfo user) {
    EshopBasketContext basketContext = contextService.getBasketContext(user.key());
    if (user.isSaleOnBehalf() || isShoppingCartHasAllAvailabilityItems(shoppingCart)) {
      if (hasVen(shoppingCart)) {
        return OrderRequestType.KSO_AUT;
      }
      if (isCounterTransferBasketCase(basketContext)) {
        return OrderRequestType.COUNTER_TRANSFER_BASKET;
      }
      if (isSaleAbsOrderCase(user, basketContext) || isCustomerAbsOrderCase(user, basketContext)) {
        return OrderRequestType.ABS_ORDER;
      }
      if (isStdOrderCase(basketContext)) {
        return OrderRequestType.STD_ORDER;
      }
    }
    return OrderRequestType.PLACE_ORDER;
  }

  protected final OrderRequestType typeForTranserBasket(ShoppingCart shoppingCart, UserInfo user) {
    if (hasVen(shoppingCart)) {
      return OrderRequestType.KSO_AUT_TRANSFER_BASKET;
    }
    return OrderRequestType.TRANSFER_BASKET;
  }

  protected final OrderRequestType typeForOffer(ShoppingCart shoppingCart, UserInfo user) {
    if (hasVen(shoppingCart)) {
      return OrderRequestType.KSO_AUT_CREATE_OFFER;
    }
    return OrderRequestType.CREATE_OFFER;
  }

  protected boolean isShoppingCartHasAllAvailabilityItems(ShoppingCart shoppingCart) {
    return shoppingCart.getItems().stream().allMatch(ShoppingCartItem::isAvailable);
  }

  protected boolean hasVen(ShoppingCart shoppingCart) {
    return shoppingCart.getItems().stream().anyMatch(ShoppingCartItem::hasVenAvailability);
  }

  private boolean isStdOrderCase(EshopBasketContext basketContext) {
    return SendMethodType.TOUR.name().equalsIgnoreCase(basketContext.getDeliveryTypeDescCode());
  }

  private boolean isCustomerAbsOrderCase(UserInfo user, EshopBasketContext basketContext) {
    return user.getSettings().isCustomerAbsEnabled()
        && SendMethodType.PICKUP.name().equalsIgnoreCase(basketContext.getDeliveryTypeDescCode())
        && (PaymentMethodType.CASH.name().equalsIgnoreCase(basketContext.getPaymentMethodDescCode())
            || PaymentMethodType.CREDIT.name()
                .equalsIgnoreCase(basketContext.getPaymentMethodDescCode()));
  }

  private boolean isSaleAbsOrderCase(UserInfo user, EshopBasketContext basketContext) {
    return user.getSettings().isSalesAbsEnabled()
        && SendMethodType.PICKUP.name().equalsIgnoreCase(basketContext.getDeliveryTypeDescCode());
  }

  private boolean isCounterTransferBasketCase(EshopBasketContext basketContext) {
    return BooleanUtils.isTrue(basketContext.getShowKSLMode())
        && basketContext.getOrderType().equals(OrderType.COUNTER);
  }

  public ShoppingCart filterOutNonReferenceItem(ShoppingCart shoppingCart) {
    ShoppingCart filterOutNonReferenceCart = initFreshShoppingCart(shoppingCart.getItems());
    List<ShoppingCartItem> collect = filterOutNonReferenceCart.getItems().stream()
        .filter(sp -> !sp.isNonReference()).collect(Collectors.toList());
    filterOutNonReferenceCart.setItems(collect);
    return filterOutNonReferenceCart;
  }

  protected ShoppingCart initFreshShoppingCart(List<ShoppingCartItem> shoppingCartItems) {
    return new ShoppingCart(shoppingCartItems);
  }

}
