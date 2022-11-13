package com.sagag.services.service.order.processor;

import com.sagag.services.service.order.model.OrderRequestType;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class OrderProcessorFactory {

  @Autowired
  private List<AbstractOrderProcessor> orderProcessors;

  public AbstractOrderProcessor getOrderingProcessor(OrderRequestType type) {
    log.debug("The order request type = {}", type);
    if (CollectionUtils.size(orderProcessors) == 1) {
      return orderProcessors.get(0);
    }
    return orderProcessors.stream().filter(processor -> processor.orderRequestType() == type)
        .findFirst().orElseThrow(() -> new NoSuchElementException("Not support this type yet"));
  }

}
