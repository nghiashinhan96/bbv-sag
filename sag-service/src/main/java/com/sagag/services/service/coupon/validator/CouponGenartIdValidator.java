package com.sagag.services.service.coupon.validator;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponGaIdFilter;
import com.sagag.services.service.coupon.CouponValidationData;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponGenartIdValidator implements CouponValidator, CouponGaIdFilter {

  @Override
  public boolean isInValid(CouponValidationData data) {
    final List<String> genartIDs = data.getCoupConditions().getSplitedGenartIds();
    final List<ShoppingCartItem> appliedItems = data.getAppliedItems();
    return (!CollectionUtils.isEmpty(genartIDs)
        && CollectionUtils.isEmpty(filter(genartIDs, appliedItems)));
  }
}
