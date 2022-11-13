package com.sagag.services.service.utils.order;

import com.sagag.services.ax.enums.AxOrderStatus;
import com.sagag.services.common.utils.DateUtils;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Class to define utility methods for Orders.
 */
@UtilityClass
public final class OrdersUtils {

  public static final String AGGREGRATION_ALL = "ALL";

  public static final List<String> ORDER_STATUSES_FILTER_LIST;

  public static final String ORDER_EMAIL_FAILED = "email_failed";

  static {
    ORDER_STATUSES_FILTER_LIST =
        Collections.unmodifiableList(
            Arrays.asList(AGGREGRATION_ALL,
                AxOrderStatus.NONE.name(),
                AxOrderStatus.PENDING.name(),
                AxOrderStatus.BACKORDER.name(),
                AxOrderStatus.DELIVERED.name(),
                AxOrderStatus.INVOICED.name(),
                AxOrderStatus.CANCELLED.name()
            ));
  }

  public static String getStrDateFrom(String dateFrom) {
    return StringUtils.isBlank(dateFrom) ? StringUtils.EMPTY : dateFrom + DateUtils.T
        + DateUtils.BEGIN_OF_DAY + DateUtils.Z;
  }

  public static String getStrDateTo(String dateTo) {
    return StringUtils.isBlank(dateTo) ? StringUtils.EMPTY : dateTo + DateUtils.T
        + DateUtils.END_OF_DAY + DateUtils.Z;
  }

  /**
   * Gets UTC begin of date from string.
   *
   * @param dateTo
   * @return date object
   */
  public static Date getDateFrom(String dateFrom) {
    if (StringUtils.isBlank(dateFrom)) {
      return null;
    }
    try {
      String beginOfDate = getStrDateFrom(dateFrom);
      return DateUtils.parseUTCDateFromString(beginOfDate, DateUtils.UTC_DATE_PATTERN_2);
    } catch (ParseException e) {
      throw new IllegalArgumentException(String.format("Invalid date format %s", dateFrom));
    }
  }

  /**
   * Gets UTC end of date from string.
   *
   * @param dateTo
   * @return date object
   */
  public static Date getDateTo(String dateTo) {
    if (StringUtils.isBlank(dateTo)) {
      return null;
    }
    try {
      String endOfDate = getStrDateTo(dateTo);
      return DateUtils.parseUTCDateFromString(endOfDate, DateUtils.UTC_DATE_PATTERN_2);
    } catch (ParseException e) {
      throw new IllegalArgumentException(String.format("Invalid date format %s", dateTo));
    }
  }

  public static Integer getArticleAbsQuantity(Integer quantity) {
    if (Objects.isNull(quantity)) {
      return null;
    }

    return Math.abs(quantity);
  }
}
