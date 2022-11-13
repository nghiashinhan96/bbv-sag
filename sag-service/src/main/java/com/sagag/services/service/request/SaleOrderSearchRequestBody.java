package com.sagag.services.service.request;

import lombok.Data;

import java.io.Serializable;


/**
 * Request body class for viewing order histories.
 */
@Data
public class SaleOrderSearchRequestBody implements Serializable {

  private static final long serialVersionUID = -5576846054516499978L;

  private String customerNumber;

  private String customerName;

  private String orderNumber;

  private String orderDate;

  private String type;

  private String totalPrice; // String to compare LIKE condition

  private Boolean orderDescByCustomerNumber;

  private Boolean orderDescByCustomerName;

  private Boolean orderDescByOrderNumber;

  private Boolean orderDescByOrderDate;

  private Boolean orderDescByType;

  private Boolean orderDescByTotalPrice;

  private int page;

  private int size;

}
