package com.sagag.services.service.coupon.validation;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponUsageCountValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;
import com.sagag.services.service.exception.coupon.FullUsageCouponException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CouponValidateUsageCountStep extends AbstractCouponValidationStep {

  @Autowired
  private CouponUsageCountValidator usageCountValidator;

  @Override
  public CouponValidationData handle(CouponValidationData data) throws CouponValidationException {
    if (getValidator().isInValid(data)) {
      final String msg = String.format("The coupon %s is full usage", data.getCouponCode());
      log.error(msg);
      throw new FullUsageCouponException(msg);
    }

    CouponConditions coupConditions = data.getCoupConditions();
    int remainUsage = coupConditions.getUsageCount() - coupConditions.getUsedCount();
    data.setRemainUsage(remainUsage);
    return data;
  }

  @Override
  protected CouponUsageCountValidator getValidator() {
    return usageCountValidator;
  }

  @Override
  protected String getStepName() {
    return "Coupon validate usage count step";
  }
}
