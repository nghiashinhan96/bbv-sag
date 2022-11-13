package com.sagag.services.service.coupon.validation;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponGaIdFilter;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponGenartIdValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.CouponValidationException.CouponErrorCase;
import com.sagag.services.service.exception.coupon.InvalidAffiliateCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponValidateGenartIdStep extends AbstractCouponValidationStep
    implements CouponGaIdFilter {

  @Autowired
  private CouponGenartIdValidator genartIdValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      final String couponCode = data.getCouponCode();
      final String msg = String.format("The coupon %s has no gen_art id is matched", couponCode);
      final String code = CouponErrorCase.CE_IAF_001.code();
      final String key = CouponErrorCase.CE_IAF_001.key();
      throw new InvalidAffiliateCouponException(couponCode, code, key, msg);
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
    return "Coupon validate genart id step";
  }
}
