package com.sagag.services.ax.availability;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.availability.backorder.DefaultBackOrderAvailabilityHandler;
import com.sagag.services.ax.availability.backorder.TourTimeTableBackOrderAvailabilityHandler;
import com.sagag.services.ax.availability.calculator.ArrivalTimeCalculator;
import com.sagag.services.ax.availability.filter.AxAvailabilityFilter;
import com.sagag.services.ax.availability.processor.AxArticleAvailabilityProcessor;
import com.sagag.services.ax.availability.tourtime.NextWorkingDateHelper;
import com.sagag.services.ax.enums.AustriaArticleAvailabilityState;
import com.sagag.services.ax.exception.WorkingDayNotFoundException;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysAvailabilityInfo;
import com.sagag.services.domain.sag.erp.Availability;

/**
 * UT for AxAvailabilityFilter.
 */
@RunWith(SpringRunner.class)
public class AxAvailabilityFilterTest extends AbstractAxAvailabilityTest {

  private static final String FIXED_TODAY = "2018-06-25T00:00:00Z";

  private static final String COUNTRY_NAME = AxDataTestUtils.COUNTRY_NAME_AT;

  @InjectMocks
  private AxAvailabilityFilter filter;

  @Mock
  private AxArticleAvailabilityProcessor availabilityProcessor;

  @Mock
  private DefaultBackOrderAvailabilityHandler defaultBackOrderAvailabilityHandler;

  @Mock
  private TourTimeTableBackOrderAvailabilityHandler tourTimeTableBackOrderAvailabilityHandler;

  @Mock
  private ArticleExternalService articleExtService;

  @Mock
  private NextWorkingDateHelper tourTimeTableHelper;

  @Mock
  private List<ArrivalTimeCalculator> arrivalTimeCalculators;

  private void initForAT(final ArticleAvailabilityResult expectedResult) {
    System.setProperty("user.timezone", "CET");

    Mockito.doCallRealMethod().when(availabilityProcessor).validArrivalTimePredicatce();
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_1() {
    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    article.setAvailabilities(AxDataTestUtils.availabilities());
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    AxDataTestUtils.logObjects(availabilities);
    Assert.assertNotNull(availabilities);
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_2() {
    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    article.setAvailabilities(AxDataTestUtils.availabilities());
    article.setPrice(AxDataTestUtils.articlePrice());
    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    AxDataTestUtils.logObjects(availabilities);
    Assert.assertNotNull(availabilities);
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_normal_workinghours_BACKORDER() {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(8).withMinuteOfHour(1);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    ArticleAvailabilityResult expectedResult =
        AustriaArticleAvailabilityState.IN_144_HOURS.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils.buildPickupArticleSearchCriteria(
        AxDataTestUtils.buildNextWorkingDates("2017-12-20T00:00:00Z"));

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(false);
    availability.setBackOrder(true);
    availability.setArrivalTime("1900-12-20T00:00:00Z");
    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    when(defaultBackOrderAvailabilityHandler.handle(any(), any())).thenReturn(true);
    availability.setAvailState(AustriaArticleAvailabilityState.IN_144_HOURS.getCode());
    availability.setAvailStateColor(AustriaArticleAvailabilityState.IN_144_HOURS.name());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);
    verify(axAustriaHolidaysChecker, never())
        .isNonWorkingDay(any(OpeningDaysAvailabilityInfo.class));
    Availability availability1 = availabilities.get(0);
    Assert.assertNotEquals(DateTime.now().plusDays(1),
        new DateTime(availability1.getArrivalTime()));

    Assert.assertThat("do not show sofort", availability1.isSofort(), is(false));
    Assert.assertThat("incorrect avail state", availability1.getAvailState(),
        is(AustriaArticleAvailabilityState.IN_144_HOURS.getCode()));
    Assert.assertThat("incorrect color expression", availability1.getAvailStateColor(),
        is(AustriaArticleAvailabilityState.IN_144_HOURS.name()));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_normal_workinghours() {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(8).withMinuteOfHour(1);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    when(availabilityProcessor.isDeliveryImmediate(eq(ErpSendMethodEnum.PICKUP),
        any(DateTime.class), eq(true))).thenReturn(true);
    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(true);
    availability.setBackOrder(false);
    availability.setArrivalTime("2017-12-20T00:00:00Z");
    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);
    Availability availability1 = availabilities.get(0);
    Assert.assertThat("original arrival time changed !", availability1.getArrivalTime(),
        is("2017-12-20T00:00:00Z"));
    Assert.assertNotEquals(DateTime.now().plusDays(1),
        new DateTime(availability1.getArrivalTime()));
    Assert.assertNotEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability1.getArrivalTime()).toLocalTime().getHourOfDay());
    Assert.assertThat("show sofort as normal", availability1.isSofort(), is(true));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_closeOfBusinessTime18PM()
      throws WorkingDayNotFoundException {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(18).withMinuteOfHour(1);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    when(availabilityProcessor.isDeliveryImmediate(eq(ErpSendMethodEnum.PICKUP),
        any(DateTime.class), eq(true))).thenReturn(true);
    mockCalculateIfOutsideWorkingHoursSofort();

    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(true);
    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);
    Availability availability1 = availabilities.get(0);
    DateTime.now().plusDays(1).equals(new DateTime(availability1.getArrivalTime()));
    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability1.getArrivalTime()).toLocalTime().getHourOfDay());
    Assert.assertThat("time adjusted, not show Sofort", availability1.isSofort(), is(false));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_closeOfBusinessTime18PM_NonSofort()
      throws WorkingDayNotFoundException {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(18).withMinuteOfHour(1);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    mockCalculateIfOutsideWorkingHoursNonSofort();

    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(false);
    DateTime mockedAXArrivalTimeOutOfHoursTooLate =
        new DateTime().withHourOfDay(18).withMinuteOfHour(15);
    availability.setArrivalTime(
        AxDataTestUtils.dtfOut.withZoneUTC().print(mockedAXArrivalTimeOutOfHoursTooLate));
    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);
    verify(axAustriaHolidaysChecker, never())
        .isNonWorkingDay(any(OpeningDaysAvailabilityInfo.class));
    Availability availability1 = availabilities.get(0);
    DateTime.now().plusDays(1).equals(new DateTime(availability1.getArrivalTime()));
    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability1.getArrivalTime()).toLocalTime().getHourOfDay());
    Assert.assertThat("time adjusted, not show Sofort", availability1.isSofort(), is(false));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_openTime8AM_workingday()
      throws WorkingDayNotFoundException {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(7).withMinuteOfHour(59);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    when(availabilityProcessor.isDeliveryImmediate(eq(ErpSendMethodEnum.PICKUP),
        any(DateTime.class), eq(true))).thenReturn(true);
    mockCalculateIfOutsideWorkingHoursSofort();

    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(true);
    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);

    Availability availability1 = availabilities.get(0);
    DateTime.now().equals(new DateTime(availability1.getArrivalTime()));
    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability1.getArrivalTime()).toLocalTime().getHourOfDay());
    Assert.assertThat("time adjusted, not show Sofort", availability1.isSofort(), is(false));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_openTime8AM_workingday_NonSofort()
      throws WorkingDayNotFoundException {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(7).withMinuteOfHour(59);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    mockCalculateIfOutsideWorkingHoursNonSofort();

    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(false);
    DateTime mockedAXArrivalTimeOutOfHoursTooEarly =
        new DateTime().withHourOfDay(7).withMinuteOfHour(59);
    availability.setArrivalTime(
        AxDataTestUtils.dtfOut.withZoneUTC().print(mockedAXArrivalTimeOutOfHoursTooEarly));
    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);

    Availability availability1 = availabilities.get(0);
    DateTime.now().equals(new DateTime(availability1.getArrivalTime()));
    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability1.getArrivalTime()).toLocalTime().getHourOfDay());
    Assert.assertThat("time adjusted, not show Sofort", availability1.isSofort(), is(false));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_openTime8AM_workingday_NonSofort_notTODAY()
      throws WorkingDayNotFoundException {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(7).withMinuteOfHour(59);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    Mockito.doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      DateTime mockedOpeningTime = DateTime.now().plusDays(1)
          .withTime(AxDataTestUtils.mockWorkingHours().get(0).getOpeningTime());
      Availability newAvailability = (Availability) args[0];
      newAvailability.setArrivalTime(AxDataTestUtils.dtfOut.withZoneUTC().print(mockedOpeningTime));
      newAvailability.setSofort(false);
      return null;
    }).when(arrivalTimeCalculator).calculateArrivalTime(Mockito.any(Availability.class),
        Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.any(), Mockito.any(),
        Mockito.any());

    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(false);
    final String RANDOM_RESPONSE_ARRIVAL_TIME = "2018-06-26T04:55:00Z";
    availability.setArrivalTime(RANDOM_RESPONSE_ARRIVAL_TIME);

    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);

    Availability availability1 = availabilities.get(0);
    DateTime.now().equals(new DateTime(availability1.getArrivalTime()));
    final String ADJUSTED_RESPONSE_ARRIVAL_TIME = "2018-06-26T06:00:00Z";
    Assert.assertEquals(ADJUSTED_RESPONSE_ARRIVAL_TIME, availability1.getArrivalTime());
    Assert.assertThat("time adjusted, not show Sofort", availability1.isSofort(), is(false));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_closeOfBusinessTime18PM_NonSofort_notTODAY()
      throws WorkingDayNotFoundException {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(18).withMinuteOfHour(1);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    final String randomResponseArrivalTimeAfter = "2018-06-26T16:5:00Z";

    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1");
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(false);
    availability.setArrivalTime(randomResponseArrivalTimeAfter);
    mockCalculateIfOutsideWorkingHoursNonSofort();

    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);

    Availability availability1 = availabilities.get(0);
    Assert.assertThat("time adjusted, not show Sofort", availability1.isSofort(), is(false));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_closeOfBusinessTime18PM_NonSofort_notTODAY_normal() {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(18).withMinuteOfHour(1);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    final String RANDOM_RESPONSE_ARRIVAL_TIME_NORMAL = "2018-06-26T06:5:00Z";

    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(false);
    availability.setArrivalTime(RANDOM_RESPONSE_ARRIVAL_TIME_NORMAL);
    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);

    Availability availability1 = availabilities.get(0);

    Assert.assertEquals(RANDOM_RESPONSE_ARRIVAL_TIME_NORMAL, availability1.getArrivalTime());
    Assert.assertThat("time adjusted, not show Sofort", availability1.isSofort(), is(false));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_openTime8AM_Weekends()
      throws WorkingDayNotFoundException {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(7).withMinuteOfHour(59);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);

    when(availabilityProcessor.isDeliveryImmediate(eq(ErpSendMethodEnum.PICKUP),
        any(DateTime.class), eq(true))).thenReturn(true);
    mockCalculateIfOutsideWorkingHoursSofort();

    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(true);
    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);

    verify(axAustriaHolidaysChecker, never())
        .isNonWorkingDay(any(OpeningDaysAvailabilityInfo.class));

    Availability availability1 = availabilities.get(0);
    DateTime.now().plusDays(1).equals(new DateTime(availability1.getArrivalTime()));
    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability1.getArrivalTime()).toLocalTime().getHourOfDay());
    Assert.assertThat("time adjusted, not show Sofort", availability1.isSofort(), is(false));
  }

  @Test
  public void testFilterAvailabilities_With_AT_PickupMethod_OutsideWorkingHoursCalculator_openTime8AM_Holidays()
      throws WorkingDayNotFoundException {
    System.setProperty("user.timezone", "CET");
    DateTime dateTime = new DateTime(FIXED_TODAY).withHourOfDay(7).withMinuteOfHour(59);
    long millis = dateTime.getMillis();
    DateTimeUtils.setCurrentMillisFixed(millis);



    when(availabilityProcessor.isDeliveryImmediate(eq(ErpSendMethodEnum.PICKUP),
        any(DateTime.class), eq(true))).thenReturn(true);
    mockCalculateIfOutsideWorkingHoursSofort();

    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildPickupArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    List<Availability> default_availabilities = AxDataTestUtils.availabilities();
    Availability availability = default_availabilities.get(0);
    availability.setImmediateDelivery(true);
    article.setAvailabilities(default_availabilities);
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    Assert.assertNotNull(availabilities);

    Availability availability1 = availabilities.get(0);
    DateTime.now().plusDays(1).equals(new DateTime(availability1.getArrivalTime()));
    Assert.assertEquals(AxDataTestUtils.OPEN_HOUR_8,
        new DateTime(availability1.getArrivalTime()).toLocalTime().getHourOfDay());
    Assert.assertThat("time adjusted, not show Sofort", availability1.isSofort(), is(false));
  }

  @Test
  public void testFilterAvailabilities_With_NormalCase() {
    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildTourArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    article.setAvailabilities(AxDataTestUtils.availabilitiesWithTour());
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    AxDataTestUtils.logObjects(availabilities);
    Assert.assertNotNull(availabilities);
  }

  @Test
  public void testGetBeforeFirstTourToday() {
    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildTourArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    article.setAvailabilities(AxDataTestUtils.availabilitiesWithTour());
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    AxDataTestUtils.logObjects(availabilities);
    Assert.assertNotNull(availabilities);
  }

  @Test
  public void testGetInWeekend() {
    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildTourArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    article.setAvailabilities(AxDataTestUtils.availabilitiesWithTour());
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    AxDataTestUtils.logObjects(availabilities);
    Assert.assertNotNull(availabilities);
  }

  @Test
  public void testGetInHolidays() {
    ArticleAvailabilityResult expectedResult = AustriaArticleAvailabilityState.YELLOW.toResult();
    initForAT(expectedResult);
    final ArticleSearchCriteria criteria = AxDataTestUtils
        .buildTourArticleSearchCriteria(AxDataTestUtils.buildFixedNextWorkingDates());

    final ArticleDocDto article = new ArticleDocDto();
    article.setAvailabilities(AxDataTestUtils.availabilitiesWithTour());
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    AxDataTestUtils.logObjects(availabilities);
    Assert.assertNotNull(availabilities);
  }

  private void mockCalculateIfOutsideWorkingHoursSofort() throws WorkingDayNotFoundException {
    Mockito.doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      DateTime mockedOpeningTime =
          DateTime.now().withTime(AxDataTestUtils.mockWorkingHours().get(0).getOpeningTime());
      Availability newAvailability = (Availability) args[0];
      newAvailability.setArrivalTime(AxDataTestUtils.dtfOut.withZoneUTC().print(mockedOpeningTime));
      newAvailability.setSofort(false);
      return null;
    }).when(arrivalTimeCalculator).calculateArrivalTime(Mockito.any(Availability.class),
        Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.any(), Mockito.any(),
        Mockito.any());
  }

  private void mockCalculateIfOutsideWorkingHoursNonSofort() throws WorkingDayNotFoundException {
    Mockito.doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      DateTime mockedOpeningTime =
          DateTime.now().withTime(AxDataTestUtils.mockWorkingHours().get(0).getOpeningTime());
      Availability newAvailability = (Availability) args[0];
      newAvailability.setArrivalTime(AxDataTestUtils.dtfOut.withZoneUTC().print(mockedOpeningTime));
      newAvailability.setSofort(false);
      return null;
    }).when(arrivalTimeCalculator).calculateArrivalTime(Mockito.any(Availability.class),
        Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.any(), Mockito.any(),
        Mockito.any());
  }

}
