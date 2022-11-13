package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.CountryRepository;
import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.api.WorkingDayRepository;
import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.repo.entity.OpeningDaysCalendar;
import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.eshop.repo.specification.OpeningDaysSpecifications;
import com.sagag.eshop.service.api.OpeningDaysService;
import com.sagag.eshop.service.converter.OpeningDaysConverters;
import com.sagag.eshop.service.dto.CsvOpeningCalendar;
import com.sagag.eshop.service.dto.OpeningDaysDetailDto;
import com.sagag.eshop.service.dto.OpeningDaysDto;
import com.sagag.eshop.service.exception.OpeningDaysValidationException;
import com.sagag.eshop.service.exception.OpeningDaysValidationException.OpeningDaysErrorCase;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.criteria.OpeningDaysSearchCriteria;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysExceptionDto;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysRequestBody;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

/**
 * Implementation class for opening days calendar service.
 */
@Service
@Slf4j
public class OpeningDaysServiceImpl implements OpeningDaysService {

  @Autowired
  private WorkingDayRepository workingDayRepository;

  @Autowired
  private OpeningDaysCalendarRepository openingDaysCalendarRepo;

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private SupportedAffiliateRepository supportedAffRepo;

  @Override
  public List<WorkingDay> getWorkingDayCodes() {
    return workingDayRepository.findAll();
  }

  @Override
  public OpeningDaysDetailDto create(final OpeningDaysRequestBody request)
      throws OpeningDaysValidationException {
    log.debug("Creating new opening days calendar {}", request);
    validateOpeningDaysRequest(request, true);
    return OpeningDaysConverters.convertToOpeningDaysDetail(
        openingDaysCalendarRepo.save(convertFromRequest().apply(request)));
  }

  @Override
  public OpeningDaysDetailDto update(final OpeningDaysRequestBody request)
      throws OpeningDaysValidationException {
    log.debug("Update existing opening days calendar {}", request);
    validateOpeningDaysRequest(request, false);

    OpeningDaysCalendar entity = new OpeningDaysCalendar();
    entity.setId(request.getId());
    if (!openingDaysCalendarRepo.exists(Example.of(entity))) {
      throw new ValidationException(
          String.format("The opening days calendar with id = %d not exits", request.getId()));
    }

    return OpeningDaysConverters.convertToOpeningDaysDetail(
        openingDaysCalendarRepo.save(convertFromRequest().apply(request)));
  }

  @Override
  public void remove(final Integer id) {
    Assert.notNull(id, "The id must be not null for removing exist opening calendar request");

    final OpeningDaysCalendar openingDaysCalendar =
        openingDaysCalendarRepo.findById(id).orElseThrow(() -> new ValidationException(
            String.format("The opening days calendar with id=%d not exits", id)));

    openingDaysCalendarRepo.delete(openingDaysCalendar);
  }

  @Override
  public Optional<OpeningDaysDetailDto> getOpeningDaysCalendar(final Integer id) {
    log.debug("Get existing opening days calendar detail by id = {}", id);
    Assert.notNull(id, "The id must not be null");
    return openingDaysCalendarRepo.findOneById(id)
        .map(OpeningDaysConverters.optionalOpeningDaysDetailConverter());
  }

  @Override
  public Page<OpeningDaysDto> searchByCriteria(final OpeningDaysSearchCriteria criteria,
      final Pageable pageable) {
    log.debug("Search opening days calendar by criteria = {}", criteria);
    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<OpeningDaysCalendar> specForOpeningDays =
        OpeningDaysSpecifications.searchOpeningDaysByCriteria(criteria);

    return openingDaysCalendarRepo.findAll(specForOpeningDays, pageable)
        .map(OpeningDaysConverters.openingDaysDtoConverter());
  }

  private void validateOpeningDaysRequest(final OpeningDaysRequestBody request,
      final boolean isCreatingMode) throws OpeningDaysValidationException {
    Assert.notNull(request, "Request body must not be null");
    if (isCreatingMode) {
      Assert.isNull(request.getId(), "The id must be null for creating new opening request");
    } else {
      Assert.notNull(request.getId(), "The id must be not null for editing exist opening request");
    }

    final Date date = DateUtils.toDate(request.getDate(), DateUtils.DEFAULT_DATE_PATTERN);
    Assert.notNull(date, "The date must be not null");

    final Optional<Integer> existingEntityId =
        openingDaysCalendarRepo.findByDateAndCountryId(date, request.getCountryId());

    // Throw exception in case create duplicated or edit duplicated with existing record.
    if (existingEntityId.isPresent()
        && (isCreatingMode || !existingEntityId.get().equals(request.getId()))) {
      throw new OpeningDaysValidationException(OpeningDaysErrorCase.ODE_DUB_001,
          "This opening days calendar already exists in database with the same date and country");
    }

    // Validate exception
    final OpeningDaysExceptionDto exceptions = request.getExceptions();
    if (Objects.isNull(exceptions)) {
      return;
    }
    Assert.notNull(exceptions.getWorkingDayCode(),
        "The working day code in exception term must be not null");

    Assert.isTrue(request.getWorkingDayCode() != exceptions.getWorkingDayCode(),
        "The working day code in exception must be different from the main part");

    Assert.isTrue(
        !CollectionUtils.isEmpty(exceptions.getBranches())
            || !CollectionUtils.isEmpty(exceptions.getDeliveryAdrressIDs()),
        "Branches or deliveryAddresses are required");
  }

  @Override
  public void importOpeningDays(final List<CsvOpeningCalendar> openingDaysCalendar,
      final Boolean checkExisting) throws OpeningDaysValidationException {
    if (checkExisting) {
      checkExistingData(openingDaysCalendar.stream().findFirst()
          .orElseThrow(() -> new OpeningDaysValidationException(OpeningDaysErrorCase.ODE_EMP_003,
              "The imported opening days calendar data is empty"))
          .getDate());
    }
    // Override all existing data
    final Map<String, Country> countryMap = getCountryMap(openingDaysCalendar);
    final Map<String, WorkingDay> workingDayMap = getWorkingDayMap(openingDaysCalendar);

    final List<OpeningDaysCalendar> openingDays =
        openingDaysCalendar
            .parallelStream().map(item -> convertFromCsvRequest(item,
                countryMap.get(item.getCountry()), workingDayMap.get(item.getWorkingDayCode())))
            .collect(Collectors.toList());
    openingDaysCalendarRepo.saveAll(openingDays);
  }

  @Override
  public Optional<Date> getDateLaterFromToday(final SupportedAffiliate affiliate,
      final String pickupBranchId, final int days) {
    final Date date = Calendar.getInstance().getTime();
    return openingDaysCalendarRepo.findWorkingDayLaterFrom(date,
        supportedAffRepo.findCountryShortNameByAffShortName(affiliate.getAffiliate()),
        affiliate.getCompanyName(), pickupBranchId, days);
  }

  private Map<String, WorkingDay> getWorkingDayMap(
      final List<CsvOpeningCalendar> openingDaysCalendar) {
    final List<String> workingDayCodes = openingDaysCalendar.parallelStream()
        .map(CsvOpeningCalendar::getWorkingDayCode).distinct().collect(Collectors.toList());
    return workingDayRepository.findAllByCodes(workingDayCodes).parallelStream()
        .collect(Collectors.toMap(WorkingDay::getCode, Function.identity()));
  }

  private Map<String, Country> getCountryMap(final List<CsvOpeningCalendar> openingDaysCalendar) {
    final List<String> countries = openingDaysCalendar.parallelStream()
        .map(CsvOpeningCalendar::getCountry).distinct().collect(Collectors.toList());
    return countryRepository.findAllByCountryNames(countries).parallelStream()
        .collect(Collectors.toMap(Country::getShortName, Function.identity()));
  }

  private void checkExistingData(final Date date) throws OpeningDaysValidationException {
    final Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    if (!openingDaysCalendarRepo.checkExistingByYear(cal.get(Calendar.YEAR))) {
      return;
    }
    throw new OpeningDaysValidationException(OpeningDaysErrorCase.ODE_DUB_002,
        "This opening days calendar already exists in database with the same year from import file");
  }

  private Function<OpeningDaysRequestBody, OpeningDaysCalendar> convertFromRequest() {
    return request -> OpeningDaysConverters.openingDaysEntityConverter(request,
        findCountryById(request.getCountryId()),
        findWorkingDayByCode(request.getWorkingDayCode()));
  }

  private OpeningDaysCalendar convertFromCsvRequest(final CsvOpeningCalendar request,
      final Country country, final WorkingDay workingDay) {
    request.setId(findExistingOpeningDaysId(request));
    return OpeningDaysConverters.openingDaysEntityConverter(request, country, workingDay);
  }

  private Integer findExistingOpeningDaysId(final CsvOpeningCalendar request) {
    return openingDaysCalendarRepo.findIdByDateAndCountryName(request.getDate(),
        request.getCountry());
  }

  private WorkingDay findWorkingDayByCode(final String code) {
    return workingDayRepository.findOneByCode(code).orElseThrow(() -> new ValidationException(
        String.format("The working day with code = %s not exits", code)));
  }

  private Country findCountryById(final Integer id) {
    return countryRepository.findById(id).orElseThrow(
        () -> new ValidationException(String.format("The country with id = %d not exits", id)));
  }
}
