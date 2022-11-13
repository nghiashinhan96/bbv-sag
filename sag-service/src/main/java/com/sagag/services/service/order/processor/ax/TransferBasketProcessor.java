package com.sagag.services.service.order.processor.ax;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * This class provides implementation to transfer basket to ax system.
 *
 */
@Component
@AxProfile
public class TransferBasketProcessor extends AbstractOrderProcessor {

  @Override
  public OrderConfirmation executeSendOrder(String companyName, ExternalOrderRequest request) {
    return orderExtService.createBasket(companyName, request);
  }

  @Override
  public OrderType orderHistoryType() {
    return OrderType.ORDER;
  }

  @Override
  public boolean allowSendOrderConfirmationMail(UserInfo user) {
    return !Objects.isNull(user) && !user.isSaleOnBehalf();
  }

  @Override
  public OrderRequestType orderRequestType() {
    return OrderRequestType.TRANSFER_BASKET;
  }

  @Override
  public ExecutionOrderType orderExecutionType() {
    return ExecutionOrderType.BASKET;
  }
}
