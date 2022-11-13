package com.sagag.services.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Class to provide date utility methods for eConnect project.
 */
@UtilityClass
@Slf4j
public final class DateUtils {

  public static final String SWISS_DATE_PATTERN = "dd.MM.yyyy HH:mm:ss";

  public static final String SWISS_DATE_PATTERN_1 = "dd.MM.yyyy HH:mm";

  public static final String SWISS_DATE_PATTERN_2 = "dd.MM.yyyy / HH:mm";

  public static final String SWISS_DATE_PATTERN_3 = "dd.MM.yyyy";

  public static final String SWISS_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

  public static final String SWISS_DATE_WITH_YEAR_MONTH_PATTERN = "yyyyMM";

  public static final String UTC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  public static final String UTC_DATE_PATTERN_2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  public static final String BEGIN_OF_DAY = "00:00:00.000";

  public static final String END_OF_DAY = "23:59:59.999";

  public static final String END_OF_DAY_IGNORE_MILISECONDS = "23:59:59.000";

  public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

  public static final String T = "T";

  public static final String Z = "Z";

  public static final TimeZone TZ_UTC = TimeZone.getTimeZone("UTC");

  public static final String DAY = "day";

  public static final String MONTH = "month";

  public static final String YEAR = "year";

  public static final String TIME_PATTERN = "HH:mm:ss";

  public static final String SHORT_TIME_PATTERN = "HH:mm";

  public static final String TIME_OPTIONAL_PATTERN = "H[H]:mm[:ss]";

  public static final String DATE_RESET_TIME = "T00:00:00";

  public static final String TZ_CET_STR = "CET";

  /**
   * Converts a date to a Swiss <code>String</code> date.
   *
   * <p>
   * The format after converting is dd.MM.yyyy HH:mm:ss
   *
   * @param date the {@link Date}
   * @return the <code>String</code> date. E.g. 20.10.2016 16:20:03
   */
  public static String toStringDate(final Date date) {
    if (date == null) {
      return StringUtils.EMPTY;
    }
    return new SimpleDateFormat(SWISS_DATE_PATTERN).format(date);
  }

  /**
   * Converts a date to a Swiss <code>String</code> date with custom pattern.
   *
   * <p>
   * The format after converting is dd.MM.yyyy HH:mm:ss
   *
   * @param date the {@link Date}
   * @param pattern the {@link String}
   * @return the <code>String</code> date. E.g. 20.10.2016 / 16:20:03
   */
  public static String toStringDate(final Date date, final String pattern) {
    if (date == null || StringUtils.isBlank(pattern)) {
      return StringUtils.EMPTY;
    }
    return new SimpleDateFormat(pattern).format(date);
  }

  /**
   * Check if date parameter is in right format.
   *
   * @param dateString the parameter to check
   * @return boolean true if the string is in date format.
   */
  public static Date toDate(String dateString, String pattern) {
    final DateFormat format = new SimpleDateFormat(pattern);
    format.setLenient(false);
    Date date = null;
    if (null == dateString) {
      return null;
    }
    try {
      date = format.parse(dateString);
    } catch (ParseException ex) {
      return null;
    }
    return date;
  }

  /**
   * Get UTC Date of input date.
   *
   * @param input date to get UTC date
   * @return the UTC date
   */
  public static Date getUTCDate(final Date input) {
    try {
      return new SimpleDateFormat(SWISS_DATE_PATTERN).parse(getUTCDateStr(input));
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * Get UTC Date String of input date.
   *
   * @param input date to get UTC date
   * @return the UTC date string
   */
  public static String getUTCDateStr(final Date input) {
    if (input == null) {
      return StringUtils.EMPTY;
    }
    final DateFormat format = new SimpleDateFormat(SWISS_DATE_PATTERN);
    format.setTimeZone(TZ_UTC);
    format.setLenient(false);
    return format.format(input);
  }

  public static String getUTCDateStrWithTimeZone(final Date input) {
    if (input == null) {
      return StringUtils.EMPTY;
    }
    return toStringDate(DateUtils.getUTCDate(input), UTC_DATE_PATTERN);
  }

  public static String getUTCStr(final DateTime date) {
    return DateTimeFormat.forPattern(DateUtils.UTC_DATE_PATTERN).withZoneUTC()
        .print(date);
  }

  /**
   * Format UTC Date String.
   *
   * @param utcDateStr date to get UTC date
   * @param pattern date pattern
   * @return the UTC date string
   */
  public static String formatUTCDateStr(final String utcDateStr, final String pattern) {
    if (StringUtils.isBlank(utcDateStr) || StringUtils.isBlank(pattern)) {
      return StringUtils.EMPTY;
    }
    return toStringDate(toDate(utcDateStr, UTC_DATE_PATTERN), pattern);
  }

  public static String formatDateStrWithTimeZone(final String dateStr, final String pattern,
      final TimeZone timeZone) {
    if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(pattern)) {
      return StringUtils.EMPTY;
    }
    return Instant.parse(dateStr).atZone(timeZone.toZoneId())
        .format(DateTimeFormatter.ofPattern(pattern));
  }

  /**
   * Gets UTC date from string.
   *
   * @param dateStr
   * @param pattern
   *
   * @return Date object
   *
   * @throws ParseException
   */
  public static Date parseUTCDateFromString(final String dateStr, final String pattern)
      throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    dateFormat.setTimeZone(DateUtils.TZ_UTC);
    return dateFormat.parse(dateStr);
  }

  public static Date parseUTCDateFromStringThrowExceptionIfHasProblem(final String time) {
    try {
      return DateUtils.parseUTCDateFromString(time, DateUtils.UTC_DATE_PATTERN);
    } catch (ParseException e) {
      throw new IllegalArgumentException("The date format is not valid");
    }
  }

  public static String getUTCDateString(Date date, final String pattern) {
    final DateFormat format = new SimpleDateFormat(pattern);
    format.setTimeZone(TZ_UTC);
    format.setLenient(false);
    return format.format(date);
  }

  public static LocalTime toLocalTime(Time time) {
    return Objects.isNull(time) ? null : new LocalTime(time);
  }

  public static LocalTime toLocalTime(java.time.LocalTime time) {
    return Objects.isNull(time) ? null
        : new LocalTime(time.getHour(), time.getMinute(), time.getSecond());
  }

  /**
   * Format Time String.
   *
   * @param time time to format
   * @param pattern time pattern
   * @return the time in string with pattern
   */
  public static String formatTimeStr(final String time, final String pattern) {
    if (StringUtils.isBlank(time) || StringUtils.isBlank(pattern)) {
      return StringUtils.EMPTY;
    }
    return LocalTime.parse(time).toString(pattern);
  }

  /**
   * Adds or subtracts the specified amount of time to the given calendar field
   *
   * @param amount - the amount of date to be added or subtracted
   * @return the date later by amount from current date
   */
  public static Date getDateLaterFromCurrent(final int amount) {
    final Calendar calendar = Calendar.getInstance();
    if (Objects.isNull(amount)) {
      return calendar.getTime();
    }
    calendar.add(Calendar.DATE, amount);
    return calendar.getTime();
  }

  /**
   * Convert date to LocalTime.
   *
   * @param date
   * @return java.time.LocalTime
   */
  public static java.time.LocalTime convertDateToLocalTime(final Date date) {
    if (Objects.isNull(date)) {
      return null;
    }
    DateTime dateTime = new DateTime(date.getTime());
    return java.time.LocalTime.of(dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
  }

  public static XMLGregorianCalendar newXMLGregorianCalendar(Instant instant) {
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(instant.toString());
    } catch (DatatypeConfigurationException e) {
      log.error("Construct newXMLGregorianCalendar has error", e);
      return null;
    }
  }

  /**
   * Convert time string to Time.
   *
   * @param time
   * @return java.sql.Time
   */
  public static Time convertStringToTime(final String time, String pattern) {
    if (StringUtils.isBlank(time)) {
      return null;
    }
    return Time.valueOf(LocalTime.parse(time).toString(pattern));
  }

  public static boolean isTimeBetweenDuration(java.time.LocalTime time,
      java.time.LocalTime startTime, java.time.LocalTime endTime) {
    if (Objects.isNull(time) || Objects.isNull(startTime) || Objects.isNull(endTime)) {
      return false;
    }
    return !time.isBefore(startTime) && !time.isAfter(endTime);
  }

  public static java.time.LocalDateTime toLocalDateTime(DateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return java.time.LocalDateTime.of(dateTime.getYear(), dateTime.getMonthOfYear(),
        dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
  }

  public static java.time.LocalTime toLocalTime(String timeStr) {
    if (StringUtils.isEmpty(timeStr)) {
      return null;
    }
    return java.time.LocalTime.parse(timeStr);
  }

  public static DateTime getCETDateTime(String utcTime) {
    if (StringUtils.isBlank(utcTime)) {
      return DateTime.now(DateTimeZone.forID(TZ_CET_STR));
    }
    return new DateTime(utcTime, DateTimeZone.forID(TZ_CET_STR));
  }
}
