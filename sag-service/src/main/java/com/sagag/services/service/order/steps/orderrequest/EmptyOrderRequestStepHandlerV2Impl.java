package com.sagag.services.service.order.steps.orderrequest;

import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.request.order.OrderContextBuilder;

import java.util.List;

public class EmptyOrderRequestStepHandlerV2Impl extends AbstractOrderRequestStepHandlerV2Impl {

  @Override
  public ExternalOrderRequest handle(UserInfo user, SupportedAffiliate affiliate,
      String axOrderType, List<ShoppingCartItem> cartItems, OrderContextBuilder orderBuilder,
      ExecutionOrderType orderExecutionType) {
    return new ExternalOrderRequest();
  }
}
