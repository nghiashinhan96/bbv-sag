package com.sagag.eshop.service.dto;

import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.domain.sag.erp.OrderConfirmation;

import lombok.Data;

import java.util.List;

/**
 * Order history info class to create.
 */
@Data
public class OrderHistoryInfo {

  private Long userId;
  private OrderConfirmation orderConfirm;
  private Long customerNr;
  private String orderInfoJson;
  private String erpOrderDetailUrl;
  private Long saleId;
  private String saleName;
  private float total;
  private OrderType type;
  private List<String> workIds;
}
