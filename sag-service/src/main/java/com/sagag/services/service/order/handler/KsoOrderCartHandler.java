package com.sagag.services.service.order.handler;

import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.erp.OrderWarningMessageCode;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.CouponUseLogModel;
import com.sagag.services.service.order.model.KsoMode;
import com.sagag.services.service.order.model.OrderRequestType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KsoOrderCartHandler extends OrderCartTypeHandler {

  @Override
  public ApiRequestType apiRequestType() {
    return ApiRequestType.ORDER;
  }

  @Override
  public KsoMode withKsoMode() {
    return KsoMode.ON;
  }

  @Override
  public CouponUseLogModel handleAppliedCoupon(UserInfo user,
      Map<?, ShoppingCart> shoppingCartByType) {
    final CouponUseLog couponUseLog = couponCacheService.getCouponUseLog(user.key());
    CouponUseLogModel couponModel = CouponUseLogModel.builder().couponUseLog(couponUseLog).build();
    if (isSplittedIntoTwoOrder(shoppingCartByType) && isAppliedCoupon(couponUseLog)) {
      couponModel.setWarningMessageCode(OrderWarningMessageCode.INVALID_COUPON);
      couponModel.setCouponUseLog(new CouponUseLog());
    }
    return couponModel;
  }

  protected boolean isAppliedCoupon(final CouponUseLog couponUseLog) {
    return StringUtils.isNotBlank(couponUseLog.getCouponsCode());
  }

  private boolean isSplittedIntoTwoOrder(Map<?, ShoppingCart> shoppingCartByType) {
    return MapUtils.isNotEmpty(shoppingCartByType) && shoppingCartByType.size() > 1;
  }

  @Override
  public Map<OrderRequestType, ShoppingCart> handleShoppingCart(ShoppingCart shoppingCart,
      UserInfo user, boolean ksoForcedDisabled) {
    ShoppingCart shoppingCartWithoutNonReferenceItem = filterOutNonReferenceItem(shoppingCart);
    Map<OrderRequestType, ShoppingCart> cartNotAffectedByKsoMode =
        super.handleShoppingCart(shoppingCart, user, ksoForcedDisabled);
    if (MapUtils.isNotEmpty(cartNotAffectedByKsoMode)) {
      return cartNotAffectedByKsoMode;
    }

    Map<OrderRequestType, ShoppingCart> result = new EnumMap<>(OrderRequestType.class);

    List<ShoppingCartItem> hasAvail = shoppingCartWithoutNonReferenceItem.getItems().stream()
        .filter(sp -> sp.isAvailable()).collect(Collectors.toList());

    List<ShoppingCartItem> hasNotAvail = shoppingCartWithoutNonReferenceItem.getItems().stream()
        .filter(sp -> !sp.isAvailable()).collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(hasAvail)) {
      ShoppingCart hasAvailShoppingCart = initFreshShoppingCart(hasAvail);
      OrderRequestType orderRequestType = typeForOrdering(hasAvailShoppingCart, user);
      result.put(orderRequestType, hasAvailShoppingCart);
    }

    if (CollectionUtils.isNotEmpty(hasNotAvail)) {
      ShoppingCart hasNotAvailShoppingCart = initFreshShoppingCart(hasNotAvail);
      OrderRequestType orderRequestType = typeForTranserBasket(hasNotAvailShoppingCart, user);
      result.put(orderRequestType, hasNotAvailShoppingCart);
    }
    return result;
  }

}
