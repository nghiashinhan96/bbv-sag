package com.sagag.services.service.request.order;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.services.domain.eshop.dto.GrantedBranchDto;
import com.sagag.services.service.order.model.CouponUseLogModel;
import com.sagag.services.service.order.model.OrderRequestType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderContextBuilder {

  private CreateOrderRequestBodyV2 body;

  private OrderConditionContextBody selectedOrderCondition;

  private OrderRequestType orderRequestType;

  private ShopType shopType;

  private CouponUseLogModel coupon;

  private GrantedBranchDto location;
}
