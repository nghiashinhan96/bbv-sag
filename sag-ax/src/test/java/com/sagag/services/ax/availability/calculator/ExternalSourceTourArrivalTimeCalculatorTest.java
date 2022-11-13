package com.sagag.services.ax.availability.calculator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.ax.availability.tourtime.AxTourTimeTableUtils;
import com.sagag.services.ax.availability.tourtime.NextWorkingDateHelper;
import com.sagag.services.ax.availability.tourtime.impl.AxDefaultTourTimeTableGenerator;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.TourTimeTable;

@RunWith(SpringRunner.class)
public class ExternalSourceTourArrivalTimeCalculatorTest {

  private static final String PICKUP_BRANCH_ID = "123";

  @InjectMocks
  private ExternalSourceTourArrivalTimeCalculator calculator;

  @Mock
  protected NextWorkingDateHelper tourTimeTableHelper;

  @Mock
  private AxDefaultTourTimeTableGenerator axDefTourTimeTableGenerator;

  @Before
  public void setup() {
    System.setProperty("user.timezone", "CET");
  }

  private static Availability createDummyAvailability(String arrivalTime) {
    Availability avaiability = new Availability();
    avaiability.setArrivalTime(arrivalTime);
    avaiability.setExternalSource(true);
    return avaiability;
  }

  private static List<TourTimeTable> createDummyTourTimeList(DateTime arrivalTime) {
    TourTimeTable tour1 = new TourTimeTable();
    tour1.setTourName("123_18_0530");
    tour1.setCutOffMinutes(30);
    tour1.setTourDays("1,2,3,4,5");
    tour1.setTourDepartureTime("04:30");
    tour1.setStartTime(DateUtils.getUTCStr(
        AxTourTimeTableUtils.createDateTimeFromTourName(arrivalTime, tour1.getTourName())));

    TourTimeTable tour2 = SagBeanUtils.map(tour1, TourTimeTable.class);
    tour2.setTourName("123_18_1045");
    tour2.setTourDepartureTime("9:45");
    tour2.setStartTime(DateUtils.getUTCStr(
        AxTourTimeTableUtils.createDateTimeFromTourName(arrivalTime, tour2.getTourName())));

    TourTimeTable tour3 = SagBeanUtils.map(tour1, TourTimeTable.class);
    tour3.setTourName("123_18_1400");
    tour3.setTourDepartureTime("13:00");
    tour3.setStartTime(DateUtils.getUTCStr(
        AxTourTimeTableUtils.createDateTimeFromTourName(arrivalTime, tour3.getTourName())));

    return Arrays.asList(tour1, tour2, tour3);
  }

  @Test
  public void shouldReturnNextTourNextWorkingDate() {
    String arrivalTime = "2022-03-01T09:45:00Z";
    Availability availability = createDummyAvailability(arrivalTime);

    // Tuesday is holiday
    List<TourTimeTable> tourTimeList = createDummyTourTimeList(availability.getCETArrivalTime());
    tourTimeList.forEach(tour -> tour.setTourDays("1,3,4,5,6"));

    Mockito.when(axDefTourTimeTableGenerator
        .generate(Mockito.any(), Mockito.any(),
            Mockito.any(), Mockito.any(), Mockito.any()))
    .thenReturn(tourTimeList);

    final DateTime nextWorkDate = DateTime.now().plusDays(1);

    Mockito.when(tourTimeTableHelper.getNextWorkingDate(Mockito.any(), Mockito.any(),
        Mockito.eq(availability.getCETArrivalTime().toDate()))).thenReturn(nextWorkDate.toDate());

    Mockito.when(axDefTourTimeTableGenerator
        .generate(Mockito.eq(nextWorkDate), Mockito.any(),
            Mockito.any(), Mockito.any(), Mockito.any()))
    .thenReturn(createDummyTourTimeList(nextWorkDate));

    executeCalculatArrivalTime(availability, tourTimeList);
  }

  @Test
  public void shouldReturnNextTourTodayGivenArrivalTimeInLastMinuteOfTourEarliest() {
    String arrivalTime = "2022-03-01T08:45:00Z";
    Availability availability = createDummyAvailability(arrivalTime);

    List<TourTimeTable> tourTimeList = createDummyTourTimeList(availability.getCETArrivalTime());

    Mockito.when(axDefTourTimeTableGenerator
        .generate(Mockito.any(), Mockito.any(),
            Mockito.any(), Mockito.any(), Mockito.any()))
    .thenReturn(tourTimeList);

    executeCalculatArrivalTime(availability, tourTimeList);
  }

  private void executeCalculatArrivalTime(Availability availability,
      List<TourTimeTable> tourTimeList) {
    SupportedAffiliate affiliate = SupportedAffiliate.SAG_CZECH;
    String pickupBranchId = PICKUP_BRANCH_ID;
    String countryName = StringUtils.EMPTY;
    Date nextWrkgDate = DateTime.now().toDate();

    boolean actual = calculator.calculateArrivalTime(availability, Optional.of(nextWrkgDate),
        affiliate, pickupBranchId, Collections.emptyList(), Collections.emptyList(), countryName);

    System.out.println(SagJSONUtil.convertObjectToPrettyJson(availability));
    Assert.assertThat(actual, Matchers.is(true));
  }

}
