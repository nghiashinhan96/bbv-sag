package com.sagag.services.service.exporter;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.user.EshopContext;

import lombok.Data;

/**
 * Criteria class for export shopping basket content.
 */
@Data
public class BasketExportCriteria {

  private final UserInfo user;

  private final ShoppingCart shoppingCart;

  private final EshopContext eshopContext;

  private final Boolean curentStateNetPriceView;

  public static BasketExportCriteria of(final UserInfo user, final ShoppingCart request,
      final EshopContext eshopContext, final Boolean curentStateNetPriceView) {
    return new BasketExportCriteria(user, request, eshopContext, curentStateNetPriceView);
  }
}
