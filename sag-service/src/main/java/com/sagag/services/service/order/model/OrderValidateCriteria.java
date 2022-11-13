package com.sagag.services.service.order.model;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class OrderValidateCriteria {

  @NonNull
  private CreateOrderRequestBodyV2 body;

  @NonNull
  private OrderConditionContextBody orderCondition;

  @NonNull
  private UserInfo user;

  @NonNull
  private OrderRequestType requestType;
}
