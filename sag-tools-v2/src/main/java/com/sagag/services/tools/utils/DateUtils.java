package com.sagag.services.tools.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * Utilities for Date Time.
 */
@UtilityClass
public final class DateUtils {

  public static final String DATE_TIME_PATTERN = "MM.dd.yyyy HH:mm:ss";

  public static final String DATE_PATTERN = "MM.dd.yyyy";

  public static final String SWISS_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

  public static Date toDateByDefaultPattern(final String value) {
    return toDateByPattern(value, DATE_PATTERN);
  }

  public static Date toDateTimeByDefaultPattern(final String value) {
    return toDateByPattern(value, DATE_TIME_PATTERN);
  }

  public static Date toDateByPattern(final String value, final String pattern) {
    if (StringUtils.isBlank(value) || StringUtils.isBlank(pattern)) {
      return null;
    }
    return DateTime.parse(value, DateTimeFormat.forPattern(value)).toDate();
  }

}
