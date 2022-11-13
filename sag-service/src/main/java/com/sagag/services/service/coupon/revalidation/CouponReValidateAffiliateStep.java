package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponAffiliateValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponReValidateAffiliateStep extends AbstractCouponReValidationStep {

  @Autowired
  private CouponAffiliateValidator affiliateValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    return handleAsDefault(data);
  }

  @Override
  protected CouponValidator getValidator() {
    return affiliateValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate affiliate step";
  }
}
