package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.WssTourRepository;
import com.sagag.eshop.repo.api.WssTourTimesRepository;
import com.sagag.eshop.repo.entity.WssTour;
import com.sagag.eshop.repo.specification.WssTourSpecifications;
import com.sagag.eshop.service.api.WssTourService;
import com.sagag.eshop.service.converter.WssTourConverters;
import com.sagag.eshop.service.exception.WssTourValidationException;
import com.sagag.eshop.service.exception.WssTourValidationException.WssTourErrorCase;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagValidationUtils;
import com.sagag.services.domain.eshop.dto.WssTourDto;
import com.sagag.services.domain.eshop.dto.WssTourTimesDto;
import com.sagag.services.domain.eshop.tour.dto.WssTourSearchRequestCriteria;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;

/**
 * Implementation class for WSS Tour service.
 */
@Service
@Slf4j
public class WssTourServiceImpl implements WssTourService {

  @Autowired
  private WssTourRepository wssTourRepo;

  @Autowired
  private WssTourTimesRepository wssTourTimesRepo;

  @Override
  @Transactional
  public WssTourDto create(final WssTourDto wssTourDto, final int orgId)
      throws WssTourValidationException {
    log.debug("Creating new WSS Tour {}", wssTourDto);
    validateTourDto(wssTourDto, true, orgId);

    wssTourDto.setOrgId(orgId);
    WssTour wssTour = WssTourConverters.convertFromDto(wssTourDto);
    wssTour.getWssTourTimes().forEach(wssTourTimes -> wssTourTimes.setWssTour(wssTour));
    return WssTourConverters.convertToWssTourDto(wssTourRepo.save(wssTour));
  }

  @Override
  @Transactional
  public WssTourDto update(final WssTourDto wssTourDto, final int orgId)
      throws WssTourValidationException {
    log.debug("Update WSS Tour {}", wssTourDto);
    validateTourDto(wssTourDto, false, orgId);

    WssTour existedWssTour = wssTourRepo.findById(wssTourDto.getId())
        .orElseThrow(WssTourValidationException.wssTourNotFound());
    wssTourTimesRepo.deleteAll(existedWssTour.getWssTourTimes());

    WssTour wssTour = WssTourConverters.convertFromDto(wssTourDto);
    existedWssTour.setName(wssTourDto.getName());
    existedWssTour.setWssTourTimes(wssTour.getWssTourTimes());

    existedWssTour.getWssTourTimes()
    .forEach(wssTourTimes -> wssTourTimes.setWssTour(existedWssTour));

    return WssTourConverters.convertToWssTourDto(wssTourRepo.save(existedWssTour));
  }

  @Override
  @Transactional
  public void remove(final Integer wssTourId, final int orgId) throws WssTourValidationException {
    Assert.notNull(wssTourId, "WssTour id must not be null");

    WssTour existedWssTour = wssTourRepo.findByIdAndOrgId(wssTourId, orgId)
        .orElseThrow(WssTourValidationException.wssTourNotFound());
    if (CollectionUtils.isNotEmpty(existedWssTour.getWssDeliveryProfileTours())) {
      throw new WssTourValidationException(WssTourErrorCase.WTE_TU_001,
          "Tour is using by delivery profile setting");
    }

    wssTourRepo.delete(existedWssTour);
  }

  @Override
  public Page<WssTourDto> searchTourByCriteria(final WssTourSearchRequestCriteria criteria,
      final Pageable pageable) {
    log.debug("Search WSS tours by criteria = {}, pageable = {}", criteria, pageable);

    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<WssTour> specForTour = WssTourSpecifications.searchTourByCriteria(criteria);
    return wssTourRepo.findAll(specForTour, pageable)
        .map(WssTourConverters.optionalTourConverter());
  }

  private void validateTourDto(final WssTourDto wssTourDto, final boolean isCreatingMode,
      final int orgId) throws WssTourValidationException {
    Assert.notNull(wssTourDto, "wss tour must not be null");
    final Optional<ConstraintViolation<WssTourDto>> violationOpt =
        SagValidationUtils.validateObjectAndReturnFirstError(wssTourDto);
    if (violationOpt.isPresent()) {
      throw new WssTourValidationException(WssTourErrorCase.WTE_IR_001,
          WssTourErrorCase.WTE_IR_001.code());
    }

    if (isCreatingMode) {
      checkExistingTour(wssTourDto, orgId);
    } else {
      checkUpdateExistingTour(wssTourDto, orgId);
    }

    checkDuplicateTourTimeWeekDay(wssTourDto.getWssTourTimesDtos());

    validateTourDepartureTime(wssTourDto.getWssTourTimesDtos());
  }

  private void checkExistingTour(final WssTourDto wssTourDto, final int orgId)
      throws WssTourValidationException {
    if (wssTourRepo.checkExistingTourByNameAndOrgId(wssTourDto.getName(), orgId)) {
      throw new WssTourValidationException(WssTourErrorCase.WTE_DUB_001,
          "This tour already exists in database with tour name " + wssTourDto.getName());
    }
  }

  private void checkUpdateExistingTour(final WssTourDto wssTourDto, final int orgId)
      throws WssTourValidationException {
    if (wssTourRepo.checkExistingTourByIdAndNameAndOrgId(wssTourDto.getId(), wssTourDto.getName(),
        orgId)) {
      throw new WssTourValidationException(WssTourErrorCase.WTE_DUB_001,
          "This tour already exists in database with tour name " + wssTourDto.getName());
    }
  }

  private void checkDuplicateTourTimeWeekDay(final List<WssTourTimesDto> wssTourTimes)
      throws WssTourValidationException {
    if (CollectionUtils.isEmpty(wssTourTimes)) {
      return;
    }
    Set<WeekDay> weekDaysSet = new HashSet<WeekDay>();
    if (wssTourTimes.stream().filter(t -> !weekDaysSet.add(t.getWeekDay()))
        .collect(Collectors.toSet()).size() > 0) {
      throw new WssTourValidationException(WssTourErrorCase.WTE_DUB_002,
          "This tour have duplicated weekday");
    }
  }

  private void validateTourDepartureTime(final List<WssTourTimesDto> wssTourTimes)
      throws WssTourValidationException {
    if (CollectionUtils.isEmpty(wssTourTimes)) {
      return;
    }
    for (WssTourTimesDto tourTimeDto : wssTourTimes) {
      validateTime(tourTimeDto.getDepartureTime(), DateUtils.TIME_PATTERN);
    }
  }

  private void validateTime(String time, String pattern) throws WssTourValidationException {
    try {
      DateUtils.convertStringToTime(time, pattern);
    } catch (IllegalArgumentException e) {
      throw new WssTourValidationException(WssTourErrorCase.WTE_IT_001,
          "The chosen time is invalid");
    }
  }

  @Override
  @Transactional
  public WssTourDto getWssTourDetail(Integer wssTourId, int orgId)
      throws WssTourValidationException {
    return WssTourConverters.convertToWssTourDto(wssTourRepo.findByIdAndOrgId(wssTourId, orgId)
        .orElseThrow(WssTourValidationException.wssTourNotFound()));
  }

}
