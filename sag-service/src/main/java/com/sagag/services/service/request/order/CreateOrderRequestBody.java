package com.sagag.services.service.request.order;

import lombok.Data;

import java.io.Serializable;

/**
 * Request body class for creating order.
 */
@Data
public class CreateOrderRequestBody implements Serializable {

  private static final long serialVersionUID = 93455816654132189L;

  private OrderRequestBody data;

  private OrderConditionContextBody orderCondition;

  private String timezone;
}
