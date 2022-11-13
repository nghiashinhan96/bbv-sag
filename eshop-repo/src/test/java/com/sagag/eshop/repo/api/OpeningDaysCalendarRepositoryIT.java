package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.OpeningDaysCalendar;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.DateUtils;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * Integration test class for {@link OpeningDaysCalendarRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OpeningDaysCalendarRepositoryIT {

  private static final String EXPECTED_DATE = "2019-04-08";

  @Autowired
  private OpeningDaysCalendarRepository openingDaysCalendarRepo;

  @Test
  public void testFindOneById() {
    final Optional<OpeningDaysCalendar> openingDay = openingDaysCalendarRepo.findById(1);
    Assert.assertThat(openingDay.isPresent(), Is.is(true));
    final OpeningDaysCalendar openingDaysCalendar = openingDay.get();
    Assert.assertThat(openingDaysCalendar.getId(), Is.is(1));
    Assert.assertThat(openingDaysCalendar.getCountry().getId(), Is.is(17));
    Assert.assertThat(StringUtils.isBlank(openingDaysCalendar.getExceptions()), Is.is(false));
  }

  @Test
  public void testFindByDateAndCountry() {
    final Date date = DateUtils.toDate("2019-03-25", DateUtils.DEFAULT_DATE_PATTERN);
    final int countryId = 17;
    final Optional<Integer> openingDayId =
        openingDaysCalendarRepo.findByDateAndCountryId(date, countryId);
    Assert.assertThat(openingDayId.isPresent(), Is.is(true));
  }

  @Test
  public void testFindByDateAndCountry_NotFound() {
    final Date date = DateUtils.toDate("2019-03-25", DateUtils.DEFAULT_DATE_PATTERN);
    final int countryId = 20;
    final Optional<Integer> openingDayId =
        openingDaysCalendarRepo.findByDateAndCountryId(date, countryId);
    Assert.assertThat(openingDayId.isPresent(), Is.is(false));
  }

  @Test
  public void testFindByDateAndCountryName() {
    final Date date = DateUtils.toDate("2019-03-25", DateUtils.DEFAULT_DATE_PATTERN);
    final String countryName = "Austria";
    Assert.assertThat(openingDaysCalendarRepo.findIdByDateAndCountryName(date, countryName),
        Is.is(20));
  }

  @Test
  public void testFindByDateAndCountryName_NotFound() {
    final Date date = DateUtils.toDate("2019-11-31", DateUtils.DEFAULT_DATE_PATTERN);
    final String countryName = "Austria";
    Assert.assertNull(openingDaysCalendarRepo.findIdByDateAndCountryName(date, countryName));
  }

  @Test
  public void testCheckExistingByYear() {
    Assert.assertThat(openingDaysCalendarRepo.checkExistingByYear(2019), Is.is(true));
  }

  @Test
  public void testCheckExistingByYear_NotExist() {
    Assert.assertThat(openingDaysCalendarRepo.checkExistingByYear(2018), Is.is(false));
  }

  @Test
  public void testGetNextWorkingDay_NullExp() {
    final Date date = DateUtils.toDate("2019-04-05", DateUtils.DEFAULT_DATE_PATTERN);
    Optional<Date> findNextWorkingDay = openingDaysCalendarRepo
        .findNextWorkingDay(date, "Austria", "Derendinger-Austria", "1006");
    Assert.assertThat(findNextWorkingDay.isPresent(), Is.is(true));
    Assert.assertThat(findNextWorkingDay.get().toString(), Is.is(EXPECTED_DATE));
  }

  @Test
  public void testGetNextWorkingDay() {
    final Date date = DateUtils.toDate("2019-04-05", DateUtils.DEFAULT_DATE_PATTERN);
    Optional<Date> findNextWorkingDay = openingDaysCalendarRepo
        .findNextWorkingDay(date, "Austria", "Derendinger-Austria", "1006");
    Assert.assertThat(findNextWorkingDay.isPresent(), Is.is(true));
    Assert.assertThat(findNextWorkingDay.get().toString(), Is.is(EXPECTED_DATE));
  }

  @Test
  public void testGetNextWorkingDay_AllAff() {
    final Date date = DateUtils.toDate("2019-04-05", DateUtils.DEFAULT_DATE_PATTERN);
    Optional<Date> findNextWorkingDay = openingDaysCalendarRepo
        .findNextWorkingDay(date, "Austria", "Derendinger-Austria", "1002");
    Assert.assertThat(findNextWorkingDay.isPresent(), Is.is(true));
    Assert.assertThat(findNextWorkingDay.get().toString(), Is.is(EXPECTED_DATE));
  }

  @Test
  public void testFindWorkingDayLaterFrom_AllAff() {
    final Date date = DateUtils.toDate("2019-04-05", DateUtils.DEFAULT_DATE_PATTERN);
    Optional<Date> findNextWorkingDay = openingDaysCalendarRepo
        .findWorkingDayLaterFrom(date, "Austria", "Derendinger-Austria", "1002", 0);
    Assert.assertThat(findNextWorkingDay.isPresent(), Is.is(true));
    Assert.assertThat(findNextWorkingDay.get().toString(), Is.is(EXPECTED_DATE));
  }

  @Test
  public void testExistByIdWithTrue() {
    OpeningDaysCalendar entity = new OpeningDaysCalendar();
    entity.setId(1);
    final boolean exists = openingDaysCalendarRepo.exists(Example.of(entity));

    Assert.assertThat(exists, Is.is(true));
  }

  @Test
  public void testExistByIdWithFalse() {
    OpeningDaysCalendar entity = new OpeningDaysCalendar();
    entity.setId(0);
    final boolean exists = openingDaysCalendarRepo.exists(Example.of(entity));

    Assert.assertThat(exists, Is.is(false));
  }
}
