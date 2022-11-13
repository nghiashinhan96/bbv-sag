package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.WssBranchOpeningTimeRepository;
import com.sagag.eshop.repo.api.WssBranchRepository;
import com.sagag.eshop.repo.entity.WssBranch;
import com.sagag.eshop.repo.entity.WssBranchOpeningTime;
import com.sagag.eshop.repo.specification.WssBranchSpecifications;
import com.sagag.eshop.service.api.WssBranchService;
import com.sagag.eshop.service.converter.WssBranchConverters;
import com.sagag.eshop.service.converter.WssBranchOpeningTimeConverters;
import com.sagag.eshop.service.exception.WssBranchValidationException;
import com.sagag.eshop.service.exception.WssBranchValidationException.WssBranchErrorCase;
import com.sagag.services.common.utils.SagValidationUtils;
import com.sagag.services.domain.eshop.branch.dto.BranchTimeDto;
import com.sagag.services.domain.eshop.branch.dto.WssBranchRequestBody;
import com.sagag.services.domain.eshop.branch.dto.WssBranchSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.WssBranchDto;
import com.sagag.services.domain.eshop.dto.WssBranchOpeningTimeDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;

/**
 * Implementation class for WSS branch service.
 */
@Service
@Slf4j
public class WssBranchServiceImpl implements WssBranchService {

  @Autowired
  private WssBranchRepository wssBranchRepository;

  @Autowired
  private WssBranchOpeningTimeRepository wssBranchOpeningTimeRepo;

  @Override
  public Optional<WssBranch> searchByBranchNr(final Integer branchNr, final Integer orgId) {
    log.debug("searching branch by branch number = {}", branchNr);
    if (Objects.isNull(branchNr)) {
      throw new IllegalArgumentException("The branch number must not be empty");
    }
    return wssBranchRepository.findOneByBranchNrAndOrgId(branchNr, orgId);
  }

  @Override
  @Transactional
  public WssBranchDto create(final WssBranchRequestBody request, final int orgId)
      throws WssBranchValidationException {
    log.debug("Creating new branch {}", request);
    validateBranchRequest(request, orgId, true);
    WssBranch wssBranch =
        wssBranchRepository.save(WssBranchConverters.convertFromRequest(request, orgId));
    return saveWssBranchOpeningTime(request, wssBranch);
  }

  @Override
  @Transactional
  public WssBranchDto update(final WssBranchRequestBody request, final int orgId)
      throws WssBranchValidationException {
    log.debug("Update existing branch {}", request);
    validateBranchRequest(request, orgId, false);

    final Integer id = wssBranchRepository.findIdByBranchNrAndOrgId(request.getBranchNr(), orgId)
        .orElseThrow(() -> new WssBranchValidationException(WssBranchErrorCase.WBE_BNE_001,
            "This branch not exits" + request.getBranchNr()));

    final WssBranch branch = WssBranchConverters.convertFromRequest(request, orgId);
    branch.setId(id);
    branch.setWssBranchOpeningTimes(Lists.emptyList());
    WssBranch wssBranch = wssBranchRepository.save(branch);
    wssBranchOpeningTimeRepo.deleteAllByWssBranch(wssBranch);
    return saveWssBranchOpeningTime(request, wssBranch);
  }

  private WssBranchDto saveWssBranchOpeningTime(WssBranchRequestBody request, WssBranch wssBranch) {
    List<WssBranchOpeningTime> branchOpeningTimes = request.getWssBranchOpeningTimes().stream()
        .map(WssBranchOpeningTimeConverters::convertFromDto).collect(Collectors.toList());
    branchOpeningTimes
        .forEach(wssBranchOpeningTime -> wssBranchOpeningTime.setWssBranch(wssBranch));
    wssBranchOpeningTimeRepo.saveAll(branchOpeningTimes);
    wssBranch.setWssBranchOpeningTimes(branchOpeningTimes);
    return WssBranchConverters.convertToBranchDto(wssBranch);
  }

  @Override
  @Transactional
  public void remove(final Integer branchNr, final Integer orgId)
      throws WssBranchValidationException {
    Assert.notNull(branchNr, "WssBranch number must not be null");

    final WssBranch branch = searchByBranchNr(branchNr, orgId)
        .orElseThrow(() -> new WssBranchValidationException(WssBranchErrorCase.WBE_BNE_001,
            "This branch not exits: " + branchNr));
    if (CollectionUtils.isNotEmpty(branch.getWssDeliveryProfile())) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_BU_001,
          "branch is using by delivery profile setting: " + branchNr);
    }
    wssBranchOpeningTimeRepo.deleteAllByWssBranch(branch);
    wssBranchRepository.delete(branch);
  }

  @Override
  public Page<WssBranchDto> searchBranchByCriteria(final WssBranchSearchRequestCriteria criteria,
      final Pageable pageable) {
    log.debug("Search WSS branches by criteria = {}, pageable = {}", criteria, pageable);

    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<WssBranch> specForBranch =
        WssBranchSpecifications.searchBranchByCriteria(criteria);
    return wssBranchRepository.findAll(specForBranch, pageable)
        .map(WssBranchConverters.optionalBranchConverter());
  }

  @Override
  public Optional<WssBranchDto> getBranchDetail(final Integer branchNr, final Integer orgId)
      throws WssBranchValidationException {
    log.debug("Get branch detail by branch number = {}", branchNr);
    Assert.notNull(branchNr, "Wss Branch number must not be null");

    return wssBranchRepository.findOneByBranchNrAndOrgId(branchNr, orgId)
        .map(WssBranchConverters.optionalBranchConverter());
  }

  private void validateBranchRequest(final WssBranchRequestBody request, int orgId,
      final boolean isCreatingMode) throws WssBranchValidationException {
    Assert.notNull(request, "request body must not be null");
    final Optional<ConstraintViolation<WssBranchRequestBody>> violationOpt =
        SagValidationUtils.validateObjectAndReturnFirstError(request);
    if (violationOpt.isPresent()) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_IR_001,
          WssBranchErrorCase.WBE_IR_001.code());
    }

    if (CollectionUtils.isEmpty(request.getWssBranchOpeningTimes())) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_MOT_001,
          "For at least one day of the week, valid opening hours must be specified");
    }
    checkDuplicatedWeekDay(request);
    checkBranchOpeningTime(request);

    if (isCreatingMode) {
      checkExistingBranch(request, orgId);
    }
  }

  private void checkBranchOpeningTime(WssBranchRequestBody request)
      throws WssBranchValidationException {
    for (WssBranchOpeningTimeDto wssBranchOpeningTime : request.getWssBranchOpeningTimes()) {
      validateBranchTime(wssBranchOpeningTime);
    }
  }

  private void checkDuplicatedWeekDay(WssBranchRequestBody request)
      throws WssBranchValidationException {
    boolean isDuplicatedWeekDay = request.getWssBranchOpeningTimes().stream()
        .collect(Collectors.groupingBy(WssBranchOpeningTimeDto::getWeekDay, Collectors.counting()))
        .entrySet().stream().anyMatch(m -> m.getValue() > 1);
    if (isDuplicatedWeekDay) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_IR_001,
          WssBranchErrorCase.WBE_IR_001.code());
    }
  }

  private void validateBranchTime(final WssBranchOpeningTimeDto request)
      throws WssBranchValidationException {
    BranchTimeDto branchTime;
    try {
      branchTime = request.getBranchTimeDto();
    } catch (IllegalArgumentException e) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_IT_001, e.getMessage());
    }

    if (!branchTime.getOpeningTime().isBefore(branchTime.getClosingTime())) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_IT_001,
          "The closing time must be greater than the opening time");
    }

    if (Objects.isNull(branchTime.getLunchStartTime())
        || Objects.isNull(branchTime.getLunchEndTime())) {
      return;
    }

    if (!branchTime.getLunchStartTime().isBefore(branchTime.getLunchEndTime())) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_IT_001,
          "The lunch start time must be greater than the lunch end time");
    }

    final Interval interval = new Interval(branchTime.getOpeningTime().toDateTimeToday(),
        branchTime.getClosingTime().toDateTimeToday());
    final Interval lunchInterval = new Interval(branchTime.getLunchStartTime().toDateTimeToday(),
        branchTime.getLunchEndTime().toDateTimeToday());
    if (!interval.contains(lunchInterval)) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_IT_001,
          "The lunch time is not within the opening interval");
    }
  }

  private void checkExistingBranch(final WssBranchRequestBody request, int orgId)
      throws WssBranchValidationException {
    if (wssBranchRepository.checkExistingBranchByBranchNrAndOrgId(request.getBranchNr(), orgId)) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_DUB_001,
          "This branch already exists in database with branch number " + request.getBranchNr());
    }

    if (wssBranchRepository.checkExistingBranchByBranchCodeAndOrgId(request.getBranchCode(),
        orgId)) {
      throw new WssBranchValidationException(WssBranchErrorCase.WBE_DUB_002,
          "This branch already exists in database with branch code " + request.getBranchCode());
    }
  }

  @Override
  public List<WssBranchDto> getBranchesByOrganisation(final Integer orgId) {
    return wssBranchRepository.findAllByOrgId(orgId).stream()
        .map(WssBranchConverters.optionalBranchConverter()).collect(Collectors.toList());
  }
}
