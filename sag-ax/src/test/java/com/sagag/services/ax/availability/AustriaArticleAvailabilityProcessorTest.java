package com.sagag.services.ax.availability;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Seconds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.availability.processor.AustriaArticleAvailabilityProcessor;
import com.sagag.services.ax.enums.AustriaArticleAvailabilityState;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.TourTimeTable;

import lombok.extern.slf4j.Slf4j;

/**
 * UT for AustriaArticleAvailabilityProcessor.
 */
@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class AustriaArticleAvailabilityProcessorTest {

  @InjectMocks
  private AustriaArticleAvailabilityProcessor processor;

  private Availability availability;

  @Before
  public void setup() {
    System.setProperty("user.timezone", "CET");
    // Change NOW to specific time 8/18/2017 CET
    DateTime dateTime = new DateTime().withHourOfDay(18).withMinuteOfHour(1);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);
    DateTime now = new DateTime();
    log.debug("Current date = {} - chronology = {}", now, now.getChronology());

    List<TourTimeTable> tourTimeTables = Lists.newArrayList();

    availability = new Availability();
    availability.setArticleId("1000740841");

    TourTimeTable tourTimeTable = new TourTimeTable();
    tourTimeTable.setTourName("1007_40340_1415");
    tourTimeTable.setStartTime("2017-08-18T12:15:00Z");
    tourTimeTables.add(tourTimeTable);
    tourTimeTable = new TourTimeTable();
    tourTimeTable.setTourName("1007_40340_0800");
    tourTimeTable.setStartTime("2017-08-21T06:00:00Z");
    tourTimeTables.add(tourTimeTable);
    tourTimeTable = new TourTimeTable();
    tourTimeTable.setTourName("1007_40340_1000");
    tourTimeTable.setStartTime("2017-08-21T08:00:00Z");
    tourTimeTables.add(tourTimeTable);
    tourTimeTable = new TourTimeTable();
    tourTimeTable.setTourName("1007_40340_1200");
    tourTimeTable.setStartTime("2017-08-21T10:00:00Z");
    tourTimeTables.add(tourTimeTable);

    availability.setTourTimeTable(tourTimeTables);
  }

  @Test
  public void testGetDefaultResult() {
    final ArticleAvailabilityResult result = processor.getDefaultResult();
    AxDataTestUtils.logObjects(result);
    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.YELLOW.getCode()));
  }

  @Test
  public void testProcess_Green_Pickup_ImmediateDelivery() {
    availability.setImmediateDelivery(true);
    availability.setArrivalTime(DateTime.now().toString());
    final ArticleAvailabilityResult result = processor.process(availability,
        ErpSendMethodEnum.PICKUP, DateTime.now());

    AxDataTestUtils.logObjects(result);

    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.GREEN.getCode()));
  }

  @Test
  public void testProcess_Green_ArrivalTime_Before_Now() {
    availability.setArrivalTime(
        DateTime.now().minusDays(NumberUtils.INTEGER_ONE).toString());
    final ArticleAvailabilityResult result =
        processor.process(availability, ErpSendMethodEnum.TOUR, DateTime.now());

    AxDataTestUtils.logObjects(result);

    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.GREEN.getCode()));
  }

  @Test
  @Ignore("The logic is the same with isDeliveryImmediate")
  public void testProcess_Green_SameDay() {
    availability.setArrivalTime(LocalDate.now().plus(Seconds.THREE).toString());
    final ArticleAvailabilityResult result =
        processor.process(availability, ErpSendMethodEnum.TOUR, DateTime.now());

    AxDataTestUtils.logObjects(result);

    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.GREEN.getCode()));
  }

  @Test
  public void testProcess_YellowGreen_NextWorkingDay() {
    final DateTime requestDate = DateTime.now().plus(Days.ONE);
    availability.setArrivalTime(requestDate.toString());

    final ArticleAvailabilityResult result =
        processor.process(availability, ErpSendMethodEnum.TOUR, requestDate);

    AxDataTestUtils.logObjects(result);

    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.YELLOW_GREEN.getCode()));
  }

  @Test
  public void testProcess_Yellow_WithoutHandledCondition() {
    final DateTime requestDate = DateTime.now().plus(Days.ONE);
    availability.setArrivalTime(requestDate.toString());

    final ArticleAvailabilityResult result =
        processor.process(availability, ErpSendMethodEnum.POST_NORMAL, requestDate);

    AxDataTestUtils.logObjects(result);

    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.YELLOW.getCode()));
  }

  @Test
  public void testProcess_Yellow_With_NoAvailability() {

    final ArticleAvailabilityResult result =
        processor.process(null, ErpSendMethodEnum.PICKUP, DateTime.now());

    AxDataTestUtils.logObjects(result);

    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.YELLOW.getCode()));
  }

  @Test
  public void testProcess_Yellow_With_NoNextWorkingTour() {

    final ArticleAvailabilityResult result =
        processor.process(availability, ErpSendMethodEnum.PICKUP, null);

    AxDataTestUtils.logObjects(result);

    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.YELLOW.getCode()));
  }

  @Test
  public void testProcess_Yellow_With_NoArrivalTime() {

    final ArticleAvailabilityResult result =
        processor.process(availability, ErpSendMethodEnum.PICKUP, DateTime.now());

    AxDataTestUtils.logObjects(result);

    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.YELLOW.getCode()));
  }

  @Test
  public void testProcess_Yellow_With_TrueBackOrder() {
    availability.setBackOrder(true);
    final ArticleAvailabilityResult result =
        processor.process(availability, ErpSendMethodEnum.PICKUP, DateTime.now());

    AxDataTestUtils.logObjects(result);

    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result.getAvailablityStateCode(),
        Matchers.is(AustriaArticleAvailabilityState.IN_144_HOURS.getCode()));
  }

}
