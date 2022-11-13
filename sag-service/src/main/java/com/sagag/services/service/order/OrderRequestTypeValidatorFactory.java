package com.sagag.services.service.order;

import com.sagag.services.service.order.validator.OrderRequestTypeValidator;
import com.sagag.services.service.order.validator.impl.CustomerOrderRequestTypeValidator;
import com.sagag.services.service.order.validator.impl.SalesOrderRequestTypeValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderRequestTypeValidatorFactory {

  @Autowired
  private SalesOrderRequestTypeValidator salesOrderRequestTypeValidator;

  @Autowired
  private CustomerOrderRequestTypeValidator customerOrderRequestTypeValidator;

  public OrderRequestTypeValidator getOrderRequestTypeValidation(final boolean isSalesOnbehalf) {
    return isSalesOnbehalf ? salesOrderRequestTypeValidator : customerOrderRequestTypeValidator;
  }
}
