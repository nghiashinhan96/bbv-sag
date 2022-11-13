package com.sagag.services.service.order.handler;

import com.sagag.eshop.repo.api.order.OrderHistoryRepository;
import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.CouponUseLogModel;
import com.sagag.services.service.order.model.KsoMode;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public abstract class CartTypeHandler {

  public static final Boolean KSO_MODE_NOT_AFFECTED_BY_DEFAULT = null;

  public static final Boolean KSO_MODE_ON = true;

  public static final Boolean KSO_MODE_OFF = false;

  @Autowired
  protected CartBusinessService cartBusinessService;

  @Autowired
  protected OrderHistoryRepository orderHistoryRepo;

  @Autowired
  protected ContextService contextService;

  @Autowired
  protected CouponCacheService couponCacheService;

  public abstract ApiRequestType apiRequestType();

  public abstract KsoMode withKsoMode();

  @LogExecutionTime
  public ShoppingCart getShoppingCartInContext(UserInfo user, ShopType shopType,
      boolean isAvailabilityReq) {
    log.info("Fetching fully shopping cart in context");
    return cartBusinessService.checkoutShopCart(user, shopType, isAvailabilityReq);
  }

  public CouponUseLogModel handleAppliedCoupon(UserInfo user,
      Map<?, ShoppingCart> shoppingCart) {
    final CouponUseLog couponUseLog = couponCacheService.getCouponUseLog(user.key());
    return CouponUseLogModel.builder().couponUseLog(couponUseLog).build();
  }

  public abstract Map<?, ShoppingCart> handleShoppingCart(ShoppingCart shoppingCart,
      UserInfo user, boolean ksoDisabled);

}
