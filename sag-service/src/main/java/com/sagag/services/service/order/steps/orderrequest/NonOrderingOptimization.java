package com.sagag.services.service.order.steps.orderrequest;

import com.sagag.services.common.profiles.LocalStockPreferringProfile;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@LocalStockPreferringProfile(true)
public class NonOrderingOptimization implements OrderingOptimizer {

  @Override
  public List<ShoppingCartItem> optimize(List<ShoppingCartItem> originalCartItems) {
    return originalCartItems;
  }

}
