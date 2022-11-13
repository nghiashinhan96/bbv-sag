package com.sagag.services.service.enums;

public enum BasketFilterEnum {

  ALL_BASKET, SALES_BASKET, CUSTOMER_BASKET, USER_BASKET;

  public boolean isAllBasket() {
    return this == ALL_BASKET;
  }

  public boolean isSalesBasket() {
    return this == SALES_BASKET;
  }

  public boolean isCustomerBasket() {
    return this == CUSTOMER_BASKET;
  }

  public boolean isUserBasket() {
    return this == USER_BASKET;
  }
}
