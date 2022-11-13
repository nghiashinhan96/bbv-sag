package com.sagag.services.stakis.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Context ID descriptions from document TM Connect page No.7.
 *
 */
@AllArgsConstructor
@Getter
public enum TmContextId {

  ARTICLE_LIST(1),
  ARTICLE_DETAIL(2),
  ERP_INFO(4),
  SHOPPING_BASKET_1(5),
  SHOPPING_BASKET_2(6),
  DELIVERY_MODES(9),
  SHOPPING_BASKET(16),
  HITS(17);

  private int value;

}
