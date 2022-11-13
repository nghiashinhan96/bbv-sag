package com.sagag.services.service.order;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.order.handler.OrderHandler;
import com.sagag.services.service.order.handler.PreOrderHandlerContext;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.model.CouponUseLogModel;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderContextBuilder;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultOrderHandlerImpl implements OrderHandler {

  @Autowired
  private OrderHandlerContextV2 orderHandlerContext;

  @Autowired
  private PreOrderHandlerContext preOrderHandlerContext;

  @Override
  public List<OrderConfirmation> handleOrder(final UserInfo user,
      final CreateOrderRequestBodyV2 body, final ApiRequestType apiType, final ShopType shopType,
      Boolean ksoForcedDisabled) throws ValidationException {
    Map<?, ShoppingCart> shoppingCartByType = preOrderHandlerContext.handleShoppingBasket(user,
        body, apiType, shopType, ksoForcedDisabled);
    CouponUseLogModel appliedCouponInfo =
        preOrderHandlerContext.handleAppliedCoupon(user, apiType, shoppingCartByType, ksoForcedDisabled);
    List<OrderConfirmation> orderResults = new ArrayList<>();
    if (MapUtils.isNotEmpty(shoppingCartByType)) {
      for (Entry<?, ShoppingCart> sp : shoppingCartByType.entrySet()) {
        ShoppingCart singleShoppingCart = sp.getValue();
        OrderRequestType orderRequestType = (OrderRequestType) sp.getKey();
        OrderConfirmation singleOrderResponse = orderHandlerContext.handleSingleOrderProcess(user,
            OrderContextBuilder.builder().body(body).orderRequestType(orderRequestType)
                .coupon(appliedCouponInfo).selectedOrderCondition(body.getOrderCondition())
                .shopType(shopType).build(),
            singleShoppingCart);
        orderResults.add(singleOrderResponse);
      }
    }
    return orderResults;
  }
}
