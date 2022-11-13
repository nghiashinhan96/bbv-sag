package com.sagag.services.service.order;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.exception.LocationValidationException;
import com.sagag.services.service.order.handler.CartTypeHandler;
import com.sagag.services.service.order.handler.OrderHandler;
import com.sagag.services.service.order.handler.PreOrderHandlerContext;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.CouponUseLogModel;
import com.sagag.services.service.order.model.KsoMode;
import com.sagag.services.service.order.model.LocationTypeFilter;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderConditionContextBody;
import com.sagag.services.service.request.order.OrderContextBuilder;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
@SbProfile
public class SbOrderHandlerImpl implements OrderHandler {

  private static final OrderRequestType DEFAULT_ORDER_TYPE_SB = OrderRequestType.STD_ORDER;

  @Autowired
  private OrderHandlerContextV2 orderHandlerContext;

  @Autowired
  private PreOrderHandlerContext preOrderHandlerContext;

  @Override
  public List<OrderConfirmation> handleOrder(UserInfo user, CreateOrderRequestBodyV2 body,
      ApiRequestType apiType, ShopType shopType, Boolean ksoForcedDisabled)
      throws ValidationException {
    Assert.notEmpty(body.getOrderConditionByLocation(),
        "Order condition by location can not be empty");
    final boolean isAvailRequest = PreOrderHandlerContext.isRequestAvailabilitiesByUserInfo(user);

    CartTypeHandler cartTypeHandler = preOrderHandlerContext.getCartTypeHandler(apiType,
        KsoMode.NOT_EFFECT);

    final ShoppingCart shoppingCart =
        cartTypeHandler.getShoppingCartInContext(user, shopType, isAvailRequest);
    shoppingCart.getItems()
        .forEach(item -> item.setReference(body.getAdditionalTextDocs().get(item.getCartKey())));
    Map<?, ShoppingCart> handleShoppingCartByLocation =
        cartTypeHandler.handleShoppingCart(shoppingCart, user, false);
    CouponUseLogModel appliedCouponInfo =
        cartTypeHandler.handleAppliedCoupon(user, handleShoppingCartByLocation);
    List<OrderConfirmation> orderResults = new ArrayList<OrderConfirmation>();
    if (MapUtils.isNotEmpty(handleShoppingCartByLocation)) {
      for (Entry<?, ShoppingCart> sp : handleShoppingCartByLocation.entrySet()) {
        ShoppingCart singleShoppingCart = sp.getValue();
        LocationTypeFilter locationkeyMap = (LocationTypeFilter) sp.getKey();
        OrderConditionContextBody orderConditionByLocation = body.getOrderConditionByLocation()
            .stream()
            .filter(condition -> condition.getLocation().getBranchId()
                .equals(locationkeyMap.getLocation().getBranchId()))
            .findFirst().orElseThrow(
                () -> new LocationValidationException(locationkeyMap.getLocation().getBranchId()));
        OrderConfirmation singleOrderResponse = orderHandlerContext.handleSingleOrderProcess(user,
            OrderContextBuilder.builder().body(body).orderRequestType(DEFAULT_ORDER_TYPE_SB)
                .location(orderConditionByLocation.getLocation()).coupon(appliedCouponInfo)
                .selectedOrderCondition(orderConditionByLocation).shopType(shopType).build(),
            singleShoppingCart);
        orderResults.add(singleOrderResponse);
      }
    }
    return orderResults;
  }

}
