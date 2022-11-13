package com.sagag.services.service.order.processor.ax;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.enums.AxOrderType;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import org.springframework.stereotype.Component;

@Component
@AxProfile
public class KSOAUTOrderProcessor extends PlaceOrderProcessor {

  @Override
  public String axOrderType(UserInfo user, OrderConditionContextBody orderCondition) {
    return AxOrderType.KSO_AUT.name();
  }

  @Override
  public OrderType orderHistoryType() {
    return OrderType.KSO_AUT;
  }

  @Override
  public OrderRequestType orderRequestType() {
    return OrderRequestType.KSO_AUT;
  }
}
