package com.sagag.services.service.coupon.validator;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.service.coupon.CouponValidationData;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Getter
public class CouponDiscountAmountValidator implements CouponValidator {

  @Autowired
  private IvdsArticleService ivdsArticleService;

  @Override
  public boolean isInValid(CouponValidationData data) {
    final UserInfo user = data.getUser();
    final String discountArtId = String.valueOf(data.getCoupConditions().getDiscountArticleId());
    final Optional<Double> discountNetPriceOpt =
        ivdsArticleService.getCounponPrice(user, discountArtId);
    discountNetPriceOpt.ifPresent(data::setDiscountNetPrice);

    return !discountNetPriceOpt.isPresent() || discountNetPriceOpt.get() <= 0;
  }
}
