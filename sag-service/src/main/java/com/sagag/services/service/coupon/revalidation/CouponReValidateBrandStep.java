package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponBrandFilter;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponBrandValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponReValidateBrandStep extends AbstractCouponReValidationStep
    implements CouponBrandFilter {

  @Autowired
  private CouponBrandValidator brandValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    if (getValidator().isInValid(data)) {
      return handleInvalidCaseAsDefault(data);
    }
    final List<ShoppingCartItem> appliedItems = data.getAppliedItems();
    data.setAppliedItems(filter(data.getCoupConditions().getSplitedBrands(), appliedItems));
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return brandValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate brand step";
  }
}
