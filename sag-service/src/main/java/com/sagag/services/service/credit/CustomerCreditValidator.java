package com.sagag.services.service.credit;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.CustomerCreditCheckResultDto;

public interface CustomerCreditValidator {

  /**
   *
   */
  CustomerCreditCheckResultDto validate(UserInfo user, double customerCredit,
      double customerCashCreditPayment, ShopType shopType);
}
