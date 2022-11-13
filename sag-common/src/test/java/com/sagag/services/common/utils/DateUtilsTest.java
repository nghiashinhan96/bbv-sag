package com.sagag.services.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtilsTest {

  @Test
  public void testToStringDateBlank() {
    Assert.assertThat(StringUtils.EMPTY, Is.is(DateUtils.toStringDate(null)));
  }

  @Test
  public void testToStringDate() {
    Calendar calendar = Calendar.getInstance();
    // 20.10.2016 16:20:03
    calendar.set(2016, 9, 20, 16, 20, 3);
    Assert.assertThat("20.10.2016 16:20:03", Is.is(DateUtils.toStringDate(calendar.getTime())));
  }

  @Test
  public void testToDateWithYearMonth_OK() throws ParseException {
    String stringDate = "201610";
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.SWISS_DATE_WITH_YEAR_MONTH_PATTERN);

    Assert.assertThat(sdf.parse(stringDate),
        Is.is(DateUtils.toDate(stringDate, DateUtils.SWISS_DATE_WITH_YEAR_MONTH_PATTERN)));
  }

  @Test
  public void testToDateWithYearMonth_inputStringNull() throws ParseException {
    Assert.assertThat(null,
        Is.is(DateUtils.toDate(null, DateUtils.SWISS_DATE_WITH_YEAR_MONTH_PATTERN)));
  }

  @Test
  public void testToDateWithYearMonth_parseFailed() throws ParseException {
    String stringDate = "201650";
    Assert.assertThat(null,
        Is.is(DateUtils.toDate(stringDate, DateUtils.SWISS_DATE_WITH_YEAR_MONTH_PATTERN)));
  }

  @Test
  public void testGetUTCDateStr_parseOK() {
    Assert.assertNotNull(DateUtils.getUTCDateStr(Calendar.getInstance().getTime()));
  }

  @Test
  public void testGetUTCDateStr_parseNullDate() {
    Assert.assertThat(StringUtils.EMPTY, Is.is(DateUtils.getUTCDateStr(null)));
  }

  @Test
  public void testGetUTCDate_parseOK() {
    Assert.assertNotNull(DateUtils.getUTCDate(Calendar.getInstance().getTime()));
  }

  @Test
  public void testGetUTCDate_parseNullDate() {
    Assert.assertNull(DateUtils.getUTCDate(null));
  }

  @Test
  public void testToStringDate_parseCustomPattern() {
    Calendar calendar = Calendar.getInstance();
    // 20.10.2016 16:20:03
    calendar.set(2016, 9, 20, 16, 20, 3);
    Assert.assertThat("20.10.2016 / 16:20",
        Is.is(DateUtils.toStringDate(calendar.getTime(), DateUtils.SWISS_DATE_PATTERN_2)));
  }

  @Test
  public void testFormatUTCDateStr() {
    // 20.10.2016 16:20:03
    final String utcDateStr = "2016-10-20T16:20:03Z";
    Assert.assertThat("20.10.2016 / 16:20",
        Is.is(DateUtils.formatUTCDateStr(utcDateStr, DateUtils.SWISS_DATE_PATTERN_2)));
  }

  @Test
  public void testformatDateStrWithTimeZone() {
    // 20.10.2016 16:20:03
    final String utcDateStr = "2017-02-21T08:15:00Z";
    final String zoneId = "Asia/Jakarta";
    Assert.assertThat("21.02.2017 / 15:15", Is.is(DateUtils.formatDateStrWithTimeZone(utcDateStr,
        DateUtils.SWISS_DATE_PATTERN_2, TimeZone.getTimeZone(ZoneId.of(zoneId)))));
  }

  @Test
  public void givenDateShouldReturnUtcString() {
    Assert.assertNotNull(DateUtils.getUTCDateStrWithTimeZone(Calendar.getInstance().getTime()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertTimeStringToTime_InvalidTimeString() {
    DateUtils.convertStringToTime("09:", DateUtils.TIME_PATTERN);
  }

  @Test
  public void testConvertTimeStringToTime() {
    Time convertedTime = DateUtils.convertStringToTime("09:00", DateUtils.TIME_PATTERN);
    Assert.assertThat("09:00",
        Is.is(DateUtils.toStringDate(convertedTime, DateUtils.SHORT_TIME_PATTERN)));
  }
}
