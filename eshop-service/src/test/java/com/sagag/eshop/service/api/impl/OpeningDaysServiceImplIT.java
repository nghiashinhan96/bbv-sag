package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.eshop.repo.entity.OpeningDaysCalendar;
import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.eshop.service.api.OpeningDaysService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.CsvOpeningCalendar;
import com.sagag.eshop.service.dto.OpeningDaysDetailDto;
import com.sagag.eshop.service.dto.OpeningDaysDto;
import com.sagag.eshop.service.exception.OpeningDaysValidationException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.WorkingDayCode;
import com.sagag.services.domain.eshop.criteria.OpeningDaysSearchCriteria;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysExceptionDto;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysRequestBody;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests for opening days calendar service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class OpeningDaysServiceImplIT {

  @Autowired
  private OpeningDaysService openingDaysService;

  @Autowired
  private OpeningDaysCalendarRepository openingDaysCalendarRepo;

  @Test
  public void testGetWorkingDayCodes() {
    final List<WorkingDay> workingdays = openingDaysService.getWorkingDayCodes();
    Assert.assertThat(CollectionUtils.isEmpty(workingdays), Is.is(false));
    Assert.assertThat(workingdays.get(0).getCode(), Is.is(WorkingDayCode.WORKING_DAY.name()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewOpeningDays_requestNull() throws OpeningDaysValidationException {
    openingDaysService.create(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewOpeningDays_DateNull() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.setDate(null);
    openingDaysService.create(openingDaysRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewOpeningDays_DupWorkingDayCode() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.getExceptions().setWorkingDayCode(openingDaysRequest.getWorkingDayCode());
    openingDaysService.create(openingDaysRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewOpeningDays_NullExpWorkingDayCode()
      throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.getExceptions().setWorkingDayCode(null);
    openingDaysService.create(openingDaysRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewOpeningDays_BranchesAndDeliveryAddsEmpty()
      throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.getExceptions().setBranches(Collections.emptyList());
    openingDaysRequest.getExceptions().setDeliveryAdrressIDs(Collections.emptyList());
    openingDaysService.create(openingDaysRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewOpeningDays_IdNotNull() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.setId(1);
    openingDaysService.create(openingDaysRequest);
  }

  @Test
  public void testCreateNewOpeningDays_BranchesEmpty() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.getExceptions().setBranches(Collections.emptyList());
    final OpeningDaysDetailDto openingDaysDto = openingDaysService.create(openingDaysRequest);
    Assert.assertNotNull(openingDaysDto);
    Assert.assertNotNull(openingDaysDto.getId());
    Assert.assertNotNull(openingDaysDto.getExceptions());
    Assert.assertThat(openingDaysDto.getExceptions().getDeliveryAdrressIDs().size(),
        Is.is(openingDaysRequest.getExceptions().getDeliveryAdrressIDs().size()));
    Assert.assertThat(openingDaysDto.getCountryId(), Is.is(openingDaysRequest.getCountryId()));
    Assert.assertThat(openingDaysDto.getDate().toString(), Is.is(openingDaysRequest.getDate()));
  }

  @Test
  public void testCreateNewOpeningDays() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    final OpeningDaysDetailDto openingDaysDto = openingDaysService.create(openingDaysRequest);
    Assert.assertNotNull(openingDaysDto);
    Assert.assertNotNull(openingDaysDto.getId());
    Assert.assertThat(openingDaysDto.getDate().toString(), Is.is(openingDaysRequest.getDate()));
    Assert.assertThat(openingDaysDto.getCountryId(), Is.is(openingDaysRequest.getCountryId()));
    Assert.assertThat(openingDaysDto.getWorkingDayCode(),
        Is.is(openingDaysRequest.getWorkingDayCode()));

    final OpeningDaysExceptionDto exceptions = openingDaysDto.getExceptions();
    Assert.assertNotNull(exceptions);
    Assert.assertThat(exceptions.getDeliveryAdrressIDs().size(),
        Is.is(openingDaysRequest.getExceptions().getDeliveryAdrressIDs().size()));
    Assert.assertThat(exceptions.getAffiliate(),
        Is.is(openingDaysRequest.getExceptions().getAffiliate()));
    Assert.assertThat(exceptions.getWorkingDayCode(),
        Is.is(openingDaysRequest.getExceptions().getWorkingDayCode()));
    Assert.assertThat(exceptions.getBranches().size(),
        Is.is(openingDaysRequest.getExceptions().getBranches().size()));
  }

  @Test(expected = ValidationException.class)
  public void testUpdateOpeningDays_NotExist() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.setId(0);
    openingDaysService.update(openingDaysRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateOpeningDays_IdNull() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysService.update(openingDaysRequest);
  }

  @Test
  public void testUpdateExistingOpeningDays() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.setId(1);
    final OpeningDaysDetailDto openingdaysDto = openingDaysService.update(openingDaysRequest);
    Assert.assertNotNull(openingdaysDto);
    Assert.assertNotNull(openingdaysDto.getId());
    Assert.assertThat(openingdaysDto.getDate().toString(), Is.is(openingDaysRequest.getDate()));
    Assert.assertThat(openingdaysDto.getCountryId(), Is.is(openingDaysRequest.getCountryId()));
    Assert.assertThat(openingdaysDto.getWorkingDayCode(),
        Is.is(openingDaysRequest.getWorkingDayCode()));

    final OpeningDaysExceptionDto exceptions = openingdaysDto.getExceptions();
    Assert.assertNotNull(exceptions);
    Assert.assertThat(exceptions.getDeliveryAdrressIDs().size(),
        Is.is(openingDaysRequest.getExceptions().getDeliveryAdrressIDs().size()));
    Assert.assertThat(exceptions.getAffiliate(),
        Is.is(openingDaysRequest.getExceptions().getAffiliate()));
    Assert.assertThat(exceptions.getWorkingDayCode(),
        Is.is(openingDaysRequest.getExceptions().getWorkingDayCode()));
    Assert.assertThat(exceptions.getBranches().size(),
        Is.is(openingDaysRequest.getExceptions().getBranches().size()));
  }

  @Test(expected = ValidationException.class)
  public void testRemoveOpeningDays_NotExist() {
    openingDaysService.remove(0);
  }

  @Test
  public void testRemoveOpeningDays() {
    final Integer id = 1;

    final Optional<OpeningDaysCalendar> existingOpeningDays =
        openingDaysCalendarRepo.findOneById(id);
    Assert.assertThat(existingOpeningDays.isPresent(), Is.is(true));

    openingDaysService.remove(id);
    final Optional<OpeningDaysCalendar> deletedOpeningDays =
        openingDaysCalendarRepo.findOneById(id);

    Assert.assertThat(deletedOpeningDays.isPresent(), Is.is(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetOpeningDaysDetail_NullId() {
    openingDaysService.getOpeningDaysCalendar(null);
  }

  @Test
  public void testGetOpeningDaysDetail_NotFound() {
    final Optional<OpeningDaysDetailDto> openingDaysDetail =
        openingDaysService.getOpeningDaysCalendar(0);
    Assert.assertThat(openingDaysDetail.isPresent(), Is.is(false));
  }

  @Test
  public void testGetOpeningDaysDetail() {
    final Integer id = 1;
    final Optional<OpeningDaysDetailDto> openingDaysDetail =
        openingDaysService.getOpeningDaysCalendar(id);
    Assert.assertThat(openingDaysDetail.isPresent(), Is.is(true));
    Assert.assertThat(openingDaysDetail.get().getId(), Is.is(id));
  }

  @Test
  public void testSearchOpeningDaysByCriteria_OneDay() {
    final OpeningDaysSearchCriteria criteria =
        OpeningDaysSearchCriteria.builder().dateFrom("2019-03-25").dateTo("2019-03-25").build();

    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
  }

  @Test
  public void testSearchOpeningDaysByCriteria_FullCountry() {
    final OpeningDaysSearchCriteria criteria = OpeningDaysSearchCriteria.builder()
        .dateFrom("2019-01-01").dateTo("2019-12-31").countryName("austria").build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 10);
  }

  @Test
  public void testSearchOpeningDaysByCriteria_LikeTextCountry() {
    final OpeningDaysSearchCriteria criteria = OpeningDaysSearchCriteria.builder()
        .dateFrom("2019-01-01").dateTo("2019-12-31").countryName("lgium").build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
  }

  @Test
  public void testSearchOpeningDaysByCriteria_WorkingDay() {
    final OpeningDaysSearchCriteria criteria = OpeningDaysSearchCriteria.builder()
        .dateFrom("2019-01-01").dateTo("2019-12-31").workingDayCode("WORKING_DAY").build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 10);
  }

  @Test
  public void testSearchOpeningDaysByCriteria_WorkingDayAndCountry() {
    final OpeningDaysSearchCriteria criteria =
        OpeningDaysSearchCriteria.builder().dateFrom("2019-01-01").dateTo("2019-12-31")
            .workingDayCode("PUBLIC_HOLIDAY").countryName("aust").build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 10);
  }

  @Test
  public void testSearchOpeningDaysByCriteria_DateRange() {
    final OpeningDaysSearchCriteria criteria =
        OpeningDaysSearchCriteria.builder().dateFrom("2019-01-01").dateTo("2019-04-05").build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 10);
  }

  @Test
  public void testSearchOpeningDaysByCriteria_MultiCriteria() {
    final OpeningDaysSearchCriteria criteria = OpeningDaysSearchCriteria.builder()
        .dateFrom("2019-01-01").dateTo("2019-04-05").workingDayCode("WORKING_DAY")
        .expAffiliate("matik").expDeliveryAddressId("1234").build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
  }

  @Test
  public void testSearchOpeningDaysByCriteria_AscSorting() {
    final OpeningDaysSearchCriteria criteria =
        OpeningDaysSearchCriteria.builder().dateFrom("2019-10-30").dateTo("2019-11-30")
            .expDeliveryAddressId("123").orderDescByCountryName(false).build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 2);
    Assert.assertThat(response.getContent().get(0).getCountryName(),
        Is.is("Austria"));
  }

  @Test
  public void testSearchOpeningDaysByCriteria_DescSorting() {
    final OpeningDaysSearchCriteria criteria =
        OpeningDaysSearchCriteria.builder().dateFrom("2019-10-30").dateTo("2019-11-30")
            .expDeliveryAddressId("123").orderDescByCountryName(true).build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 2);
    Assert.assertThat(response.getContent().get(0).getCountryName(),
        Is.is("Belgium"));
  }

  @Test
  public void testSearchOpeningDaysByCriteria_DateDescSorting() {
    final OpeningDaysSearchCriteria criteria = OpeningDaysSearchCriteria.builder()
        .dateFrom("2019-02-05").dateTo("2019-12-31").orderDescByDate(true).build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 10);
    Assert.assertThat(response.getContent().get(0).getDate(),
        Is.is(Date.valueOf("2019-12-31")));
  }

  @Test
  public void testSearchOpeningDaysByCriteria_DateAscSorting() {
    final OpeningDaysSearchCriteria criteria = OpeningDaysSearchCriteria.builder()
        .dateFrom("2019-02-05").dateTo("2019-12-31").orderDescByDate(false).build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 10);
    Assert.assertThat(response.getContent().get(0).getDate(),
        Is.is(Date.valueOf("2019-02-05")));
  }

  @Test
  public void testSearchOpeningDaysByCriteria_Branch() {
    final OpeningDaysSearchCriteria criteria = OpeningDaysSearchCriteria.builder()
        .dateFrom("2019-02-05").dateTo("2019-12-31").expBranchInfo("1012").build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 1);
  }

  @Test
  public void testSearchOpeningDaysByCriteria_ExpWorkingDay() {
    final OpeningDaysSearchCriteria criteria = OpeningDaysSearchCriteria.builder()
        .dateFrom("2019-02-05").dateTo("2019-12-31").expWorkingDayCode("WORKING_DAY").build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 4);
  }

  @Test
  public void testSearchOpeningDaysByCriteria_NotFound() {
    final OpeningDaysSearchCriteria criteria = OpeningDaysSearchCriteria.builder()
        .dateFrom("2019-02-05").dateTo("2019-12-31").expAffiliate("10012").build();
    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(response, 0);
  }

  @Test(expected = OpeningDaysValidationException.class)
  public void testImportOpeningDays_checkExistingTrue() throws OpeningDaysValidationException {
    final CsvOpeningCalendar csvOpeningCalendar =
        CsvOpeningCalendar.builder().date(Date.valueOf("2019-03-25")).country("Austria")
            .workingDayCode(WorkingDayCode.WORKING_DAY.name()).build();
    openingDaysService.importOpeningDays(Collections.singletonList(csvOpeningCalendar), true);
  }

  @Test
  public void testImportOpeningDays_checkExistingFalse() throws OpeningDaysValidationException {
    final Date date = Date.valueOf("2015-03-25");
    final String countryName = "Austria";
    final CsvOpeningCalendar csvOpeningCalendar = CsvOpeningCalendar.builder().date(date)
        .country(countryName).workingDayCode(WorkingDayCode.WORKING_DAY.name()).build();
    openingDaysService.importOpeningDays(Collections.singletonList(csvOpeningCalendar), true);
    final Integer importedId = openingDaysCalendarRepo.findIdByDateAndCountryName(date, countryName);
    Assert.assertThat(importedId, Matchers.notNullValue());
  }

  @Test
  public void testImportOpeningDays() throws OpeningDaysValidationException {
    final Date date = Date.valueOf("2019-03-25");
    final String countryName = "Austria";
    final CsvOpeningCalendar csvOpeningCalendar = CsvOpeningCalendar.builder().date(date)
        .country(countryName).workingDayCode(WorkingDayCode.NON_WORKING_DAY.name()).build();
    openingDaysService.importOpeningDays(Collections.singletonList(csvOpeningCalendar), false);
    final Integer importedId = openingDaysCalendarRepo.findIdByDateAndCountryName(date, countryName);
    Assert.assertThat(importedId, Matchers.notNullValue());
    Optional<OpeningDaysCalendar> importedDate = openingDaysCalendarRepo.findOneById(importedId);
    Assert.assertThat(importedDate.isPresent(), Is.is(true));
    Assert.assertThat(importedDate.get().getCountry().getShortName(), Is.is(countryName));
    Assert.assertThat(importedDate.get().getDatetime(), Is.is(date));
  }

  private void assertSearchResult(final Page<OpeningDaysDto> openingDays, final Integer expected) {
    Assert.assertThat(openingDays, Matchers.notNullValue());
    Assert.assertThat(openingDays.getContent(), Matchers.notNullValue());
    Assert.assertThat(openingDays.getNumberOfElements(), Matchers.greaterThanOrEqualTo(expected));
  }

  private OpeningDaysRequestBody initOpeningDaysData() {
    final OpeningDaysExceptionDto exceptions = OpeningDaysExceptionDto.builder()
        .affiliate(SupportedAffiliate.DERENDINGER_AT.getCompanyName()).branches(Arrays.asList("1001","1002"))
        .workingDayCode(WorkingDayCode.WORKING_DAY.name()).deliveryAdrressIDs(Arrays.asList("123456789")).build();
    return OpeningDaysRequestBody.builder()
        .date("2015-02-22")
        .countryId(17)
        .workingDayCode(WorkingDayCode.NON_WORKING_DAY.name())
        .exceptions(exceptions).build();
  }
}
