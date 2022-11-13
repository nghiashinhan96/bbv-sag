package com.sagag.services.tools.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OfferTables {
  SHOP_ARTICLE("SHOP_ARTICLE"), OFFER_PERSON("OFFER_PERSON"), OFFER("OFFER"),
  OFFER_POSITION("OFFER_POSITION"), OFFER_ADDRESS("OFFER_ADDRESS");

  private String tableName;
}
