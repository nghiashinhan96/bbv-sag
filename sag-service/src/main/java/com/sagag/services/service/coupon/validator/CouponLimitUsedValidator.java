package com.sagag.services.service.coupon.validator;

import com.sagag.eshop.repo.api.CouponUseLogRepository;
import com.sagag.services.service.coupon.CouponValidationData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponLimitUsedValidator implements CouponValidator {

  @Autowired
  private CouponUseLogRepository couponUseLogRepo;

  @Override
  public boolean isInValid(CouponValidationData data) {
    return data.getCoupConditions().isSingleCustomer()
        && couponUseLogRepo.existsCouponCodeByCustomerNr(data.getUser().getCustNrStr(),
            data.getCouponCode());
  }
}
