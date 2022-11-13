package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.api.DeliveryProfileRepository;
import com.sagag.eshop.repo.api.ExternalVendorRepository;
import com.sagag.eshop.repo.api.VDeliveryProfileRepository;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.entity.DeliveryProfile;
import com.sagag.eshop.repo.entity.ExternalVendor;
import com.sagag.eshop.repo.entity.VDeliveryProfile;
import com.sagag.eshop.repo.specification.DeliveryProfileSpecifications;
import com.sagag.eshop.service.api.CountryService;
import com.sagag.eshop.service.api.DeliveryProfileService;
import com.sagag.eshop.service.converter.BranchConverters;
import com.sagag.eshop.service.converter.DeliveryProfileConverters;
import com.sagag.eshop.service.dto.CsvDeliveryProfileDto;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException.DeliveryProfileErrorCase;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.common.utils.SagValidationUtils;
import com.sagag.services.domain.eshop.branch.dto.BranchRequestBody;
import com.sagag.services.domain.eshop.criteria.DeliveryProfileSearchCriteria;
import com.sagag.services.domain.eshop.dto.externalvendor.CountryDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileSavingDto;
import com.sagag.services.domain.eshop.dto.externalvendor.SupportDeliveryProfileDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

@Service
@Slf4j
public class DeliveryProfileServiceImpl implements DeliveryProfileService {

  private static final String ODE_EMP_002_MSG = "Csv file import have duplicate data for 2 fields "
      + "delivery profile id and delivery profile name";

  @Autowired
  private DeliveryProfileRepository deliveryProfileRepo;

  @Autowired
  private ExternalVendorRepository externalVendorRepo;

  @Autowired
  private BranchRepository branchRepo;

  @Autowired
  private VDeliveryProfileRepository vDeliveryProfileRepo;

  @Autowired
  private CountryService countryService;

  @Override
  @Transactional
  public void createDeliveryProfile(DeliveryProfileSavingDto profile, Long createdUserId)
      throws DeliveryProfileValidationException {
    Assert.notNull(profile, "The given delivery profile must not be null");
    Assert.notNull(createdUserId, "The given created user id must not be null");
    final DeliveryProfile deliveryProfile =
        DeliveryProfileConverters.deliveryProfileEntityConverter().apply(profile, Optional.empty());
    final Optional<ConstraintViolation<DeliveryProfile>> violationOpt =
        SagValidationUtils.validateObjectAndReturnFirstError(deliveryProfile);
    if (violationOpt.isPresent()) {
      final String msg = violationOpt.get().getMessage();
      log.error(msg);
      throw validationDeliveryProfileExceptionSupplier(msg).get();
    }
    duplicateDeliveryProfile(profile.getDeliveryBranchId());
    duplicateProfileName(profile);
    deliveryProfile.setCreatedUserId(createdUserId);
    deliveryProfile.setCreatedDate(Calendar.getInstance().getTime());
    deliveryProfileRepo.save(deliveryProfile);
  }

  @Override
  @Transactional
  public void updateDeliveryProfile(DeliveryProfileSavingDto updatedProfile, Long modifiedUserId)
      throws DeliveryProfileValidationException {
    Assert.notNull(updatedProfile, "The given updated delivery profile must not be null");
    Assert.notNull(modifiedUserId, "The given modified user id must not be null");

    final Integer deliveryProfileId = updatedProfile.getId();
    final Optional<DeliveryProfile> deliveryProfileOpt =
        deliveryProfileRepo.findById(deliveryProfileId);
    if (!deliveryProfileOpt.isPresent()) {
      throw notFoundDeliveryProfileExceptionSupplier(deliveryProfileId).get();
    }
    DeliveryProfile updateDeliveryProfile = DeliveryProfileConverters
        .deliveryProfileEntityConverter().apply(updatedProfile, deliveryProfileOpt);

    final Optional<ConstraintViolation<DeliveryProfile>> violationOpt =
        SagValidationUtils.validateObjectAndReturnFirstError(updateDeliveryProfile);
    if (violationOpt.isPresent()) {
      throw validationDeliveryProfileExceptionSupplier(violationOpt.get().getMessage()).get();
    }
    checkChangeDeliveryBranchId(updateDeliveryProfile, deliveryProfileOpt.get());
    duplicateProfileName(updatedProfile);
    updateDeliveryProfile.setModifiedUserId(modifiedUserId);
    updateDeliveryProfile.setModifiedDate(Calendar.getInstance().getTime());
    deliveryProfileRepo.save(updateDeliveryProfile);
  }

  private void duplicateProfileName(DeliveryProfileSavingDto profile)
      throws DeliveryProfileValidationException {
    Optional<Integer> optDeliveryProfileId =
        findDeliveryProfileIdByDeliveryProfileName(profile.getDeliveryProfileName());
    if (optDeliveryProfileId.isPresent()) {
      checkDuplicateProfileName(profile.getDeliveryProfileId(), optDeliveryProfileId.get(),
          profile.getDeliveryProfileName());
    }
  }

  private void checkChangeDeliveryBranchId(DeliveryProfile deliveryProfileDto,
      DeliveryProfile originDeliveryProfile) throws DeliveryProfileValidationException {
    final int deliveryBranchIdUpdate = deliveryProfileDto.getDeliveryBranchId();
    final int deliveryBranchIdOrigin = originDeliveryProfile.getDeliveryBranchId();

    if (deliveryBranchIdUpdate != deliveryBranchIdOrigin) {
      duplicateDeliveryProfile(deliveryBranchIdUpdate);
    }
  }

  private void duplicateDeliveryProfile(Integer deliveryBranchId)
      throws DeliveryProfileValidationException {
    DeliveryProfile deliveryProfileObj =
        DeliveryProfile.builder().deliveryBranchId(deliveryBranchId).build();
    if (deliveryProfileRepo.exists(Example.of(deliveryProfileObj))) {
      throw duplicateDeliveryProfileExceptionSupplier(deliveryBranchId).get();
    }
  }

  @Override
  @Transactional
  public void removeDeliveryProfile(Integer deliveryProfileId)
      throws DeliveryProfileValidationException {
    Assert.notNull(deliveryProfileId, "The given delivery profile id must not be null");
    if (!deliveryProfileRepo.existsById(deliveryProfileId)) {
      throw notFoundDeliveryProfileExceptionSupplier(deliveryProfileId).get();
    }

    // Check exist in external vendor
    final ExternalVendor vendor = new ExternalVendor();
    vendor.setDeliveryProfileId(deliveryProfileId);
    if (externalVendorRepo.exists(Example.of(vendor))) {
      final String msg = String.format(
          "Please remove external vendor is used delivery profile with id = %d", deliveryProfileId);
      throw new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_005, msg);
    }

    deliveryProfileRepo.deleteById(deliveryProfileId);
  }

  @Override
  @Transactional
  public void replaceDeliveryProfilesByCsv(List<CsvDeliveryProfileDto> csvDeliveryProfiles)
      throws DeliveryProfileValidationException {
    if (CollectionUtils.isEmpty(csvDeliveryProfiles)) {
      throw new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_001,
          "The imported delivery profile data is empty");
    }
    List<DeliveryProfile> deliveryProfiles = csvDeliveryProfiles.stream()
        .map(DeliveryProfileConverters.deliveryProfileConverter()).collect(Collectors.toList());

    List<Integer> branches =
        branchRepo.findAll().stream().map(Branch::getBranchNr).collect(Collectors.toList());

    boolean nonExistedDeliveryProfileId =
        deliveryProfiles.stream().map(DeliveryProfile::getDeliveryBranchId).filter(Objects::nonNull)
            .anyMatch(deliveryBranchId -> !branches.contains(deliveryBranchId));
    if (nonExistedDeliveryProfileId) {
      throw new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_008,
          "Csv file import is used to delivery branch id is not existed in database");
    }

    boolean nonExistedDistribution = deliveryProfiles.stream()
        .map(DeliveryProfile::getDistributionBranchId).filter(Objects::nonNull)
        .anyMatch(distributionBranchId -> !branches.contains(distributionBranchId));
    if (nonExistedDistribution) {
      throw new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_008,
          "Csv file import is used to distribution branch id is not existed in database");
    }

    checkDuplicateDeliveryBranchIdAndProfileId(deliveryProfiles);

    List<DeliveryProfile> invalidItems = deliveryProfiles.stream()
        .collect(DeliveryProfileValidateCollector.toObjectDeliveryProfile());
    if (CollectionUtils.isNotEmpty(invalidItems)) {
      throw new DeliveryProfileValidationException(
          DeliveryProfileErrorCase.ODE_EMP_002, ODE_EMP_002_MSG);
    }
    deliveryProfileRepo.deleteAll();
    deliveryProfileRepo.saveAll(deliveryProfiles);
  }

  private static void checkDuplicateDeliveryBranchIdAndProfileId(
      List<DeliveryProfile> deliveryProfiles) throws DeliveryProfileValidationException {
    final Map<Integer, List<Integer>> deliveryProfileIdAndBranchIds = new HashMap<>();
    deliveryProfiles.forEach(p -> deliveryProfileIdAndBranchIds.compute(p.getDeliveryProfileId(),
        (deliveryProfileId, branchIds) -> {
          if (branchIds == null) {
            branchIds = new ArrayList<>();
          }
          branchIds.add(p.getDeliveryBranchId());
          return branchIds;
        }));

    log.debug("deliveryProfileIdAndBranchIds ={}",
        SagJSONUtil.convertObjectToPrettyJson(deliveryProfileIdAndBranchIds));
    for (Entry<Integer, List<Integer>> entry : deliveryProfileIdAndBranchIds.entrySet()) {
      List<Integer> distinctedDeliveryBranchId = entry.getValue().stream().distinct()
          .collect(Collectors.toList());
      if (CollectionUtils.size(entry.getValue()) != distinctedDeliveryBranchId.size()) {
        throw new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_006,
            "CSV file import have duplicate delivery branch id");
      }
    }
  }

  @Override
  public Page<VDeliveryProfile> searchDeliveryProfile(
      DeliveryProfileSearchCriteria searchCriteria) {
    final List<CountryDto> countries = countryService.getSupportedCountries();
    Specification<VDeliveryProfile> spec =
        DeliveryProfileSpecifications.searchDeliveryProfile(searchCriteria);
    final Pageable pageable = searchCriteria.buildPageable();
    return vDeliveryProfileRepo.findAll(spec, pageable)
        .map(de -> deliveryProfileOperator(countries).apply(de));
  }

  private static UnaryOperator<VDeliveryProfile> deliveryProfileOperator(
      final List<CountryDto> countries) {
    return deliveryProfile -> {
      final VDeliveryProfile clonedDeliveryProfile = ObjectUtils.cloneIfPossible(deliveryProfile);
      if (CollectionUtils.isEmpty(countries)) {
        clonedDeliveryProfile.setCountry(StringUtils.EMPTY);
        return clonedDeliveryProfile;
      }
      clonedDeliveryProfile.setCountry(countries.stream()
          .filter(c -> c.getCode().equalsIgnoreCase(deliveryProfile.getCountry()))
          .findFirst()
          .map(CountryDto::getDescription)
          .orElse(StringUtils.EMPTY));
      return clonedDeliveryProfile;
    };
  }

  @Override
  public SupportDeliveryProfileDto getMasterDataDeliveryProfile() {

    List<BranchRequestBody> branchNr = branchRepo.findAll().stream()
        .map(BranchConverters.convertToRequest()).collect(Collectors.toList());
    List<CountryDto> countries = countryService.getSupportedCountries();
    List<BranchRequestBody> distributionBranches =
        deliveryProfileRepo.findBranchCodeByDistributionBranchId().stream()
            .map(BranchConverters.convertToRequest()).collect(Collectors.toList());
    List<BranchRequestBody> deliveryBranches =
        deliveryProfileRepo.findBranchCodeByDeliveryBranchId().stream()
            .map(BranchConverters.convertToRequest()).collect(Collectors.toList());
    return SupportDeliveryProfileDto.builder().branchNr(branchNr).countries(countries)
        .distributionBranchSearch(distributionBranches).deliveryBranchSearch(deliveryBranches)
        .build();
  }

  @Override
  public DeliveryProfileDto findProfileNameByDeliveryProfileId(Integer deliveryProfileId) {
    return deliveryProfileRepo
        .findAll(Example.of(DeliveryProfile.builder().deliveryProfileId(deliveryProfileId).build()))
        .stream().findFirst().map(DeliveryProfileConverters.deliveryProfileToProfileNameConverter())
        .orElseGet(DeliveryProfileDto::new);
  }

  private Optional<Integer> findDeliveryProfileIdByDeliveryProfileName(String profileName) {
    return deliveryProfileRepo
        .findAll(Example.of(DeliveryProfile.builder().deliveryProfileName(profileName).build()))
        .stream().findFirst().map(DeliveryProfile::getDeliveryProfileId);
  }

  private void checkDuplicateProfileName(int inputProfileId, int existedDeliveryProfileId,
      String deliveryProfileName) throws DeliveryProfileValidationException {
    if (existedDeliveryProfileId != inputProfileId) {
      throw duplicateDeliveryProfileNameSupplier(deliveryProfileName, existedDeliveryProfileId)
          .get();
    }
  }
}
