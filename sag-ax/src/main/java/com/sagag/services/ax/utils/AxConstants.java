package com.sagag.services.ax.utils;

import lombok.experimental.UtilityClass;

/**
 * Constant class to define some common static value in AX.
 *
 */
@UtilityClass
public class AxConstants {

  public static final String DEFAULT_BRANCH_ID = "1001";

  /** Constants for E-Shop Sales Origin Id (AT, CH...). */
  public static final String ESHOP_SALES_ORIGIN_ID = "E-Shop";

  public static final String DELIVERY_IMMEDIATE = "delivery.immediate";

  public static final String DELIVERY_24HOURS = "delivery.24hours";

  public static final int DEFAULT_SIZE_OF_PAGE = 100;

  public static final String NON_RETURNABLE = "ExcludeReturnOrders";

}
