package com.sagag.services.service.coupon.validation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponValidationProcess implements InitializingBean {

  @Autowired
  private CouponValidateCodeStep couponValidateCodeStep;

  @Autowired
  private CouponValidateLimitUsedStep couponValidateLimitUsedStep;

  @Autowired
  private CouponValidateDateStep couponValidateDateStep;

  @Autowired
  private CouponValidateUsageCountStep couponValidateUsageCountStep;

  @Autowired
  private CouponValidateAffiliateStep couponValidateAffiliateStep;

  @Autowired
  private CouponValidateCustomerStep couponValidateCustomerStep;

  @Autowired
  private CouponValidateGenartIdStep couponValidateGenartIdStep;

  @Autowired
  private CouponValidateBrandStep couponValidateBrandStep;

  @Autowired
  private CouponValidateArticleIdStep couponValidateArticleIdStep;

  @Autowired
  private CouponValidateMinimumOrderAmountStep couponValidateMinimumOrderAmountStep;

  @Autowired
  private CouponValidateMaximumOrderAmountStep couponValidateMaximumOrderAmountStep;

  @Autowired
  private CouponValidateDiscountAmountStep couponValidateDiscountAmountStep;

  @Override
  public void afterPropertiesSet() {
    setUpSteps();
  }

  public AbstractCouponValidationStep getFirstStep() {
    return couponValidateCodeStep;
  }

  //@formatter:off
  private void setUpSteps() {
    couponValidateCodeStep
        .nextStep(couponValidateLimitUsedStep)
        .nextStep(couponValidateDateStep)
        .nextStep(couponValidateUsageCountStep)
        // step 5 : check Country - not ready at the moment
        .nextStep(couponValidateAffiliateStep)
        // Step 7 : verify for Customer Group - not ready at the moment
        .nextStep(couponValidateCustomerStep)
        .nextStep(couponValidateGenartIdStep)
        .nextStep(couponValidateBrandStep)
        .nextStep(couponValidateArticleIdStep)
        .nextStep(couponValidateMinimumOrderAmountStep)
        .nextStep(couponValidateMaximumOrderAmountStep)
        .nextStep(couponValidateDiscountAmountStep);
  }
  //@formatter:on
}
