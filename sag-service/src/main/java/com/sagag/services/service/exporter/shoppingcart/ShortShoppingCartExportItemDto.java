package com.sagag.services.service.exporter.shoppingcart;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShortShoppingCartExportItemDto implements Serializable {

  private static final long serialVersionUID = -6175208416418779505L;

  private String articleNumber;
  private int quantity;
}
