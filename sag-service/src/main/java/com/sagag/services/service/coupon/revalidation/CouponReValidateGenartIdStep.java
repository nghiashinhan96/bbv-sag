package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponGaIdFilter;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponGenartIdValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponReValidateGenartIdStep extends AbstractCouponReValidationStep
    implements CouponGaIdFilter {

  @Autowired
  private CouponGenartIdValidator genartIdValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    if (getValidator().isInValid(data)) {
      return handleInvalidCaseAsDefault(data);
    }

    final List<ShoppingCartItem> appliedItems = data.getAppliedItems();
    data.setAppliedItems(filter(data.getCoupConditions().getSplitedGenartIds(), appliedItems));
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return genartIdValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate genart id step";
  }
}
