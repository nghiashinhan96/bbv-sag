package com.sagag.services.common.enums.order;

import org.apache.commons.lang3.StringUtils;

/**
 * Types of order history.
 */
public enum OrderType {

  /** default ORDER type if the order type name was not defined from AX. */
  ORDER,

  /** COUNTER order type. */
  COUNTER,

  /** ABS order type. */
  ABS,

  /** STD order type. */
  STD,

  /** KSO_AUT order type. */
  KSO_AUT;

  /**
   * Returns null-safe order type as default order type (e.g. ORDER) from undefined/blank order
   * <code>typeName</code>.
   * <p>
   * This is because NonDVSE does not define any order type, but AX does.
   *
   * @param name the order type name
   * @return order type enumeration value, {@link OrderType#ORDER} otherwise if
   *         <code>typeName</code> was undefined/blank.
   */
  public static OrderType getNullSafeType(String name) {
    if (StringUtils.isBlank(name)) {
      return ORDER;
    }
    return valueOf(name);
  }
}
