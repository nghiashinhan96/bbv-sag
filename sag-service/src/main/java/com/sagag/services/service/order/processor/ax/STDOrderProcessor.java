package com.sagag.services.service.order.processor.ax;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.enums.AxOrderType;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import org.springframework.stereotype.Component;

/**
 * This class provides implementation to create STD order to ax system.
 *
 */
@Component
@AxProfile
public class STDOrderProcessor extends PlaceOrderProcessor {

  /**
   * AX Order Type of STD order
   */
  @Override
  public String axOrderType(UserInfo user, OrderConditionContextBody orderCondition) {
    return AxOrderType.STD.name();
  }

  @Override
  public OrderType orderHistoryType() {
    return OrderType.STD;
  }

  @Override
  public boolean allowSendOrderConfirmationMail(UserInfo user) {
    return !user.isSaleOnBehalf();
  }

  @Override
  public OrderRequestType orderRequestType() {
    return OrderRequestType.STD_ORDER;
  }

}
