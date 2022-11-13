package com.sagag.eshop.repo.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistorySearchCriteria {

  private String customerNumber;

  private String customerName;

  private String orderNumber;

  private String orderDate;

  private String type;

  private String totalPrice; // String to compare LIKE condition

  private Long affiliateId;

  private Long saleId;

  private Boolean orderDescByCustomerNumber;

  private Boolean orderDescByCustomerName;

  private Boolean orderDescByOrderNumber;

  private Boolean orderDescByOrderDate;

  private Boolean orderDescByType;

  private Boolean orderDescByTotalPrice;

}
