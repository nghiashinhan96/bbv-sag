package com.sagag.services.service.order.processor.ax;

import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.service.order.processor.AbstractPlaceOrderProcessor;

import org.springframework.stereotype.Component;

@Component
@AxProfile
public class PlaceOrderProcessor extends AbstractPlaceOrderProcessor {

  @Override
  public OrderConfirmation executeSendOrder(String companyName, ExternalOrderRequest request) {
    return orderExtService.createOrder(companyName, request);
  }
}
