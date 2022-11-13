package com.sagag.services.service.order.steps.afterorder;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.order.EshopOrderHistoryState;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.OrderAvailabilityResponse;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.order.model.AfterOrderCriteria;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;
import com.sagag.services.service.order.steps.AfterOrderHandler;
import com.sagag.services.service.order.steps.InitializingOrderHistoryStepHandler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@SbProfile
public class SbAfterOrderHandler implements AfterOrderHandler {

  @Autowired
  private InitializingOrderHistoryStepHandler initializingOrderHistoryStepHandler;

  @Override
  public void handle(UserInfo user, AbstractOrderProcessor processor,
      AfterOrderCriteria afterOrderCriteria) {
    final OrderConfirmation orderConfirmResponse = afterOrderCriteria.getAxResult();
    final ShoppingCart shoppingCart = afterOrderCriteria.getShoppingCart();

    shoppingCart.getItems().forEach(cartItem -> {
      Optional<Availability> avail = cartItem.getArticle().getAvailabilities().stream().findFirst();
      if (!avail.isPresent()) {
        return;
      }
      final Optional<OrderAvailabilityResponse> afterOrderAvail = orderConfirmResponse
          .getOrderAvailabilities().stream()
          .filter(availRes -> StringUtils.equals(
              availRes.getArticle().getArtid(), cartItem.getArticle().getArtid()))
          .findFirst();
      if (!afterOrderAvail.isPresent()) {
        return;
      }
      avail.get().setArrivalTime(afterOrderAvail.get().getAvailabilityResponse().getDeliveryTime());
    });

    //Override the order json with updated arrival time which only return after place order
    String createOrderInfoJson = initializingOrderHistoryStepHandler
        .createOrderInfoJson(shoppingCart, afterOrderCriteria.getAdditionalTextDocs(),
            afterOrderCriteria.getInvoiceTypeCode(),
            afterOrderCriteria.getExternalOrderRequest(), afterOrderCriteria.getTotalPrice(),
            user.getSettings().isPriceDisplayChanged());
    afterOrderCriteria.getOrderHistory().setOrderInfoJson(createOrderInfoJson);

    processor.updateOrderHistory(afterOrderCriteria.getOrderHistory(), orderConfirmResponse,
        EshopOrderHistoryState.ORDERED, user.getCustNrStr());
  }

}
