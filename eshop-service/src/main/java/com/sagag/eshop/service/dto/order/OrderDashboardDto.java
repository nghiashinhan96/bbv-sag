package com.sagag.eshop.service.dto.order;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class OrderDashboardDto implements Serializable {

  private static final long serialVersionUID = -77311455425194117L;

  private Integer newOrderQuantity;
  private Integer openOrderQuantity;
  private Integer orderedQuantity;

}
