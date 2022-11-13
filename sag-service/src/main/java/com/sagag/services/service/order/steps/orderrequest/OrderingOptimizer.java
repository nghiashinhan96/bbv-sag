package com.sagag.services.service.order.steps.orderrequest;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import java.util.List;

public interface OrderingOptimizer {

  public List<ShoppingCartItem> optimize(List<ShoppingCartItem> originalCartItems);

}
