package com.sagag.services.service.order.steps;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.service.order.model.AfterOrderCriteria;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;

public interface AfterOrderHandler {

  /**
   * Process business after order.
   *
   * @param user the user info
   * @param processor the processor in context
   * @param afterOrderCriteria the afterOrderCriteria
   */
  void handle(UserInfo user, AbstractOrderProcessor processor,
      AfterOrderCriteria afterOrderCriteria);

}
