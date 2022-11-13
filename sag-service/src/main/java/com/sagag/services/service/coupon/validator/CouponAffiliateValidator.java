package com.sagag.services.service.coupon.validator;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.service.coupon.CouponValidationData;

import org.springframework.stereotype.Component;

@Component
public class CouponAffiliateValidator implements CouponValidator {

  private static final String GROUP_PERMISSION = "Group";

  @Override
  public boolean isInValid(CouponValidationData data) {
    final CouponConditions coupConditions = data.getCoupConditions();
    final UserInfo user = data.getUser();
    final String affiliate = coupConditions.getAffiliate();
    return !GROUP_PERMISSION.equalsIgnoreCase(affiliate)
        && !affiliate.equalsIgnoreCase(user.getAffiliateShortName());
  }
}
