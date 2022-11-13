package com.sagag.services.service.coupon;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public interface CouponBrandFilter {

  default List<ShoppingCartItem> filter(final List<String> brands,
      List<ShoppingCartItem> shoppingCartItems) {
    if (CollectionUtils.isEmpty(brands)) {
      return shoppingCartItems;
    }
    return shoppingCartItems.stream().filter(
        shoppingCartItem -> brands.contains(shoppingCartItem.getArticleItem().getProductBrand()))
        .collect(Collectors.toList());
  }
}
