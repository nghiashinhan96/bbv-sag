package com.sagag.services.service.exporter;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.Data;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReportGroupedBasketItemDto implements Serializable {

  private static final long serialVersionUID = -5153191224368433543L;

  private String title;

  private List<ReportBasketItemDto> reportBasketItems;

  public ReportGroupedBasketItemDto(final String title, final List<ShoppingCartItem> cartItems,
      NumberFormat numberFormat) {
    this.title = title;
    this.reportBasketItems = cartItems.stream()
        .map(shoppingCartItem -> new ReportBasketItemDto(shoppingCartItem, numberFormat))
        .collect(Collectors.toList());
  }
}
