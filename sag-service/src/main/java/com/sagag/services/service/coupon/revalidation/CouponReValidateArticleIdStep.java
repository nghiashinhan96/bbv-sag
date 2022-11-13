package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponArticleIdFilter;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponArticleIdValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponReValidateArticleIdStep extends AbstractCouponReValidationStep
    implements CouponArticleIdFilter {

  @Autowired
  private CouponArticleIdValidator articleIdValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    if (getValidator().isInValid(data)) {
      return handleInvalidCaseAsDefault(data);
    }

    final List<ShoppingCartItem> appliedItems = data.getAppliedItems();
    data.setAppliedItems(filter(data.getCoupConditions().getSplitedArticleIds(), appliedItems));
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return articleIdValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate article id step";
  }
}
