package com.sagag.services.service.coupon.validator;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponBrandFilter;
import com.sagag.services.service.coupon.CouponValidationData;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponBrandValidator implements CouponValidator, CouponBrandFilter {

  @Override
  public boolean isInValid(CouponValidationData data) {
    final List<String> brands = data.getCoupConditions().getSplitedBrands();
    final List<ShoppingCartItem> appliedItems = data.getAppliedItems();
    return !CollectionUtils.isEmpty(brands)
        && CollectionUtils.isEmpty(filter(brands, appliedItems));
  }
}
