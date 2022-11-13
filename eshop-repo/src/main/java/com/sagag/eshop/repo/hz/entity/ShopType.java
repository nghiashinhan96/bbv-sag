package com.sagag.eshop.repo.hz.entity;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

public enum ShopType {

  DEFAULT_SHOPPING_CART, SUB_FINAL_SHOPPING_CART;

  public static ShopType defaultValueOf(String shopType) {
    return Stream.of(values()).filter(val -> StringUtils.equalsIgnoreCase(shopType, val.name()))
        .findFirst().orElse(DEFAULT_SHOPPING_CART);
  }

  public static ShopType defaultValueOf(ShopType shopType) {
    return ObjectUtils.defaultIfNull(shopType, DEFAULT_SHOPPING_CART);
  }

  public static String defaultShopTypeCartKey(ShopType shopType) {
    return ShopType.DEFAULT_SHOPPING_CART == shopType ? StringUtils.EMPTY : shopType.name();
  }

  public boolean isDefaultShoppingCart() {
    return DEFAULT_SHOPPING_CART == this;
  }

}
