package com.sagag.services.service.order.steps.orderrequest;

import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.order.steps.InitializeOrderRequestStepHandler;
import com.sagag.services.service.request.order.OrderContextBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Primary
public class CompositeInitializeOrderRequestStepHandlerImpl
    implements InitializeOrderRequestStepHandler {

  @Autowired(required = false)
  private List<InitializeOrderRequestStepHandler> handlers;

  @Override
  public ExternalOrderRequest handle(UserInfo user, SupportedAffiliate affiliate,
      String axOrderType, List<ShoppingCartItem> cartItems, OrderContextBuilder orderBuilder,
      ExecutionOrderType orderExecutionType) {
    return handlers.stream().filter(Objects::nonNull).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Not found any order request handler."))
        .handle(user, affiliate, axOrderType, cartItems, orderBuilder, orderExecutionType);
  }

}
