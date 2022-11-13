package com.sagag.services.common.enums;

import lombok.Getter;

@Getter
public enum FinalCustomerType {

  ONLINE, PASSANT;

  public boolean isOnline() {
    return this == ONLINE;
  }
}
