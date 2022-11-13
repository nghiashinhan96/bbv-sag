package com.sagag.services.article.api.domain.customer;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreditLimitInfo implements Serializable {

  private static final long serialVersionUID = 1984244649766940710L;

  private Double alreadyUsedCredit;
  private Double availableCredit;
  private Double alreadyUsedCreditCashPayment;
  private Double availableCreditCashPayment;
}
