package com.sagag.services.service.order.steps.afterorder;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.service.order.model.AfterOrderCriteria;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;
import com.sagag.services.service.order.steps.AfterOrderHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class CompositeAfterOrderHandler implements AfterOrderHandler {

  @Autowired
  private EshopUserAfterOrderHandler eshopUserAfterOrderHandler;

  @Autowired
  private FinalUserAfterOrderHandler finalUserAfterOrderHandler;

  @Autowired(required = false)
  private SbAfterOrderHandler sbAfterOrderHelper;

  @Override
  public void handle(UserInfo user, AbstractOrderProcessor processor,
      AfterOrderCriteria afterOrderCriteria) {
    AfterOrderHandler handler = eshopUserAfterOrderHandler;
    if (user.isFinalUserRole()) {
      handler = finalUserAfterOrderHandler;
    }
    handler.handle(user, processor, afterOrderCriteria);

    if (sbAfterOrderHelper != null) {
      sbAfterOrderHelper.handle(user, processor, afterOrderCriteria);
    }
  }

}
