package com.sagag.services.service.coupon.validator;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponArticleIdFilter;
import com.sagag.services.service.coupon.CouponValidationData;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponArticleIdValidator implements CouponValidator, CouponArticleIdFilter {

  @Override
  public boolean isInValid(CouponValidationData data) {
    final List<String> articleIds = data.getCoupConditions().getSplitedArticleIds();
    final List<ShoppingCartItem> appliedItems = data.getAppliedItems();
    return !CollectionUtils.isEmpty(articleIds)
        && CollectionUtils.isEmpty(filter(articleIds, appliedItems));
  }
}
