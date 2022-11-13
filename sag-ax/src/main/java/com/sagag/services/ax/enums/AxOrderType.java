package com.sagag.services.ax.enums;

/**
 * Enumeration of AX Order Type.
 *
 */
public enum AxOrderType {

  KSL, ABS, STD, KSO_AUT, KSO_BEET, KSO_T, STD_KSO, KSO_DROP;

  public boolean isKsoAut() {
    return this == KSO_AUT || this == KSO_BEET || this == KSO_T || this == STD_KSO
        || this == KSO_DROP;
  }

  public boolean isKsoMix() {
    return this == STD_KSO;
  }
}
