package com.sagag.services.service.order.model;

import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AfterOrderCriteria {

  private ShoppingCart shoppingCart;
  private OrderConfirmation axResult;
  private OrderHistory orderHistory;
  private CouponUseLog couponUseLog;
  private ShopType shopType;

  private Integer goodsReceiverId;
  private Long finalCustomerOrderId;

  private String reference;
  private String branchRemark;

  private ExternalOrderRequest externalOrderRequest;
  private String invoiceTypeCode;
  private Double totalPrice;
  private UserInfo userInfo;
  private Map<String, String> additionalTextDocs;
}
