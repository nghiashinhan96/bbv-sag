package com.sagag.services.common.enums;

public enum UserHistoryFromSource {

  C4C, C4S;

  public boolean isSalesOnbehalfMode() {
    return C4S == this;
  }

  public static UserHistoryFromSource findFromSource(boolean isSalesOnbehalf) {
    return isSalesOnbehalf ? C4S : C4C;
  }
}
