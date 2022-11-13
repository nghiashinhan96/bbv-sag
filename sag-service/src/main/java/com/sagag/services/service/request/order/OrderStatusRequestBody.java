package com.sagag.services.service.request.order;

import lombok.Data;

import java.io.Serializable;

/**
 * Request body class for change sales order status.
 */
@Data
public class OrderStatusRequestBody implements Serializable {

  private static final long serialVersionUID = 2994851512339844950L;

  private long orderHistoryId;

  private String orderNr;


}
