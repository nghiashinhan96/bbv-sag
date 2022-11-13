package com.sagag.services.ax.availability;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.availability.tourtime.impl.AxBackOrderTourTimeTableGenerator;
import com.sagag.services.ax.holidays.impl.AxDefaultHolidaysCheckerImpl;
import com.sagag.services.ax.utils.AxConstants;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.sag.erp.TourTimeTable;

/**
 * UT to verify {@link AxBackOrderTourTimeTableGenerator}.
 *
 */
@EshopMockitoJUnitRunner
public class AxTourTimeTableGeneratorTest {

  private static final String COUNTRY_NAME = AxDataTestUtils.COUNTRY_NAME_AT;

  @InjectMocks
  private AxBackOrderTourTimeTableGenerator generator;

  @Mock
  private AxDefaultHolidaysCheckerImpl axAustriaHolidaysChecker;

  @Mock
  private ArticleExternalService articleExtService;

  private SupportedAffiliate affiliate;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Before
  public void initFirst() {
    System.setProperty("user.timezone", "CET");
    affiliate = SupportedAffiliate.DERENDINGER_AT;
    Mockito.when(articleExtService.getNextWorkingDate(affiliate.getCompanyName(),
        Mockito.eq(Mockito.anyString()), Mockito.eq(Calendar.getInstance().getTime())))
    .thenReturn(Optional.of(Calendar.getInstance().getTime()));
  }

  @Test
  public void shouldReturnEmptyData() {
    final DateTime nextWorkingDate = DateTime.parse("2018-03-12T00:00:00");
    final List<TourTimeDto> tourTimeList = new ArrayList<>();
    final String branchId = StringUtils.EMPTY;

    List<TourTimeTable> tourTimeTable =
        generator.generate(nextWorkingDate, tourTimeList, branchId, affiliate, COUNTRY_NAME);
    AxDataTestUtils.logObjects(tourTimeTable);
    Assert.assertNotNull(tourTimeTable);
  }

  @Test
  public void shouldReturnDataWith_BeforeSecondTour() {
    final DateTime nextWorkingDate = DateTime.parse("2018-04-26T00:30:00");
    final List<TourTimeDto> tourTimeList = AxDataTestUtils.getDummyTourTimeList();
    final String branchId = AxConstants.DEFAULT_BRANCH_ID;

    Mockito.when(axAustriaHolidaysChecker.isNonWorkingDay(Mockito.any())).thenReturn(false);

    List<TourTimeTable> tourTimeTable =
        generator.generate(nextWorkingDate, tourTimeList, branchId, affiliate, COUNTRY_NAME);
    AxDataTestUtils.logObjects(tourTimeTable);
    Assert.assertNotNull(tourTimeTable);
  }

  @Test
  public void shouldReturnDataWith_BeforeSecondTour_WithHolidays() {
    final DateTime nextWorkingDate = DateTime.parse("2018-04-03T00:30:00");
    final List<TourTimeDto> tourTimeList = AxDataTestUtils.getDummyTourTimeList();
    final String branchId = AxConstants.DEFAULT_BRANCH_ID;

    Mockito.when(axAustriaHolidaysChecker.isNonWorkingDay(Mockito.any())).thenReturn(false);

    List<TourTimeTable> tourTimeTable =
        generator.generate(nextWorkingDate, tourTimeList, branchId, affiliate, COUNTRY_NAME);
    AxDataTestUtils.logObjects(tourTimeTable);
    Assert.assertNotNull(tourTimeTable);
  }

  @Test
  public void shouldReturnDataWith_BeforeThirdTour_WithHolidays() {
    final DateTime nextWorkingDate = DateTime.parse("2018-04-03T00:30:00");
    final List<TourTimeDto> tourTimeList = AxDataTestUtils.getDummyTourTimeList();
    final String branchId = AxConstants.DEFAULT_BRANCH_ID;

    Mockito.when(axAustriaHolidaysChecker.isNonWorkingDay(Mockito.any())).thenReturn(false);

    List<TourTimeTable> tourTimeTable =
        generator.generate(nextWorkingDate, tourTimeList, branchId, affiliate, COUNTRY_NAME);
    AxDataTestUtils.logObjects(tourTimeTable);
    Assert.assertNotNull(tourTimeTable);
  }

  @Test
  public void shouldReturnDataWith_BeforeThirdTour() {
    final DateTime nextWorkingDate = DateTime.parse("2018-03-30T00:30:00");
    final List<TourTimeDto> tourTimeList = AxDataTestUtils.getDummyTourTimeList();
    final String branchId = AxConstants.DEFAULT_BRANCH_ID;

    Mockito.when(axAustriaHolidaysChecker.isNonWorkingDay(Mockito.any())).thenReturn(false);

    List<TourTimeTable> tourTimeTable =
        generator.generate(nextWorkingDate, tourTimeList, branchId, affiliate, COUNTRY_NAME);
    AxDataTestUtils.logObjects(tourTimeTable);
    Assert.assertNotNull(tourTimeTable);
  }

  @Test
  public void shouldReturnDataWith_BeforeFirstTour() {
    final DateTime nextWorkingDate = DateTime.parse("2018-04-27T00:00:00");
    final List<TourTimeDto> tourTimeList = AxDataTestUtils.getDummyTourTimeList();
    final String branchId = AxConstants.DEFAULT_BRANCH_ID;

    Mockito.when(axAustriaHolidaysChecker.isNonWorkingDay(Mockito.any())).thenReturn(false);

    List<TourTimeTable> tourTimeTable =
        generator.generate(nextWorkingDate, tourTimeList, branchId, affiliate, COUNTRY_NAME);
    AxDataTestUtils.logObjects(tourTimeTable);
    Assert.assertNotNull(tourTimeTable);
  }

  @Test
  public void shouldReturnDataWith_AfterLastTour() {
    final DateTime nextWorkingDate = DateTime.parse("2018-04-27T00:00:00");
    final DateTime expectedNextOfNextWorkingDateTime = DateTime.parse("2018-04-28T00:00:00Z");
    final List<TourTimeDto> tourTimeList = AxDataTestUtils.getDummyTourTimeList();
    final String branchId = AxConstants.DEFAULT_BRANCH_ID;

    // Because data test has last tour time is 16:30:00
    Mockito.when(axAustriaHolidaysChecker.isNonWorkingDay(Mockito.any())).thenReturn(false);
    Mockito.when(articleExtService.getNextWorkingDate(affiliate.getCompanyName(), branchId,
        nextWorkingDate.toDate())).thenReturn(Optional.of(expectedNextOfNextWorkingDateTime.toDate()));

    List<TourTimeTable> tourTimeTable =
        generator.generate(nextWorkingDate, tourTimeList, branchId, affiliate, COUNTRY_NAME);
    AxDataTestUtils.logObjects(tourTimeTable);
    Assert.assertNotNull(tourTimeTable);
  }

  @Test
  public void shouldReturnDataWith_Saturday() {
    System.setProperty("user.timezone", "CET");

    final DateTime nextWorkingDate = DateTime.parse("2018-03-12T00:00:00");
    final List<TourTimeDto> tourTimeList = AxDataTestUtils.getDummyTourTimeList();
    final String branchId = AxConstants.DEFAULT_BRANCH_ID;

    Mockito.when(axAustriaHolidaysChecker.isNonWorkingDay(Mockito.any())).thenReturn(true);

    List<TourTimeTable> tourTimeTable =
        generator.generate(nextWorkingDate, tourTimeList, branchId, affiliate, COUNTRY_NAME);
    AxDataTestUtils.logObjects(tourTimeTable);
    Assert.assertNotNull(tourTimeTable);
  }

  @Test
  public void shouldReturnDataWith_Holidays() {
    final DateTime nextWorkingDate = DateTime.parse("2018-04-16T00:00:00");
    final List<TourTimeDto> tourTimeList = AxDataTestUtils.getDummyTourTimeList();
    final String branchId = AxConstants.DEFAULT_BRANCH_ID;

    Mockito.when(axAustriaHolidaysChecker.isNonWorkingDay(Mockito.any())).thenReturn(true);

    List<TourTimeTable> tourTimeTable =
        generator.generate(nextWorkingDate, tourTimeList, branchId, affiliate, COUNTRY_NAME);
    AxDataTestUtils.logObjects(tourTimeTable);
    Assert.assertNotNull(tourTimeTable);
  }

}
