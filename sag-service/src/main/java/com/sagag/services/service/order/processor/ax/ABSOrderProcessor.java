package com.sagag.services.service.order.processor.ax;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.enums.AxOrderType;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * This class provides implementation to create ABS order to ax system.
 *
 */
@Component
@AxProfile
public class ABSOrderProcessor extends PlaceOrderProcessor {

  /**
   * AX Order Type of ABS order
   */
  @Override
  public String axOrderType(UserInfo user, OrderConditionContextBody orderCondition) {
    if (user.isSaleOnBehalf() || user.getSettings().isCustomerAbsEnabled()) {
      return AxOrderType.ABS.name();
    }
    return StringUtils.EMPTY;
  }

  @Override
  public OrderType orderHistoryType() {
    return OrderType.ABS;
  }

  @Override
  public boolean allowSendOrderConfirmationMail(UserInfo user) {
    return !user.isSaleOnBehalf();
  }

  @Override
  public OrderRequestType orderRequestType() {
    return OrderRequestType.ABS_ORDER;
  }
}
