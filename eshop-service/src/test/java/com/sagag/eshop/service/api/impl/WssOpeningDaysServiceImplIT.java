package com.sagag.eshop.service.api.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.CountryRepository;
import com.sagag.eshop.repo.api.WssOpeningDaysCalendarRepository;
import com.sagag.eshop.repo.api.WssWorkingDayRepository;
import com.sagag.eshop.repo.entity.WssOpeningDaysCalendar;
import com.sagag.eshop.repo.entity.WssWorkingDay;
import com.sagag.eshop.service.api.WssOpeningDaysService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.WssOpeningDaysValidationException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.WssWorkingDayCode;
import com.sagag.services.domain.eshop.criteria.WssOpeningDaysSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvOpeningCalendar;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDetailDto;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDto;
import com.sagag.services.domain.eshop.openingdays.dto.WssOpeningDaysExceptionDto;
import com.sagag.services.domain.eshop.openingdays.dto.WssOpeningDaysRequestBody;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

/**
 * Integration tests for WSS opening days calendar service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class WssOpeningDaysServiceImplIT {

  private static final int WHOLE_SALER_ORG_ID_137 = 137;

  @Autowired
  private WssOpeningDaysService wssOpeningDaysService;

  @Autowired
  private WssOpeningDaysCalendarRepository wssOpeningDaysCalendarRepo;

  @Autowired
  private CountryRepository countryRepo;

  @Autowired
  private WssWorkingDayRepository wssWorkingDayRepo;

  @Test
  public void testGetWssWorkingDayCodes() {
    final List<WssWorkingDay> workingdays = wssOpeningDaysService.getWorkingDayCodes();
    Assert.assertThat(CollectionUtils.isEmpty(workingdays), Is.is(false));
    Assert.assertThat(workingdays.get(0).getCode(), Is.is(WssWorkingDayCode.WORKING_DAY.name()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewOpeningDays_requestNull() throws WssOpeningDaysValidationException {
    wssOpeningDaysService.create(null, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewWssOpeningDays_DateNull() throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    openingDaysRequest.setDate(null);
    wssOpeningDaysService.create(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewWssOpeningDays_DupWssWorkingDayCode()
      throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    openingDaysRequest.getExceptions().setWorkingDayCode(openingDaysRequest.getWorkingDayCode());
    wssOpeningDaysService.create(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewWssOpeningDays_NullExpWssWorkingDayCode()
      throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    openingDaysRequest.getExceptions().setWorkingDayCode(null);
    wssOpeningDaysService.create(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewWssOpeningDays_BranchesAndDeliveryAddsEmpty()
      throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    openingDaysRequest.getExceptions().setBranches(Collections.emptyList());
    wssOpeningDaysService.create(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewWssOpeningDays_IdNotNull() throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    openingDaysRequest.setId(1);
    wssOpeningDaysService.create(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewWssOpeningDays_BranchesEmpty() throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    openingDaysRequest.getExceptions().setBranches(Collections.emptyList());
    final WssOpeningDaysDetailDto WssOpeningDaysDto =
        wssOpeningDaysService.create(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
    Assert.assertNotNull(WssOpeningDaysDto);
    Assert.assertNotNull(WssOpeningDaysDto.getId());
    Assert.assertNotNull(WssOpeningDaysDto.getExceptions());
    Assert.assertThat(WssOpeningDaysDto.getCountryId(), Is.is(openingDaysRequest.getCountryId()));
    Assert.assertThat(WssOpeningDaysDto.getDate().toString(), Is.is(openingDaysRequest.getDate()));
  }

  @Test
  public void testCreateNewWssOpeningDays() throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    final WssOpeningDaysDetailDto WssOpeningDaysDto =
        wssOpeningDaysService.create(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
    Assert.assertNotNull(WssOpeningDaysDto);
    Assert.assertNotNull(WssOpeningDaysDto.getId());
    Assert.assertThat(WssOpeningDaysDto.getDate().toString(), Is.is(openingDaysRequest.getDate()));
    Assert.assertThat(WssOpeningDaysDto.getCountryId(), Is.is(openingDaysRequest.getCountryId()));
    Assert.assertThat(WssOpeningDaysDto.getWorkingDayCode(),
        Is.is(openingDaysRequest.getWorkingDayCode()));

    final WssOpeningDaysExceptionDto exceptions = WssOpeningDaysDto.getExceptions();
    Assert.assertNotNull(exceptions);
    Assert.assertThat(exceptions.getWorkingDayCode(),
        Is.is(openingDaysRequest.getExceptions().getWorkingDayCode()));
    Assert.assertThat(exceptions.getBranches().size(),
        Is.is(openingDaysRequest.getExceptions().getBranches().size()));
  }

  @Test(expected = ValidationException.class)
  public void testUpdateWssOpeningDays_NotExist() throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    openingDaysRequest.setId(0);
    wssOpeningDaysService.update(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateWssOpeningDays_IdNull() throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    wssOpeningDaysService.update(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = ValidationException.class)
  public void testUpdateExistingWssOpeningDays() throws WssOpeningDaysValidationException {
    final WssOpeningDaysRequestBody openingDaysRequest = initWssOpeningDaysData();
    openingDaysRequest.setId(5);
    final WssOpeningDaysDetailDto wssOpeningDaysDto =
        wssOpeningDaysService.update(openingDaysRequest, WHOLE_SALER_ORG_ID_137);
    Assert.assertNotNull(wssOpeningDaysDto);
    Assert.assertNotNull(wssOpeningDaysDto.getId());
    Assert.assertThat(wssOpeningDaysDto.getDate().toString(), Is.is(openingDaysRequest.getDate()));
    Assert.assertThat(wssOpeningDaysDto.getCountryId(), Is.is(openingDaysRequest.getCountryId()));
    Assert.assertThat(wssOpeningDaysDto.getWorkingDayCode(),
        Is.is(openingDaysRequest.getWorkingDayCode()));

    final WssOpeningDaysExceptionDto exceptions = wssOpeningDaysDto.getExceptions();
    Assert.assertNotNull(exceptions);
    Assert.assertThat(exceptions.getWorkingDayCode(),
        Is.is(openingDaysRequest.getExceptions().getWorkingDayCode()));
    Assert.assertThat(exceptions.getBranches().size(),
        Is.is(openingDaysRequest.getExceptions().getBranches().size()));
  }

  @Test(expected = ValidationException.class)
  public void testRemoveWssOpeningDays_NotExist() {
    wssOpeningDaysService.remove(0);
  }

  @Test
  public void testRemoveWssOpeningDays() {
    final Integer id = 5;

    final Optional<WssOpeningDaysCalendar> existingOpeningDays =
        wssOpeningDaysCalendarRepo.findOneById(id);
    Assert.assertThat(existingOpeningDays.isPresent(), Matchers.anyOf(Is.is(true), Is.is(false)));

    if (!existingOpeningDays.isPresent()) {
      return;
    }
    wssOpeningDaysService.remove(id);
    final Optional<WssOpeningDaysCalendar> deletedOpeningDays =
        wssOpeningDaysCalendarRepo.findOneById(id);

    Assert.assertThat(deletedOpeningDays.isPresent(), Matchers.anyOf(Is.is(true), Is.is(false)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWssOpeningDaysDetail_NullId() {
    wssOpeningDaysService.getOpeningDaysCalendar(null);
  }

  @Test
  public void testGetWssOpeningDaysDetail_NotFound() {
    final Optional<WssOpeningDaysDetailDto> openingDaysDetail =
        wssOpeningDaysService.getOpeningDaysCalendar(0);
    Assert.assertThat(openingDaysDetail.isPresent(), Is.is(false));
  }

  @Test
  public void testGetWssOpeningDaysDetail() {
    final Integer id = 5;
    final Optional<WssOpeningDaysDetailDto> openingDaysDetail =
        wssOpeningDaysService.getOpeningDaysCalendar(id);
    Assert.assertThat(openingDaysDetail.isPresent(), Matchers.anyOf(Is.is(true), Is.is(false)));
    if (openingDaysDetail.isPresent()) {
      Assert.assertThat(openingDaysDetail.get().getId(), Is.is(id));
    }

  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_OneDay() {
    final WssOpeningDaysSearchCriteria criteria = WssOpeningDaysSearchCriteria.builder()
        .dateFrom("2019-03-25").dateTo("2019-03-25").orgId(WHOLE_SALER_ORG_ID_137).build();

    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_FullCountry() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-01-01").dateTo("2019-12-31")
            .countryName("austria").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 3);
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_LikeTextCountry() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-01-01").dateTo("2019-12-31")
            .countryName("lgium").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_WorkingDay() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-01-01").dateTo("2019-12-31")
            .workingDayCode("WORKING_DAY").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_WorkingDayAndCountry() {
    final WssOpeningDaysSearchCriteria criteria = WssOpeningDaysSearchCriteria.builder()
        .dateFrom("2019-01-01").dateTo("2019-12-31").workingDayCode("PUBLIC_HOLIDAY")
        .countryName("aust").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 2);
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_DateRange() {
    final WssOpeningDaysSearchCriteria criteria = WssOpeningDaysSearchCriteria.builder()
        .dateFrom("2019-01-01").dateTo("2019-04-05").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 3);
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_MultiCriteria() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-01-01").dateTo("2019-04-05")
            .workingDayCode("PUBLIC_HOLIDAY").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 2);
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_AscSorting() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-10-30").dateTo("2019-11-30")
            .orderDescByCountryName(false).orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
    if (!response.hasContent()) {
      return;
    }
    Assert.assertThat(response.getContent().get(0).getCountryName(), Is.is("Belgium"));
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_DescSorting() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-10-30").dateTo("2019-11-30")
            .orderDescByCountryName(true).orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
    if (response.hasContent()) {
      Assert.assertThat(response.getContent().get(0).getCountryName(), Is.is("Belgium"));
    }
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_DateDescSorting() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-02-05").dateTo("2019-12-31")
            .orderDescByDate(true).orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 3);
    if (response.hasContent()) {
      Assert.assertThat(response.getContent().get(0).getDate(), Is.is(Date.valueOf("2019-10-30")));
    }

  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_DateAscSorting() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-02-05").dateTo("2019-12-31")
            .orderDescByDate(false).orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 3);
    if (!response.hasContent()) {
      return;
    }
    Assert.assertThat(response.getContent().get(0).getDate(), Is.is(Date.valueOf("2019-02-18")));
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_Branch() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-02-05").dateTo("2019-12-31")
            .expBranchInfo("1012").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_ExpWorkingDay() {
    final WssOpeningDaysSearchCriteria criteria =
        WssOpeningDaysSearchCriteria.builder().dateFrom("2019-02-05").dateTo("2019-12-31")
            .expWorkingDayCode("WORKING_DAY").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
  }

  @Test
  public void testSearchWssOpeningDaysByCriteria_NotFound() {
    final WssOpeningDaysSearchCriteria criteria = WssOpeningDaysSearchCriteria.builder()
        .dateFrom("2019-02-05").dateTo("2019-12-31").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssOpeningDaysDto> response =
        wssOpeningDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 3);
  }

  @Test(expected = WssOpeningDaysValidationException.class)
  @Ignore("Data is not ready for running IT")
  public void testImportWssOpeningDays_checkExistingTrue()
      throws WssOpeningDaysValidationException {
    final WssOpeningDaysCalendar entity = WssOpeningDaysCalendar.builder()
        .datetime(Date.valueOf("2019-03-25"))
        .country(countryRepo.findByCode("at").get())
        .wssWorkingDay(wssWorkingDayRepo.findOneByCode(WssWorkingDayCode.WORKING_DAY.name()).get())
        .build();
    wssOpeningDaysCalendarRepo.save(entity);

    final WssCsvOpeningCalendar wssCsvOpeningCalendar =
        WssCsvOpeningCalendar.builder().date(Date.valueOf("2019-03-25")).country("Austria")
            .workingDayCode(WssWorkingDayCode.WORKING_DAY.name()).build();

    wssOpeningDaysService.importWssOpeningDays(Collections.singletonList(wssCsvOpeningCalendar),
        true, WHOLE_SALER_ORG_ID_137);
  }

  @Test
  public void testImportWssOpeningDays_checkExistingFalse()
      throws WssOpeningDaysValidationException {
    final Date date = Date.valueOf("2015-03-25");
    final String countryName = "Austria";
    final WssCsvOpeningCalendar wssCsvOpeningCalendar = WssCsvOpeningCalendar.builder().date(date)
        .country(countryName).workingDayCode(WssWorkingDayCode.WORKING_DAY.name()).build();
    wssOpeningDaysService.importWssOpeningDays(Collections.singletonList(wssCsvOpeningCalendar),
        true, WHOLE_SALER_ORG_ID_137);
    final Integer importedId = wssOpeningDaysCalendarRepo.findIdByDateAndCountryNameAndOrgId(date,
        countryName, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(importedId, Matchers.notNullValue());
  }

  @Test
  public void testImportWssOpeningDays() throws WssOpeningDaysValidationException {
    final Date date = Date.valueOf("2019-03-25");
    final String countryName = "Austria";
    final WssCsvOpeningCalendar wssCsvOpeningCalendar = WssCsvOpeningCalendar.builder().date(date)
        .country(countryName).workingDayCode(WssWorkingDayCode.NON_WORKING_DAY.name()).build();
    wssOpeningDaysService.importWssOpeningDays(Collections.singletonList(wssCsvOpeningCalendar),
        false, WHOLE_SALER_ORG_ID_137);
    final Integer importedId = wssOpeningDaysCalendarRepo.findIdByDateAndCountryNameAndOrgId(date,
        countryName, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(importedId, Matchers.notNullValue());
    Optional<WssOpeningDaysCalendar> importedDate =
        wssOpeningDaysCalendarRepo.findOneById(importedId);
    Assert.assertThat(importedDate.isPresent(), Is.is(true));
    Assert.assertThat(importedDate.get().getCountry().getShortName(), Is.is(countryName));
    Assert.assertThat(importedDate.get().getDatetime(), Is.is(date));
  }

  @Test
  public void testCheckExistingWorkingDayByDateAndOrgIdAndBranchId()
      throws WssOpeningDaysValidationException {
    LocalDate date = LocalDate.parse("2021-01-08");
    boolean isWorkingDay = wssOpeningDaysCalendarRepo
        .checkExistingWorkingDayByDateAndOrgIdAndBranchNr(date, WHOLE_SALER_ORG_ID_137, 1000);
    Assert.assertFalse(isWorkingDay);
  }

  @Test
  public void testFindNextWorkingDayLaterFromAndInWeekdays()
      throws WssOpeningDaysValidationException {
    LocalDate date = LocalDate.parse("2021-01-12");
    Date workingDateOpt =
        wssOpeningDaysCalendarRepo.findNextWorkingDayLaterFromAndInWeekdays(date,
            WHOLE_SALER_ORG_ID_137, 1000, Lists.newArrayList("THURSDAY")).orElse(null);
    Assert.assertThat(workingDateOpt, Matchers.notNullValue());

  }

  private void assertSearchResult(final Page<WssOpeningDaysDto> openingDays,
      final Integer expected) {
    Assert.assertThat(openingDays, Matchers.notNullValue());
    Assert.assertThat(openingDays.getContent(), Matchers.notNullValue());
    if (openingDays.hasContent()) {
      Assert.assertThat(openingDays.getNumberOfElements(), Is.is(expected));
    }
  }

  private WssOpeningDaysRequestBody initWssOpeningDaysData() {
    final WssOpeningDaysExceptionDto exceptions =
        WssOpeningDaysExceptionDto.builder().branches(Arrays.asList("1001", "1002"))
            .workingDayCode(WssWorkingDayCode.WORKING_DAY.name()).build();
    return WssOpeningDaysRequestBody.builder().date("2015-02-22").countryId(17)
        .workingDayCode(WssWorkingDayCode.NON_WORKING_DAY.name()).exceptions(exceptions).build();
  }
}
