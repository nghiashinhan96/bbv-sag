package com.sagag.services.service.coupon.revalidation;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponUsageCountValidator;
import com.sagag.services.service.coupon.validator.CouponValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponReValidateUsageCountStep extends AbstractCouponReValidationStep {

  @Autowired
  private CouponUsageCountValidator usageCountValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) {
    if (getValidator().isInValid(data)) {
      return handleInvalidCaseAsDefault(data);
    }
    CouponConditions coupConditions = data.getCoupConditions();
    int remainUsage = coupConditions.getUsageCount() - coupConditions.getUsedCount();
    data.setRemainUsage(remainUsage);
    return data;
  }

  @Override
  protected CouponValidator getValidator() {
    return usageCountValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon revalidate usage count step";
  }

}
