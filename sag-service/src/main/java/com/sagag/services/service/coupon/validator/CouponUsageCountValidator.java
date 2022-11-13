package com.sagag.services.service.coupon.validator;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.service.coupon.CouponValidationData;

import org.springframework.stereotype.Component;

@Component
public class CouponUsageCountValidator implements CouponValidator {

  @Override
  public boolean isInValid(CouponValidationData target) {
    final CouponConditions coupConditions = target.getCoupConditions();
    final int usageCount = coupConditions.getUsageCount();
    final int usedCount = coupConditions.getUsedCount();
    return usedCount >= usageCount;
  }
}
