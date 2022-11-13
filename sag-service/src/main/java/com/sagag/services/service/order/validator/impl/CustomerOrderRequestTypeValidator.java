package com.sagag.services.service.order.validator.impl;

import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.order.validator.OrderRequestTypeValidator;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomerOrderRequestTypeValidator implements OrderRequestTypeValidator {

  @Override
  public boolean validate(final OrderConditionContextBody orderCondition,
      final OrderRequestType requestType) {
    if (StringUtils.isBlank(orderCondition.getDeliveryTypeCode())
        || StringUtils.isBlank(orderCondition.getPaymentMethod())) {
      return false;
    }
    if (OrderRequestType.ABS_ORDER.equals(requestType)) {
      final String paymentMethod = orderCondition.getPaymentMethod();
      return StringUtils.equals(ErpSendMethodEnum.PICKUP.name(),
          orderCondition.getDeliveryTypeCode())
          && (StringUtils.equals(PaymentMethodType.CREDIT.name(), paymentMethod)
              || StringUtils.equals(PaymentMethodType.CASH.name(), paymentMethod));
    }
    return true;
  }
}
