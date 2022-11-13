package com.sagag.services.service.coupon.validator;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.service.coupon.CouponValidationData;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class CouponDateValidator implements CouponValidator {

  @Override
  public boolean isInValid(CouponValidationData data) {
    final CouponConditions coupConditions = data.getCoupConditions();
    final Date currentDate = Calendar.getInstance().getTime();
    final Date dateStart = coupConditions.getDateStart();
    final Date dateEnd = coupConditions.getDateEnd();
    return (currentDate.compareTo(dateStart) < 0 || currentDate.compareTo(dateEnd) > 0);
  }
}
