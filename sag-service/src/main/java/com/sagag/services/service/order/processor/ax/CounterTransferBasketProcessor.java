package com.sagag.services.service.order.processor.ax;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.enums.AxOrderType;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import org.springframework.stereotype.Component;

/**
 * This class provides implementation to create counter basket to ax system.
 *
 */
@Component
@AxProfile
public class CounterTransferBasketProcessor extends TransferBasketProcessor {

  /**
   * AX Order Type of KSL order
   */
  @Override
  public String axOrderType(UserInfo user, OrderConditionContextBody orderCondition) {
    return AxOrderType.KSL.name();
  }

  @Override
  public OrderType orderHistoryType() {
    return OrderType.COUNTER;
  }

  @Override
  public boolean allowSendOrderConfirmationMail(UserInfo user) {
    return false;
  }

  @Override
  public OrderRequestType orderRequestType() {
    return OrderRequestType.COUNTER_TRANSFER_BASKET;
  }
}
