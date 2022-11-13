package com.sagag.services.service.order.processor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.service.order.model.OrderRequestType;

public class EmptyOrderProcessor extends AbstractOrderProcessor {

  @Override
  public OrderType orderHistoryType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean allowSendOrderConfirmationMail(UserInfo user) {
    throw new UnsupportedOperationException();
  }

  @Override
  public OrderConfirmation executeSendOrder(String companyName, ExternalOrderRequest request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public OrderRequestType orderRequestType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ExecutionOrderType orderExecutionType() {
    throw new UnsupportedOperationException();
  }

}
