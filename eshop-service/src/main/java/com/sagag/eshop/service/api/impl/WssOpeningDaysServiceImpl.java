package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.CountryRepository;
import com.sagag.eshop.repo.api.WssOpeningDaysCalendarRepository;
import com.sagag.eshop.repo.api.WssWorkingDayRepository;
import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.repo.entity.WssOpeningDaysCalendar;
import com.sagag.eshop.repo.entity.WssWorkingDay;
import com.sagag.eshop.repo.specification.WssOpeningDaysSpecifications;
import com.sagag.eshop.service.api.WssOpeningDaysService;
import com.sagag.eshop.service.converter.WssOpeningDaysConverters;
import com.sagag.eshop.service.exception.WssOpeningDaysValidationException;
import com.sagag.eshop.service.exception.WssOpeningDaysValidationException.WssOpeningDaysErrorCase;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.criteria.WssOpeningDaysSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvOpeningCalendar;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDetailDto;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDto;
import com.sagag.services.domain.eshop.openingdays.dto.WssOpeningDaysExceptionDto;
import com.sagag.services.domain.eshop.openingdays.dto.WssOpeningDaysRequestBody;

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
 * Implementation class for WSS opening days calendar service.
 */
@Service
@Slf4j
public class WssOpeningDaysServiceImpl implements WssOpeningDaysService {

  @Autowired
  private WssWorkingDayRepository wssWorkingDayRepository;

  @Autowired
  private WssOpeningDaysCalendarRepository wssOpeningDaysCalendarRepo;

  @Autowired
  private CountryRepository countryRepository;

  @Override
  public List<WssWorkingDay> getWorkingDayCodes() {
    return wssWorkingDayRepository.findAll();
  }

  @Override
  public WssOpeningDaysDetailDto create(final WssOpeningDaysRequestBody request, final int orgId)
      throws WssOpeningDaysValidationException {
    log.debug("Creating new WSS opening days calendar {}", request);
    validateWssOpeningDaysRequest(request, true, orgId);
    WssOpeningDaysCalendar wssOpeningCalendar = convertFromRequest().apply(request);
    wssOpeningCalendar.setOrgId(orgId);
    return WssOpeningDaysConverters.convertToWssOpeningDaysDetail(
        wssOpeningDaysCalendarRepo.save(wssOpeningCalendar));
  }

  @Override
  public WssOpeningDaysDetailDto update(final WssOpeningDaysRequestBody request, final int orgId)
      throws WssOpeningDaysValidationException {
    log.debug("Update existing WSS opening days calendar {}", request);
    validateWssOpeningDaysRequest(request, false, orgId);

    WssOpeningDaysCalendar entity = new WssOpeningDaysCalendar();
    entity.setId(request.getId());
    if (!wssOpeningDaysCalendarRepo.exists(Example.of(entity))) {
      throw new ValidationException(
          String.format("The WSS opening days calendar with id = %d not exits", request.getId()));
    }

    entity = convertFromRequest().apply(request);
    entity.setOrgId(orgId);
    return WssOpeningDaysConverters.convertToWssOpeningDaysDetail(
        wssOpeningDaysCalendarRepo.save(entity));
  }

  @Override
  public void remove(final Integer id) {
    Assert.notNull(id, "The id must be not null for removing exist WSS opening calendar request");

    final WssOpeningDaysCalendar wssOpeningDaysCalendar =
        wssOpeningDaysCalendarRepo.findById(id).orElseThrow(() -> new ValidationException(
            String.format("The WSS opening days calendar with id=%d not exits", id)));

    wssOpeningDaysCalendarRepo.delete(wssOpeningDaysCalendar);
  }

  @Override
  public Optional<WssOpeningDaysDetailDto> getOpeningDaysCalendar(final Integer id) {
    log.debug("Get existing WSS opening days calendar detail by id = {}", id);
    Assert.notNull(id, "The id must not be null");
    return wssOpeningDaysCalendarRepo.findOneById(id)
        .map(WssOpeningDaysConverters.optionalWssOpeningDaysDetailConverter());
  }

  @Override
  public Page<WssOpeningDaysDto> searchByCriteria(final WssOpeningDaysSearchCriteria criteria,
      final Pageable pageable) {
    log.debug("Search WSS opening days calendar by criteria = {}", criteria);
    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<WssOpeningDaysCalendar> specForOpeningDays =
        WssOpeningDaysSpecifications.searchOpeningDaysByCriteria(criteria);

    return wssOpeningDaysCalendarRepo.findAll(specForOpeningDays, pageable)
        .map(WssOpeningDaysConverters.wssOpeningDaysDtoConverter());
  }

  private void validateWssOpeningDaysRequest(final WssOpeningDaysRequestBody request,
      final boolean isCreatingMode, int orgId) throws WssOpeningDaysValidationException {
    Assert.notNull(request, "Request body must not be null");
    if (isCreatingMode) {
      Assert.isNull(request.getId(), "The id must be null for creating new opening request");
    } else {
      Assert.notNull(request.getId(), "The id must be not null for editing exist opening request");
    }

    final Date date = DateUtils.toDate(request.getDate(), DateUtils.DEFAULT_DATE_PATTERN);
    Assert.notNull(date, "The date must be not null");

    final Optional<Integer> existingEntityId =
        wssOpeningDaysCalendarRepo.findByDateAndCountryIdAndOrgId(date, request.getCountryId(), orgId);

    if (existingEntityId.isPresent()
        && (isCreatingMode || !existingEntityId.get().equals(request.getId()))) {
      throw new WssOpeningDaysValidationException(WssOpeningDaysErrorCase.WODE_DUB_001,
          "This WSS opening days calendar already exists in database with the same date, country and organisation id");
    }

    final WssOpeningDaysExceptionDto exceptions = request.getExceptions();
    if (Objects.isNull(exceptions)) {
      return;
    }
    Assert.notNull(exceptions.getWorkingDayCode(),
        "The working day code in exception term must be not null");

    Assert.isTrue(request.getWorkingDayCode() != exceptions.getWorkingDayCode(),
        "The working day code in exception must be different from the main part");

    Assert.isTrue(!CollectionUtils.isEmpty(exceptions.getBranches()), "Branches are required");
  }

  @Override
  public void importWssOpeningDays(final List<WssCsvOpeningCalendar> wssOpeningDaysCalendar,
      final Boolean checkExisting, final int orgId) throws WssOpeningDaysValidationException {
    if (checkExisting) {
      checkExistingData(wssOpeningDaysCalendar.stream().findFirst()
          .orElseThrow(() -> new WssOpeningDaysValidationException(WssOpeningDaysErrorCase.WODE_EMP_003,
              "The imported WSS opening days calendar data is empty"))
          .getDate(), orgId);
    }
    final Map<String, Country> countryMap = getCountryMap(wssOpeningDaysCalendar);
    final Map<String, WssWorkingDay> workingDayMap = getWorkingDayMap(wssOpeningDaysCalendar);

    final List<WssOpeningDaysCalendar> openingDays =
        wssOpeningDaysCalendar
            .parallelStream().map(item -> convertFromCsvRequest(item,
                countryMap.get(item.getCountry()), workingDayMap.get(item.getWorkingDayCode()), orgId))
            .collect(Collectors.toList());
    wssOpeningDaysCalendarRepo.saveAll(openingDays);
  }

  private Map<String, WssWorkingDay> getWorkingDayMap(
      final List<WssCsvOpeningCalendar> openingDaysCalendar) {
    final List<String> workingDayCodes = openingDaysCalendar.parallelStream()
        .map(WssCsvOpeningCalendar::getWorkingDayCode).distinct().collect(Collectors.toList());
    return wssWorkingDayRepository.findAllByCodes(workingDayCodes).parallelStream()
        .collect(Collectors.toMap(WssWorkingDay::getCode, Function.identity()));
  }

  private Map<String, Country> getCountryMap(final List<WssCsvOpeningCalendar> wssOpeningDaysCalendar) {
    final List<String> countries = wssOpeningDaysCalendar.parallelStream()
        .map(WssCsvOpeningCalendar::getCountry).distinct().collect(Collectors.toList());
    return countryRepository.findAllByCountryNames(countries).parallelStream()
        .collect(Collectors.toMap(Country::getShortName, Function.identity()));
  }

  private void checkExistingData(final Date date, int orgId) throws WssOpeningDaysValidationException {
    final Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    if (!wssOpeningDaysCalendarRepo.checkExistingByYearAndOrgId(cal.get(Calendar.YEAR), orgId)) {
      return;
    }
    throw new WssOpeningDaysValidationException(WssOpeningDaysErrorCase.WODE_DUB_002,
        "This WSS opening days calendar already exists in database with the same year from import file");
  }

  private Function<WssOpeningDaysRequestBody, WssOpeningDaysCalendar> convertFromRequest() {
    return request -> WssOpeningDaysConverters.wssOpeningDaysEntityConverter(request,
        findCountryById(request.getCountryId()), findWorkingDayByCode(request.getWorkingDayCode()));
  }

  private WssOpeningDaysCalendar convertFromCsvRequest(final WssCsvOpeningCalendar request,
      final Country country, final WssWorkingDay workingDay, int orgId) {
    request.setId(findExistingOpeningDaysId(request, orgId));
    return WssOpeningDaysConverters.wssOpeningDaysEntityConverter(request, country, workingDay, orgId);
  }

  private Integer findExistingOpeningDaysId(final WssCsvOpeningCalendar request, int orgId) {
    return wssOpeningDaysCalendarRepo.findIdByDateAndCountryNameAndOrgId(request.getDate(),
        request.getCountry(), orgId);
  }

  private WssWorkingDay findWorkingDayByCode(final String code) {
    return wssWorkingDayRepository.findOneByCode(code).orElseThrow(() -> new ValidationException(
        String.format("The working day with code = %s not exits", code)));
  }

  private Country findCountryById(final Integer id) {
    return countryRepository.findById(id).orElseThrow(
        () -> new ValidationException(String.format("The country with id = %d not exits", id)));
  }
}
