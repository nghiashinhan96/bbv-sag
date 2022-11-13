package com.sagag.services.domain.sag.erp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErpArticleMemoKey {
  FGAS("FGAS", StatusValue.NO),
  DEPOSIT_ITEM("DepositItem", StatusValue.NO),
  NON_RETURNABLE("NonReturnableItem", StatusValue.NO);

  @Getter
  @AllArgsConstructor
  public enum StatusValue {
    YES("Yes"), NO("No");

    private String value;
  }

  private String key;

  private StatusValue defaultValue;

}
