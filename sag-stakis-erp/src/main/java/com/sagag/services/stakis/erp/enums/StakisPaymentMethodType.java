package com.sagag.services.stakis.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StakisPaymentMethodType {

  CASH("Platba v hotovosti"),
  EUR_PAYMENT("EUR payment"),
  BANK_TRANSFER("");

  private String code;

}
