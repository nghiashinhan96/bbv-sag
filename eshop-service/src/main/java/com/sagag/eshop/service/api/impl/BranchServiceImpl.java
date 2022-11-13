package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.BranchOpeningTimeRepository;
import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.api.CountryRepository;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.entity.BranchOpeningTime;
import com.sagag.eshop.repo.specification.BranchSpecifications;
import com.sagag.eshop.service.api.BranchService;
import com.sagag.eshop.service.converter.BranchConverters;
import com.sagag.eshop.service.converter.BranchOpeningTimeConverters;
import com.sagag.eshop.service.dto.BranchDetailDto;
import com.sagag.eshop.service.dto.BranchDto;
import com.sagag.eshop.service.exception.BranchValidationException;
import com.sagag.eshop.service.exception.BranchValidationException.BranchErrorCase;
import com.sagag.services.common.utils.SagValidationUtils;
import com.sagag.services.domain.eshop.branch.dto.BranchRequestBody;
import com.sagag.services.domain.eshop.branch.dto.BranchSearchRequestCriteria;
import com.sagag.services.domain.eshop.branch.dto.BranchTimeDto;
import com.sagag.services.domain.eshop.dto.BranchOpeningTimeDto;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementation class for branch service.
 */
@Service
@Slf4j
public class BranchServiceImpl implements BranchService {

  @Autowired
  private BranchRepository branchRepository;

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private BranchOpeningTimeRepository branchOpeningTimeRepo;

  @Override
  @Transactional
  public BranchDetailDto create(final BranchRequestBody request) throws BranchValidationException {
    log.debug("Creating new branch {}", request);
    validateBranchRequest(request, true);
    final Branch branch = branchRepository.save(BranchConverters.convertFromRequest(request));
    return saveBranchOpeningTime(request, branch);
  }

  @Override
  @Transactional
  public BranchDetailDto update(final BranchRequestBody request) throws BranchValidationException {
    log.debug("Update existing branch {}", request);
    validateBranchRequest(request, false);

    final Integer id = branchRepository.findIdByBranchNr(request.getBranchNr())
        .orElseThrow(() -> new BranchValidationException(BranchErrorCase.BE_BNE_001,
            "This branch not exits" + request.getBranchNr()));

    Branch branch = BranchConverters.convertFromRequest(request);
    branch.setId(id);
    branch = branchRepository.save(branch);
    branchOpeningTimeRepo.deleteAllByBranchId(branch.getId());
    return saveBranchOpeningTime(request, branch);
  }

  @Override
  @Transactional
  public void remove(final Integer branchNr) throws BranchValidationException {
    Assert.notNull(branchNr, "Branch number must not be null");

    final Branch branch = branchRepository.findOneByBranchNr(branchNr)
        .orElseThrow(() -> new BranchValidationException(BranchErrorCase.BE_BNE_001,
            "This branch not exits: " + branchNr));
    branchOpeningTimeRepo.deleteAllByBranchId(branch.getId());
    branchRepository.delete(branch);
  }

  @Override
  public Page<BranchDto> searchBranchByCriteria(final BranchSearchRequestCriteria criteria,
      final Pageable pageable) {
    log.debug("Search branches by criteria = {}, pageable = {}", criteria, pageable);
    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<Branch> specForBranch =
        BranchSpecifications.searchBranchByCriteria(criteria);
    final Page<Branch> branches = branchRepository.findAll(specForBranch, pageable);

    final List<BranchOpeningTime> branchOpeningTimeList =
        branchOpeningTimeRepo.findByBranchNrList(branches.map(Branch::getBranchNr).getContent());
    final Map<Integer, List<BranchOpeningTime>> branchOpeningTimeMap = branchOpeningTimeList
        .stream().collect(Collectors.groupingBy(BranchOpeningTime::getBranchId));

    return branches.map(branch -> BranchConverters
        .optionalBranchConverter(branchOpeningTimeMap.getOrDefault(branch.getId(),
            Collections.emptyList())).apply(branch));
  }

  @Override
  public Optional<BranchDetailDto> getBranchDetail(final Integer branchNr)
      throws BranchValidationException {
    log.debug("Get branch detail by branch number = {}", branchNr);
    Assert.notNull(branchNr, "Branch number must not be null");

    return branchRepository.findOneByBranchNr(branchNr)
        .map(branch -> BranchConverters.optionalBranchDetailConverter(
            branchOpeningTimeRepo.findByBranchId(branch.getId())).apply(branch));
  }

  private void validateBranchRequest(final BranchRequestBody request, final boolean isCreatingMode)
      throws BranchValidationException {
    Assert.notNull(request, "request body must not be null");
    final Optional<ConstraintViolation<BranchRequestBody>> violationOpt =
        SagValidationUtils.validateObjectAndReturnFirstError(request);
    if (violationOpt.isPresent()) {
      throw new BranchValidationException(BranchErrorCase.BE_IR_001,
          BranchErrorCase.BE_IR_001.code());
    }
    validateBranchTime(request);

    if (isCreatingMode) {
      checkExistingBranch(request);
    }
  }

  private void validateBranchTime(final BranchRequestBody request)
      throws BranchValidationException {
    if (CollectionUtils.isEmpty(request.getBranchOpeningTimes())) {
      throw new BranchValidationException(BranchErrorCase.BE_MOT_001,
          "For at least one day of the week, valid opening hours must be specified");
    }
    
    checkDuplicatedWeekDay(request);
    for (BranchOpeningTimeDto branchOpeningTimeDto : request.getBranchOpeningTimes()) {
      BranchTimeDto branchTime = new BranchTimeDto();
      try {
        branchTime = branchOpeningTimeDto.getBranchTimeDto();
      } catch (IllegalArgumentException | NullPointerException e) {
        throw new BranchValidationException(BranchErrorCase.BE_IT_001, e.getMessage());
      }

      if (!branchTime.getOpeningTime().isBefore(branchTime.getClosingTime())) {
        throw new BranchValidationException(BranchErrorCase.BE_IT_001,
            "The closing time must be greater than the opening time");
      }

      if (Objects.isNull(branchTime.getLunchStartTime())
          || Objects.isNull(branchTime.getLunchEndTime())) {
        return;
      }

      if (!branchTime.getLunchStartTime().isBefore(branchTime.getLunchEndTime())) {
        throw new BranchValidationException(BranchErrorCase.BE_IT_001,
            "The lunch start time must be greater than the lunch end time");
      }

      final Interval interval = new Interval(branchTime.getOpeningTime().toDateTimeToday(),
          branchTime.getClosingTime().toDateTimeToday());
      final Interval lunchInterval = new Interval(branchTime.getLunchStartTime().toDateTimeToday(),
          branchTime.getLunchEndTime().toDateTimeToday());
      if (!interval.contains(lunchInterval)) {
        throw new BranchValidationException(BranchErrorCase.BE_IT_001,
            "The lunch time is not within the opening interval");
      }
    }
  }

  private void checkDuplicatedWeekDay(BranchRequestBody request) throws BranchValidationException {
    boolean isDuplicatedWeekDay = request.getBranchOpeningTimes().stream()
        .collect(Collectors.groupingBy(BranchOpeningTimeDto::getWeekDay, Collectors.counting()))
        .entrySet().stream().anyMatch(m -> m.getValue() > 1);
    if (isDuplicatedWeekDay) {
      throw new BranchValidationException(BranchErrorCase.BE_IR_001,
          BranchErrorCase.BE_IR_001.code());
    }
  }

  private void checkExistingBranch(final BranchRequestBody request)
      throws BranchValidationException {
    if (branchRepository.checkExistingBranchByBranchNr(request.getBranchNr())) {
      throw new BranchValidationException(BranchErrorCase.BE_DUB_001,
          "This branch already exists in database with branch number " + request.getBranchNr());
    }

    if (!StringUtils.isEmpty(request.getBranchCode())
        && branchRepository.checkExistingBranchByBranchCode(request.getBranchCode())) {
      throw new BranchValidationException(BranchErrorCase.BE_DUB_002,
          "This branch already exists in database with branch code " + request.getBranchCode());
    }
  }

  @Override
  public List<BranchDto> getBranches() {
    return branchRepository.findAll().stream()
        .map(BranchConverters.optionalBranchConverter())
        .collect(Collectors.toList());
  }

  @Override
  public List<BranchDto> getBranchesByCountry(final String countryShortCode)
      throws BranchValidationException {
    Assert.notNull(countryShortCode, "The country short code must not be null");

    final List<Integer> ids = countryRepository.findIdsByShortCode(countryShortCode);
    if (CollectionUtils.isEmpty(ids)) {
      throw new BranchValidationException(BranchErrorCase.BE_BNE_001,
          "No branch belongs to country code " + countryShortCode);
    }
    return branchRepository.findByCountries(ids).parallelStream()
        .map(BranchConverters.optionalBranchConverter(Lists.newArrayList())).collect(Collectors.toList());
  }

  private BranchDetailDto saveBranchOpeningTime(BranchRequestBody request, Branch branch) {
    List<BranchOpeningTime> branchOpeningTimes =
        CollectionUtils.emptyIfNull(request.getBranchOpeningTimes()).stream()
        .map(BranchOpeningTimeConverters::convertFromDto).collect(Collectors.toList());
    branchOpeningTimes.forEach(branchOpeningTime -> branchOpeningTime.setBranchId(branch.getId()));

    branchOpeningTimes = branchOpeningTimeRepo.saveAll(branchOpeningTimes);

    return BranchConverters.optionalBranchDetailConverter(branchOpeningTimes).apply(branch);
  }

}
