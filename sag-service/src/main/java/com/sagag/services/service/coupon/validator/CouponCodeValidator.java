package com.sagag.services.service.coupon.validator;

import com.sagag.eshop.repo.api.CouponConditionsRepository;
import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.service.coupon.CouponValidationData;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Getter
public class CouponCodeValidator implements CouponValidator {

  @Autowired
  private CouponConditionsRepository couponConditionsRepo;

  @Override
  public boolean isInValid(CouponValidationData data) {
    final Optional<CouponConditions> conditions =
        couponConditionsRepo.findOneByCouponsCode(data.getCouponCode());
    conditions.ifPresent(data::setCoupConditions);
    return !conditions.isPresent();
  }
}
