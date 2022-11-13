package com.sagag.services.service.exporter.shoppingcart;

import lombok.Data;

@Data
public class ShoppingCartExportItemDto {
  private String headerTitle;
  private String articleNumber;
  private String articleDescription;
  private int quantity;
  private String grossPriceType;
  private String grossPrice;
  private String netPrice;
  private String total;
}
