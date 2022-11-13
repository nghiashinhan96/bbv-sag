package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.CouponConditionsRepository;
import com.sagag.eshop.repo.api.CouponUseLogRepository;
import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.eshop.service.api.CouponService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CouponServiceImpl implements CouponService {

  @Autowired
  CouponUseLogRepository couponUseLogRepo;

  @Autowired
  CouponConditionsRepository couponConditionsRepo;

  @Override
  public void update(CouponUseLog couponUseLog) {
    couponUseLogRepo.save(couponUseLog);
    //update Usage && used number coupon-condtion
    final Optional<CouponConditions> coupon =
        couponConditionsRepo.findOneByCouponsCode(couponUseLog.getCouponsCode());
    if (!coupon.isPresent()) {
      return;
    }
    CouponConditions couponCondition = coupon.get();
    couponCondition.setUsedCount(couponCondition.getUsedCount() + 1);
    couponConditionsRepo.save(couponCondition);
  }
}
