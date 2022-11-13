package com.sagag.services.service.coupon.validation;

import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponArticleIdFilter;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponArticleIdValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.CouponValidationException.CouponErrorCase;
import com.sagag.services.service.exception.coupon.InvalidAffiliateCouponException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponValidateArticleIdStep extends AbstractCouponValidationStep
    implements CouponArticleIdFilter {

  @Autowired
  private CouponArticleIdValidator articleIdValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      final String couponCode = data.getCouponCode();
      final String msg = String.format("The coupon %s has no pim_id is matched", couponCode);
      final String code = CouponErrorCase.CE_IAF_003.code();
      final String key = CouponErrorCase.CE_IAF_003.key();
      throw new InvalidAffiliateCouponException(couponCode, code, key, msg);
    }


    List<ShoppingCartItem> appliedItems = data.getAppliedItems();
    data.setAppliedItems(filter(data.getCoupConditions().getSplitedArticleIds(), appliedItems));
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return articleIdValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate article id step";
  }
}
