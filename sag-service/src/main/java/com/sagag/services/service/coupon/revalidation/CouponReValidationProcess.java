package com.sagag.services.service.coupon.revalidation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponReValidationProcess implements InitializingBean {

  @Autowired
  private CouponReValidateCodeStep couponReValidateCodeStep;

  @Autowired
  private CouponReValidateLimitUsedStep couponReValidateLimitUsedStep;

  @Autowired
  private CouponReValidateDateStep couponReValidateDateStep;

  @Autowired
  private CouponReValidateUsageCountStep couponReValidateUsageCountStep;

  @Autowired
  private CouponReValidateAffiliateStep couponReValidateAffiliateStep;

  @Autowired
  private CouponReValidateCustomerStep couponReValidateCustomerStep;

  @Autowired
  private CouponReValidateGenartIdStep couponReValidateGenartIdStep;

  @Autowired
  private CouponReValidateBrandStep couponReValidateBrandStep;

  @Autowired
  private CouponReValidateArticleIdStep couponReValidateArticleIdStep;

  @Autowired
  private CouponReValidateMinimumOrderAmountStep couponReValidateMinimumOrderAmountStep;

  @Autowired
  private CouponReValidateMaximumOrderAmountStep couponReValidateMaximumOrderAmountStep;

  @Autowired
  private CouponReValidateDiscountAmountStep couponReValidateDiscountAmountStep;

  @Override
  public void afterPropertiesSet() throws Exception {
    setUpSteps();
  }

  public AbstractCouponReValidationStep getFirstStep() {
    return couponReValidateCodeStep;
  }

  //@formatter:off
  private void setUpSteps() {
    couponReValidateCodeStep
        .nextStep(couponReValidateLimitUsedStep)
        .nextStep(couponReValidateDateStep)
        .nextStep(couponReValidateUsageCountStep)
        // step 5 : check Country - not ready at the moment
        .nextStep(couponReValidateAffiliateStep)
        // Step 7 : verify for Customer Group - not ready at the moment
        .nextStep(couponReValidateCustomerStep)
        .nextStep(couponReValidateGenartIdStep)
        .nextStep(couponReValidateBrandStep)
        .nextStep(couponReValidateArticleIdStep)
        .nextStep(couponReValidateMinimumOrderAmountStep)
        .nextStep(couponReValidateMaximumOrderAmountStep)
        .nextStep(couponReValidateDiscountAmountStep);
  }
  //@formatter:on

}
