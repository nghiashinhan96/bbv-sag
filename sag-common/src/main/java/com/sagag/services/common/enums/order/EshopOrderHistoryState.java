package com.sagag.services.common.enums.order;

public enum EshopOrderHistoryState {
  // #3340 #3360
  OPEN,
  PROCESSING,
  ORDERED,
  ERP_TIMEOUT_ERROR,
  ERP_ORDER_ERROR
}