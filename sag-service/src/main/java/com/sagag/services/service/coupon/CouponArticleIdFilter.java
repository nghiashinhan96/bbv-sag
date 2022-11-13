package com.sagag.services.service.coupon;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public interface CouponArticleIdFilter {

  default List<ShoppingCartItem> filter(final List<String> articleIDs,
      List<ShoppingCartItem> shoppingCartItems) {
    if (CollectionUtils.isEmpty(articleIDs)) {
      return shoppingCartItems;
    }
    return shoppingCartItems.stream().filter(
        shoppingCartItem -> articleIDs.contains(shoppingCartItem.getArticleItem().getIdSagsys()))
        .collect(Collectors.toList());
  }
}
