package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.CustomerCreditCheckResultDto;
import com.sagag.services.service.api.CustomerCreditService;
import com.sagag.services.service.credit.CustomerCreditValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerCreditServiceImpl implements CustomerCreditService {

  @Autowired
  private CustomerCreditValidator customerCreditValidator;

  @Override
  public CustomerCreditCheckResultDto checkCustomerCredit(UserInfo user, double customerCredit,
      double customerCashCreditPayment, ShopType shopType) {
    return customerCreditValidator.validate(user, customerCredit, customerCashCreditPayment,
        shopType);
  }

}
