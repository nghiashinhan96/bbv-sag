package com.sagag.services.service.order.processor.ax;

import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.service.order.model.OrderRequestType;

import org.springframework.stereotype.Component;

/**
 * This class provides implementation to create offer to ax system.
 *
 */
@Component
@AxProfile
public class CreateOfferProcessor extends TransferBasketProcessor {

  @Override
  public OrderConfirmation executeSendOrder(String companyName, ExternalOrderRequest request) {
    return orderExtService.createOffer(companyName, request);
  }

  @Override
  public OrderType orderHistoryType() {
    return OrderType.ORDER;
  }

  @Override
  public OrderRequestType orderRequestType() {
    return OrderRequestType.CREATE_OFFER;
  }

  @Override
  public final ExecutionOrderType orderExecutionType() {
    return ExecutionOrderType.OFFER;
  }

}
