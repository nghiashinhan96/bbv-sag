package com.sagag.services.ax.availability.backorder;

import com.sagag.services.common.enums.SupportedAffiliate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BackOrderUtils {

  private static final int BACKORDER_NEXT_WORKING_DAYS = 6;

  private static final int BACKORDER_NEXT_WORKING_DAYS_AT = 1;

  public static int getBackOrderDays(SupportedAffiliate supportedAffiliate) {
    if (supportedAffiliate.isAtAffiliate()) {
      return BACKORDER_NEXT_WORKING_DAYS_AT;
    }
    return BACKORDER_NEXT_WORKING_DAYS;
  }
}
