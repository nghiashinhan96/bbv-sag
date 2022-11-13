package com.sagag.services.service.order.handler;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;

import java.util.List;

public interface OrderHandler {

  public List<OrderConfirmation> handleOrder(final UserInfo user,
      final CreateOrderRequestBodyV2 body, final ApiRequestType apiType, final ShopType shopType,
      Boolean ksoForcedDisabled) throws ValidationException;
}
