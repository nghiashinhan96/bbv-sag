package com.sagag.services.ax.availability;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.availability.calculator.PickupArrivalTimeCalculator;
import com.sagag.services.ax.availability.processor.AxArticleAvailabilityProcessor;
import com.sagag.services.ax.exception.WorkingDayNotFoundException;
import com.sagag.services.ax.holidays.impl.AxDefaultHolidaysCheckerImpl;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Slf4j
public class ArrivalTimeCalculatorTest {

  private static final String FIXED_SUNDAY = "2019-01-20T12:00:00Z";
  private static final String FIXED_WORKING_DAY = "2019-01-21T12:00:00Z";
  private static final String COUNTRY_NAME = AxDataTestUtils.COUNTRY_NAME_AT;
  private static final String TIME_ZONE = "user.timezone";
  private static final String CET = "CET";
  private static final String BRANCH_ID = "1001";

  @InjectMocks
  private PickupArrivalTimeCalculator arrivalTimeCalculator;

  @Mock
  private ArticleExternalService articleExtService;

  @Mock
  private AxDefaultHolidaysCheckerImpl axAustriaHolidaysChecker;

  @Mock
  private OpeningDaysCalendarRepository openingDaysRepo;

  @Mock
  private AxArticleAvailabilityProcessor availabilityProcessor;

  @Test
  public void testCalculateIfOutsideWorkingHoursSofortWeekend() throws WorkingDayNotFoundException {
    System.setProperty(TIME_ZONE, CET);
    DateTime today = new DateTime(FIXED_SUNDAY).withHourOfDay(8).withMinuteOfHour(1);
    when(axAustriaHolidaysChecker.isNonWorkingDay(any())).thenReturn(true);

    Date nextWorkingDate = today.plusDays(1).toDate();
    Availability availability = Availability.builder().sofort(true)
        .arrivalTime(AxDataTestUtils.dtfOut.withZoneUTC().print(today)).build();

    when(openingDaysRepo.findNextWorkingDay(
        Mockito.eq(new DateTime(availability.getArrivalTime()).toDate()), Mockito.any(),
        Mockito.any(), Mockito.any())).thenReturn(Optional.of(nextWorkingDate));

    arrivalTimeCalculator.calculateArrivalTime(availability, Optional.ofNullable(nextWorkingDate),
        SupportedAffiliate.DERENDINGER_AT, BRANCH_ID, Collections.emptyList(),
        AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    log.debug("testCalculateIfOutsideWorkingHoursSofortWeekend \n{}",
        SagJSONUtil.convertObjectToPrettyJson(availability));

    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability.getArrivalTime()).getHourOfDay());

    Assert.assertEquals(new DateTime(nextWorkingDate).getDayOfYear(),
        new DateTime(availability.getArrivalTime()).getDayOfYear());
  }

  @Test
  public void testCalculateIfOutsideWorkingHoursSofortTooEarly()
      throws WorkingDayNotFoundException {
    System.setProperty(TIME_ZONE, CET);
    // Change NOW to specific time 8:01 CET
    DateTime nowTooEarly = new DateTime(FIXED_WORKING_DAY).withHourOfDay(7).withMinuteOfHour(50);
    DateTimeUtils.setCurrentMillisFixed(nowTooEarly.getMillis());

    Date nextWorkingDate = nowTooEarly.plusDays(1).toDate();
    Availability availability = Availability.builder().sofort(true).build();

    arrivalTimeCalculator.calculateArrivalTime(availability, Optional.ofNullable(nextWorkingDate),
        SupportedAffiliate.DERENDINGER_AT, BRANCH_ID, Collections.emptyList(),
        AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability.getArrivalTime()).getHourOfDay());
    Assert.assertEquals(new DateTime(nowTooEarly).getDayOfYear(),
        new DateTime(availability.getArrivalTime()).getDayOfYear());
  }

  @Test
  public void testCalculateIfOutsideWorkingHoursSofortLunchTime()
      throws WorkingDayNotFoundException {
    System.setProperty(TIME_ZONE, CET);
    // Change NOW to specific time 8:01 CET
    DateTime nowTooEarly = new DateTime(FIXED_WORKING_DAY).withHourOfDay(12).withMinuteOfHour(50);
    DateTimeUtils.setCurrentMillisFixed(nowTooEarly.getMillis());

    Date nextWorkingDate = nowTooEarly.plusDays(1).toDate();
    Availability availability = Availability.builder().sofort(true).build();

    arrivalTimeCalculator.calculateArrivalTime(availability, Optional.ofNullable(nextWorkingDate),
        SupportedAffiliate.DERENDINGER_AT, BRANCH_ID, Collections.emptyList(),
        AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertEquals(AxDataTestUtils.mockWorkingHours().get(0).getLunchEndTime().getHourOfDay(),
        new DateTime(availability.getArrivalTime()).getHourOfDay());
  }

  @Test
  public void testCalculateIfOutsideWorkingHoursNonSofortHolidays()
      throws WorkingDayNotFoundException {
    System.setProperty(TIME_ZONE, CET);
    DateTime arrivalTimeTooHoliday =
        new DateTime("2020-01-01T12:00:00Z").withHourOfDay(7).withMinuteOfHour(50);
    when(axAustriaHolidaysChecker.isNonWorkingDay(any())).thenReturn(true);
    Availability availability = Availability.builder().sofort(false)
        .arrivalTime(AxDataTestUtils.dtfOut.withZoneUTC().print(arrivalTimeTooHoliday)).build();

    final DateTime nextWorkingDate = arrivalTimeTooHoliday.plusDays(1);
    when(openingDaysRepo.findNextWorkingDay(
        Mockito.eq(new DateTime(availability.getArrivalTime()).toDate()), Mockito.any(),
        Mockito.any(), Mockito.any())).thenReturn(Optional.of(nextWorkingDate.toDate()));

    arrivalTimeCalculator.calculateArrivalTime(availability,
        Optional.ofNullable(nextWorkingDate.toDate()), SupportedAffiliate.DERENDINGER_AT, null,
        Collections.emptyList(),
        AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    log.debug("testCalculateIfOutsideWorkingHoursNonSofortHolidays\n{}",
        SagJSONUtil.convertObjectToPrettyJson(availability));

    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability.getArrivalTime()).getHourOfDay());
    Assert.assertEquals(nextWorkingDate.getDayOfYear(),
        new DateTime(availability.getArrivalTime()).getDayOfYear());
  }

  @Test
  public void testCalculateIfOutsideWorkingHoursNonSofortTooEarly()
      throws WorkingDayNotFoundException {
    System.setProperty(TIME_ZONE, CET);
    DateTime arrivalTimeTooEarly =
        new DateTime(FIXED_WORKING_DAY).withHourOfDay(7).withMinuteOfHour(50);

    Availability availability = Availability.builder().sofort(true)
        .arrivalTime(AxDataTestUtils.dtfOut.withZoneUTC().print(arrivalTimeTooEarly)).build();

    arrivalTimeCalculator.calculateArrivalTime(availability,
        Optional.ofNullable(arrivalTimeTooEarly.toDate()),
        SupportedAffiliate.DERENDINGER_AT, BRANCH_ID, Collections.emptyList(),
        AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);


    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability.getArrivalTime()).getHourOfDay());
    final int expectedDayOfYear = 59;
    Assert.assertEquals(expectedDayOfYear,
        new DateTime(availability.getArrivalTime()).getDayOfYear());
  }

  @Test
  public void testCalculateIfOutsideWorkingHoursNonSofortLunchTime()
      throws WorkingDayNotFoundException {
    System.setProperty(TIME_ZONE, CET);
    DateTime arrivalTimeLunchTime =
        new DateTime(FIXED_WORKING_DAY).withHourOfDay(12).withMinuteOfHour(50);

    Availability availability = Availability.builder().sofort(true)
        .arrivalTime(AxDataTestUtils.dtfOut.withZoneUTC().print(arrivalTimeLunchTime)).build();

    arrivalTimeCalculator.calculateArrivalTime(availability,
        Optional.ofNullable(arrivalTimeLunchTime.toDate()),
        SupportedAffiliate.DERENDINGER_AT, BRANCH_ID, Collections.emptyList(),
        AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertEquals(AxDataTestUtils.LUNCH_TIME_END_13,
        new DateTime(availability.getArrivalTime()).getHourOfDay());
    Assert.assertEquals(new DateTime(arrivalTimeLunchTime).getDayOfYear(),
        new DateTime(availability.getArrivalTime()).getDayOfYear());
  }

  @Test
  public void testCalculateIfOutsideWorkingHoursNonSofortAfterBusinessTime()
      throws WorkingDayNotFoundException {
    System.setProperty(TIME_ZONE, CET);
    DateTime arrivalTime =
        new DateTime("2020-01-06T12:00:00Z").withHourOfDay(19).withMinuteOfHour(50);
    when(axAustriaHolidaysChecker.isNonWorkingDay(any())).thenReturn(false);
    Availability availability = Availability.builder().sofort(false)
        .arrivalTime(AxDataTestUtils.dtfOut.withZoneUTC().print(arrivalTime)).build();


    arrivalTimeCalculator.calculateArrivalTime(availability,
        Optional.ofNullable(arrivalTime.toDate()),
        SupportedAffiliate.DERENDINGER_AT, BRANCH_ID, Collections.emptyList(),
        AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    log.debug("testCalculateIfOutsideWorkingHoursNonSofortHolidays\n{}",
        SagJSONUtil.convertObjectToPrettyJson(availability));

    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability.getArrivalTime()).getHourOfDay());
  }

}
