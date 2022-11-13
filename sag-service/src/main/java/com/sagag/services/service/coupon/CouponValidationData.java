package com.sagag.services.service.coupon;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponValidationData {
  private String couponCode;
  private CouponConditions coupConditions;
  private UserInfo user;
  private ShoppingCart cart;
  private List<ShoppingCartItem> appliedItems;
  private double discountNetPrice;
  private int remainUsage;
  private boolean endProcess;
  private boolean invalidCoupon;
}
