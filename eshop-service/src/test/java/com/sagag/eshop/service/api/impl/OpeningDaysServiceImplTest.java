package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.api.CountryRepository;
import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.eshop.repo.api.WorkingDayRepository;
import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.repo.entity.OpeningDaysCalendar;
import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.eshop.service.dto.OpeningDaysDto;
import com.sagag.eshop.service.exception.OpeningDaysValidationException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.WorkingDayCode;
import com.sagag.services.domain.eshop.criteria.OpeningDaysSearchCriteria;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysExceptionDto;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysRequestBody;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

/**
 * Test class for opening days calendar service.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class OpeningDaysServiceImplTest {

  @InjectMocks
  private OpeningDaysServiceImpl openingDaysService;

  @Mock
  private OpeningDaysCalendarRepository openingDaysCalendarRepo;

  @Mock
  private WorkingDayRepository workingDayRepository;

  @Mock
  private CountryRepository countryRepository;

  @Mock
  private BranchRepository branchRepository;

  @Test
  public void testGetWorkingDayCodes() {
    Mockito.when(workingDayRepository.findAll())
        .thenReturn(Collections.singletonList(new WorkingDay()));
    final List<WorkingDay> workingdays = openingDaysService.getWorkingDayCodes();
    Assert.assertThat(CollectionUtils.isEmpty(workingdays), Is.is(false));
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
  public void testCreateNewOpeningDays_BranchAndDeliveryAddNull() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.getExceptions().setBranches(Collections.emptyList());
    openingDaysRequest.getExceptions().setDeliveryAdrressIDs(Collections.emptyList());
    openingDaysService.create(openingDaysRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewOpeningDays_IdNotNull() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.setId(1);;
    openingDaysService.create(openingDaysRequest);
  }

  @Test
  public void testCreateNewOpeningDays_BranchNull() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.getExceptions().setBranches(Collections.emptyList());
    final OpeningDaysCalendar openingDaysCalendar = initOpeningDaysEntityData();

    Mockito.when(countryRepository.findById(Mockito.any())).thenReturn(Optional.of(new Country()));
    Mockito.when(workingDayRepository.findOneByCode(Mockito.any()))
        .thenReturn(Optional.of(new WorkingDay()));
    Mockito.when(openingDaysCalendarRepo.save(Mockito.any())).thenReturn(openingDaysCalendar);

    openingDaysService.create(openingDaysRequest);
    Mockito.verify(openingDaysCalendarRepo, times(1)).save(openingDaysCalendar);
  }

  @Test
  public void testCreateNewOpeningDays() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    final OpeningDaysCalendar openingDaysCalendar = initOpeningDaysEntityData();

    Mockito.when(countryRepository.findById(Mockito.any())).thenReturn(Optional.of(new Country()));
    Mockito.when(workingDayRepository.findOneByCode(Mockito.any()))
        .thenReturn(Optional.of(new WorkingDay()));
    Mockito.when(openingDaysCalendarRepo.save(Mockito.any())).thenReturn(openingDaysCalendar);

    openingDaysService.create(openingDaysRequest);
    Mockito.verify(openingDaysCalendarRepo, times(1)).save(openingDaysCalendar);
  }

  @Test(expected = ValidationException.class)
  public void testUpdateOpeningDays_NotExist() throws OpeningDaysValidationException {
    final OpeningDaysRequestBody openingDaysRequest = initOpeningDaysData();
    openingDaysRequest.setId(0);
    Mockito.when(openingDaysCalendarRepo.exists(Mockito.any())).thenReturn(false);
    openingDaysService.update(openingDaysRequest);
    Mockito.verify(openingDaysCalendarRepo, times(1)).exists(Mockito.any());
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
    final OpeningDaysCalendar openingDaysCalendar = initOpeningDaysEntityData();
    openingDaysCalendar.setId(1);
    Mockito.when(countryRepository.findById(Mockito.any())).thenReturn(Optional.of(new Country()));
    Mockito.when(workingDayRepository.findOneByCode(Mockito.any()))
        .thenReturn(Optional.of(new WorkingDay()));
    Mockito.when(openingDaysCalendarRepo.exists(Mockito.any()))
        .thenReturn(true);
    Mockito.when(openingDaysCalendarRepo.save(Mockito.any())).thenReturn(openingDaysCalendar);

    openingDaysService.update(openingDaysRequest);
    Mockito.verify(openingDaysCalendarRepo, times(1)).save(openingDaysCalendar);
  }

  @Test(expected = ValidationException.class)
  public void testRemoveOpeningDays_NotExist() {
    Mockito.when(openingDaysCalendarRepo.findById(0)).thenReturn(Optional.empty());
    openingDaysService.remove(0);
    Mockito.verify(openingDaysCalendarRepo, times(1)).findById(0);
  }

  @Test
  public void testRemoveOpeningDays() {
    final OpeningDaysCalendar openingDaysCalendar = initOpeningDaysEntityData();
    final Integer id = 1;
    Mockito.when(openingDaysCalendarRepo.findById(id)).thenReturn(Optional.of(openingDaysCalendar));
    openingDaysService.remove(id);
    Mockito.verify(openingDaysCalendarRepo, times(1)).findById(id);
  }

  @Test
  public void testGetOpeningDaysDetail_NotExit() {
    final int id = 0;
    Mockito.when(openingDaysCalendarRepo.findOneById(Mockito.anyInt()))
        .thenReturn(Optional.empty());
    openingDaysService.getOpeningDaysCalendar(id);
    Mockito.verify(openingDaysCalendarRepo, times(1)).findOneById(id);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetOpeningDaysDetail_NullId() {
    final Integer id = null;
    openingDaysService.getOpeningDaysCalendar(id);
  }

  @Test
  public void testGetOpeningDaysDetail() {
    final OpeningDaysCalendar openingDaysCalendar = initOpeningDaysEntityData();
    final Integer id = 1;
    openingDaysCalendar.setId(id);
    Mockito.when(openingDaysCalendarRepo.findOneById(Mockito.anyInt()))
        .thenReturn(Optional.of(openingDaysCalendar));
    openingDaysService.getOpeningDaysCalendar(id);
    Mockito.verify(openingDaysCalendarRepo, times(1)).findOneById(id);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSearchOpeningDaysByCriteria() {
    final OpeningDaysSearchCriteria criteria =
        OpeningDaysSearchCriteria.builder().dateFrom("2019-03-25").dateTo("2019-12-25").build();

    final OpeningDaysCalendar openingDaysCalendar = initOpeningDaysEntityData();
    openingDaysCalendar.setId(1);

    final Page<OpeningDaysCalendar> page = new PageImpl<>(Arrays.asList(openingDaysCalendar));
    when(openingDaysCalendarRepo.findAll(Mockito.any(Specification.class),
        Mockito.any(Pageable.class))).thenReturn(page);

    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    Assert.assertNotNull(response);
    Assert.assertThat(response.getContent(), Matchers.notNullValue());
    Assert.assertThat(response.getContent().size(), Is.is(1));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSearchOpeningDaysByCriteria_NotFound() {
    final OpeningDaysSearchCriteria criteria =
        OpeningDaysSearchCriteria.builder().dateFrom("2019-03-25").dateTo("2019-12-25").build();

    when(openingDaysCalendarRepo.findAll(Mockito.any(Specification.class),
        Mockito.any(Pageable.class))).thenReturn(Page.empty());

    final Page<OpeningDaysDto> response =
        openingDaysService.searchByCriteria(criteria, PageRequest.of(0, 10));
    Assert.assertNotNull(response);
    Assert.assertThat(response.getContent(), Matchers.notNullValue());
    Assert.assertThat(response.getContent().size(), Is.is(0));
  }

  private OpeningDaysRequestBody initOpeningDaysData() {
    final OpeningDaysExceptionDto exceptions =
        OpeningDaysExceptionDto.builder()
            .affiliate(SupportedAffiliate.DERENDINGER_AT.getCompanyName())
            .workingDayCode(WorkingDayCode.WORKING_DAY.name())
            .deliveryAdrressIDs(Arrays.asList("123456789"))
            .branches(Collections.emptyList())
            .build();
    return OpeningDaysRequestBody.builder()
        .date("2019-02-22")
        .countryId(17)
        .workingDayCode(WorkingDayCode.NON_WORKING_DAY.name())
        .exceptions(exceptions)
        .build();
  }

  private OpeningDaysCalendar initOpeningDaysEntityData() {
    return OpeningDaysCalendar.builder()
        .datetime(Date.valueOf("2019-02-22"))
        .country(new Country())
        .workingDay(new WorkingDay())
        .exceptions(
            "{\"affiliate\":\"Derendinger-Austria\",\"branches\":[],\"workingDayCode\":\"WORKING_DAY\",\"deliveryAdrressIDs\":[\"123456789\"]}")
        .build();
  }
}
