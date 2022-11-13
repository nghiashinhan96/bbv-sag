package com.sagag.services.ax.holidays.impl;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.holidays.AxHolidaysChecker;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysAvailabilityInfo;

/**
 * IT to verify {@link AxDefaultHolidaysCheckerImplIT}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@EshopIntegrationTest
public class AxDefaultHolidaysCheckerImplIT {

  private static final String COUNTRY_NAME = AxDataTestUtils.COUNTRY_NAME_AT;

  @Autowired
  private AxHolidaysChecker axHolidaysChecker;

  @Test
  public void testCheckNonWorkingDay_NullExp() {
    Assert.assertThat(
        axHolidaysChecker.isNonWorkingDay(initOpeningDaysAvailInfoWithDate("2019-02-19")),
        Is.is(false));
  }

  @Test
  public void testCheckNonWorkingDay_DiffAffiliate() {
    Assert.assertThat(
        axHolidaysChecker.isNonWorkingDay(initOpeningDaysAvailInfoWithDate("2019-02-18")),
        Is.is(false));
  }

  @Test
  public void testCheckNonWorkingDay_DiffBranch() {
    final OpeningDaysAvailabilityInfo info = initOpeningDaysAvailInfoWithDate("2019-11-30");
    info.setPickupBranchId("1234");
    Assert.assertThat(axHolidaysChecker.isNonWorkingDay(info), Is.is(true));
  }

  @Test
  public void testCheckNonWorkingDay() {
    final OpeningDaysAvailabilityInfo info = initOpeningDaysAvailInfoWithDate("2019-11-30");
    Assert.assertThat(axHolidaysChecker.isNonWorkingDay(info), Is.is(false));
  }

  private OpeningDaysAvailabilityInfo initOpeningDaysAvailInfoWithDate(final String date) {
    return OpeningDaysAvailabilityInfo.builder().affiliate(SupportedAffiliate.DERENDINGER_AT)
        .pickupBranchId("1001").date(DateUtils.toDate(date, DateUtils.DEFAULT_DATE_PATTERN))
        .countryName(COUNTRY_NAME)
        .build();
  }
}
