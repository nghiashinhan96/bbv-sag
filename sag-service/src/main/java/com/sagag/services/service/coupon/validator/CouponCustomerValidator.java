package com.sagag.services.service.coupon.validator;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.service.coupon.CouponValidationData;

import org.springframework.stereotype.Component;

@Component
public class CouponCustomerValidator implements CouponValidator {

  @Override
  public boolean isInValid(CouponValidationData data) {
    final CouponConditions coupConditions = data.getCoupConditions();
    final UserInfo user = data.getUser();
    final String customerNr = coupConditions.getCustomerNr();
    return customerNr != null && !customerNr.equalsIgnoreCase(user.getCustNrStr());
  }
}
