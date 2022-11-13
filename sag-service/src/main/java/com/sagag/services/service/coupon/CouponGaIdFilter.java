package com.sagag.services.service.coupon;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public interface CouponGaIdFilter {

  default List<ShoppingCartItem> filter(final List<String> genartIDs,
      List<ShoppingCartItem> shoppingCartItems) {
    if (CollectionUtils.isEmpty(genartIDs)) {
      return shoppingCartItems;
    }
    return shoppingCartItems.stream()
        .filter(shoppingCartItem -> genartIDs.contains(shoppingCartItem.getArticleItem().getGaId()))
        .collect(Collectors.toList());
  }
}
