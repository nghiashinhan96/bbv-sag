package com.sagag.services.service.api;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.CustomerCreditCheckResultDto;

/**
 * Interface to define the APIs for customer Credit.
 */
public interface CustomerCreditService {

  /**
   * Calculates and does check credit base on customer credit and open cart total.
   *
   * @param user information of user whom belong to a customer
   * @param customerCredit
   * @param customerCashCreditPayment
   * @return CustomerCreditCheckResultDto
   */
  CustomerCreditCheckResultDto checkCustomerCredit(UserInfo user, double customerCredit,
      double customerCashCreditPayment, ShopType shopType);

}
