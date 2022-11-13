package com.sagag.services.service.order.validator;

import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.request.order.OrderConditionContextBody;

public interface OrderRequestTypeValidator {

  /**
   * Validate the order type request
   *
   * @param orderCondition
   * @param requestType
   * @return true if valid otherwise is false
   */
  boolean validate(OrderConditionContextBody orderCondition, OrderRequestType requestType);
}
