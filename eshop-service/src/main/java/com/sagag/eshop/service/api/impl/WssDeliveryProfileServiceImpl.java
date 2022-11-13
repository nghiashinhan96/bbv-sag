package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.WssBranchRepository;
import com.sagag.eshop.repo.api.WssDeliveryProfileRepository;
import com.sagag.eshop.repo.api.WssDeliveryProfileToursRepository;
import com.sagag.eshop.repo.api.WssTourRepository;
import com.sagag.eshop.repo.entity.WssDeliveryProfile;
import com.sagag.eshop.repo.entity.WssDeliveryProfileTours;
import com.sagag.eshop.repo.specification.WssDeliveryProfileSpecifications;
import com.sagag.eshop.service.api.WssDeliveryProfileService;
import com.sagag.eshop.service.converter.WssDeliveryProfileConverters;
import com.sagag.eshop.service.exception.WssDeliveryProfileValidationException;
import com.sagag.eshop.service.exception.WssDeliveryProfileValidationException.WssDeliveryProfileErrorCase;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.criteria.WssDeliveryProfileSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileRequestDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Optional;

@Service
@Slf4j
public class WssDeliveryProfileServiceImpl implements WssDeliveryProfileService {

  private static final int DELIVERY_PROFILE_TOUR_MINIMUM = 1;

  private static final String WSS_ORG_ID_MUST_NOT_NULL_MSG = "The given WSS orgId must not be null";

  @Autowired
  private WssDeliveryProfileRepository wssDeliveryProfileRepo;

  @Autowired
  private WssBranchRepository wssBranchRepo;

  @Autowired
  private WssTourRepository wssTourRepo;

  @Autowired
  private WssDeliveryProfileToursRepository wssDeliveryProfileToursRepo;

  @Autowired
  private CustomerSettingsRepository customerSettingsRepo;

  @Override
  @Transactional
  public void createWssDeliveryProfile(WssDeliveryProfileRequestDto profile, Integer orgId)
      throws WssDeliveryProfileValidationException {
    Assert.notNull(profile, "The given WSS delivery profile must not be null");
    Assert.notNull(orgId, "The given wholesaler org Id must not be null");
    validate(profile, true, orgId);

    final WssDeliveryProfile deliveryProfile = WssDeliveryProfileConverters
        .wssDeliveryProfileEntityConverter().apply(profile, Optional.empty());
    Integer wssBranchId = profile.getWssBranchId();
    deliveryProfile.setWssBranch(wssBranchRepo.findByIdAndOrgId(wssBranchId, orgId)
        .orElseThrow(() -> notFoundWssDeliveryProfileBranchExceptionSupplier(wssBranchId).get()));
    WssDeliveryProfileTours wssDeliveryProfileTours =
        WssDeliveryProfileConverters.wssDeliveryProfileToursEntityConverter().apply(profile);
    wssDeliveryProfileTours.setWssDeliveryProfile(deliveryProfile);
    Integer wssTourId = profile.getWssTourId();
    if (wssTourId != null) {
      wssDeliveryProfileTours.setWssTour(wssTourRepo.findByIdAndOrgId(wssTourId, orgId)
          .orElseThrow(() -> notFoundWssDeliveryProfileTourExceptionSupplier(wssTourId).get()));
    }
    deliveryProfile.setWssDeliveryProfileTours(Arrays.asList(wssDeliveryProfileTours));
    deliveryProfile.setOrgId(orgId);
    wssDeliveryProfileRepo.save(deliveryProfile);
  }

  @Override
  @Transactional
  public void updateWssDeliveryProfile(WssDeliveryProfileRequestDto updatedProfile, Integer orgId)
      throws WssDeliveryProfileValidationException {
    Assert.notNull(updatedProfile, "The given updated delivery profile must not be null");
    Assert.notNull(orgId, WSS_ORG_ID_MUST_NOT_NULL_MSG);
    validate(updatedProfile, false, orgId);

    Integer wssDeliveryProfileId = updatedProfile.getId();
    Optional<WssDeliveryProfile> wssDeliveryProfileOpt =
        wssDeliveryProfileRepo.findByIdAndOrgId(wssDeliveryProfileId, orgId);

    if (!wssDeliveryProfileOpt.isPresent()) {
      throw notFoundWssDeliveryProfileExceptionSupplier(wssDeliveryProfileId).get();
    }
    WssDeliveryProfile wssDeliveryProfile = wssDeliveryProfileOpt.get();
    wssDeliveryProfile.setName(updatedProfile.getName());
    wssDeliveryProfile.setDescription(updatedProfile.getDescription());
    Integer wssBranchId = updatedProfile.getWssBranchId();
    if (wssDeliveryProfile.getWssBranch().getId() != wssBranchId) {
      wssDeliveryProfile.setWssBranch(wssBranchRepo.findByIdAndOrgId(wssBranchId, orgId)
          .orElseThrow(() -> notFoundWssDeliveryProfileBranchExceptionSupplier(wssBranchId).get()));
    }
    wssDeliveryProfileRepo.save(wssDeliveryProfile);
  }

  @Override
  @Transactional
  public void removeWssDeliveryProfile(Integer wssDeliveryProfileId, Integer orgId)
      throws WssDeliveryProfileValidationException {
    Assert.notNull(wssDeliveryProfileId, "The given WSS delivery profile id must not be null");
    if (!wssDeliveryProfileRepo.findByIdAndOrgId(wssDeliveryProfileId, orgId).isPresent()) {
      throw notFoundWssDeliveryProfileExceptionSupplier(wssDeliveryProfileId).get();
    }
    if (customerSettingsRepo.checkDeliveryProfileBeingUsed(wssDeliveryProfileId)) {
      throw new WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase.ODE_DPU_001,
          "This WSS delivery profile is being assigned to final customer: " + wssDeliveryProfileId);
    }

    wssDeliveryProfileRepo.deleteById(wssDeliveryProfileId);
  }

  @Override
  @Transactional
  public Page<WssDeliveryProfileDto> searchDeliveryProfileByCriteria(
      final WssDeliveryProfileSearchRequestCriteria criteria, final Pageable pageable) {
    log.debug("Search WSS branches by criteria = {}, pageable = {}", criteria, pageable);

    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<WssDeliveryProfile> specForDeliveryProfile =
        WssDeliveryProfileSpecifications.searchDeliveryProfileByCriteria(criteria);
    return wssDeliveryProfileRepo.findAll(specForDeliveryProfile, pageable)
        .map(WssDeliveryProfileConverters.optionalDeliveryProfileConverter());
  }

  @Override
  @Transactional
  public void addWssDeliveryProfileTour(WssDeliveryProfileRequestDto updatedProfile, Integer orgId)
      throws WssDeliveryProfileValidationException {
    Assert.notNull(updatedProfile, "The given updated delivery profile must not be null");
    Assert.notNull(orgId, WSS_ORG_ID_MUST_NOT_NULL_MSG);
    validateDeliveryTour(updatedProfile);
    checkDuplicateDeliveryProfileTour(updatedProfile, orgId, true);

    Optional<WssDeliveryProfile> wssDeliveryProfileOpt =
        wssDeliveryProfileRepo.findByIdAndOrgId(updatedProfile.getId(), orgId);

    if (!wssDeliveryProfileOpt.isPresent()) {
      throw notFoundWssDeliveryProfileExceptionSupplier(updatedProfile.getId()).get();
    }
    WssDeliveryProfile wssDeliveryProfile = wssDeliveryProfileOpt.get();

    WssDeliveryProfileTours wssDeliveryProfileTours =
        WssDeliveryProfileConverters.wssDeliveryProfileToursEntityConverter().apply(updatedProfile);
    wssDeliveryProfileTours.setWssDeliveryProfile(wssDeliveryProfile);

    Integer wssTourId = updatedProfile.getWssTourId();
    if (wssTourId != null) {
      wssDeliveryProfileTours.setWssTour(wssTourRepo.findByIdAndOrgId(wssTourId, orgId)
          .orElseThrow(notFoundWssDeliveryProfileTourExceptionSupplier(wssTourId)));
    }

    wssDeliveryProfileToursRepo.save(wssDeliveryProfileTours);
  }

  @Override
  @Transactional
  public void updateWssDeliveryProfileTour(WssDeliveryProfileRequestDto updatedProfile,
      Integer orgId) throws WssDeliveryProfileValidationException {
    Assert.notNull(updatedProfile, "The given updated WSS delivery profile must not be null");
    Assert.notNull(orgId, WSS_ORG_ID_MUST_NOT_NULL_MSG);
    Assert.notNull(updatedProfile.getWssDeliveryProfileTourId(),
        "The given WSS delivery profile tour id must not be null");
    validateDeliveryTour(updatedProfile);
    checkDuplicateDeliveryProfileTour(updatedProfile, orgId, false);

    Integer wssDeliveryProfileTourId = updatedProfile.getWssDeliveryProfileTourId();
    Optional<WssDeliveryProfileTours> wssDeliveryProfileTourOpt =
        wssDeliveryProfileToursRepo.findById(wssDeliveryProfileTourId);

    if (!wssDeliveryProfileTourOpt.isPresent()) {
      throw notFoundWssDeliveryProfileExceptionSupplier(wssDeliveryProfileTourId).get();
    }
    WssDeliveryProfileTours wssDeliveryProfileTours = wssDeliveryProfileTourOpt.get();
    wssDeliveryProfileTours.setOverNight(updatedProfile.getIsOverNight());
    wssDeliveryProfileTours.setPickupWaitDuration(updatedProfile.getPickupWaitDuration());
    wssDeliveryProfileTours.setSupplierDepartureTime(DateUtils
        .convertStringToTime(updatedProfile.getSupplierDepartureTime(), DateUtils.TIME_PATTERN));
    wssDeliveryProfileTours.setSupplierTourDay(updatedProfile.getSupplierTourDay());

    Integer wssTourId = updatedProfile.getWssTourId();
    if (wssTourId != null) {
      wssDeliveryProfileTours.setWssTour(wssTourRepo.findByIdAndOrgId(wssTourId, orgId)
          .orElseThrow(() -> notFoundWssDeliveryProfileTourExceptionSupplier(wssTourId).get()));
    } else {
      wssDeliveryProfileTours.setWssTour(null);
    }

    wssDeliveryProfileToursRepo.save(wssDeliveryProfileTours);
  }

  @Override
  @Transactional
  public void removeWssDeliveryProfileTour(Integer deliveryProfileTourId, Integer orgId)
      throws WssDeliveryProfileValidationException {
    Assert.notNull(deliveryProfileTourId,
        "The given updated WSS delivery profile tour must not be null");
    Assert.notNull(orgId, WSS_ORG_ID_MUST_NOT_NULL_MSG);

    Optional<WssDeliveryProfileTours> wssDeliveryProfileTourOpt =
        wssDeliveryProfileToursRepo.findById(deliveryProfileTourId);

    if (!wssDeliveryProfileTourOpt.isPresent()) {
      throw notFoundWssDeliveryProfileExceptionSupplier(deliveryProfileTourId).get();
    }

    WssDeliveryProfileTours wssDeliveryProfileTours = wssDeliveryProfileTourOpt.get();
    Integer wssDeliveryProfileId = wssDeliveryProfileTours.getWssDeliveryProfile().getId();
    Optional<WssDeliveryProfile> wssDeliveryProfileOpt =
        wssDeliveryProfileRepo.findByIdAndOrgId(wssDeliveryProfileId, orgId);

    if (!wssDeliveryProfileOpt.isPresent()) {
      throw notFoundWssDeliveryProfileExceptionSupplier(wssDeliveryProfileId).get();
    }
    WssDeliveryProfile wssDeliveryProfile = wssDeliveryProfileOpt.get();
    if (wssDeliveryProfile.getWssDeliveryProfileTours().size() <= DELIVERY_PROFILE_TOUR_MINIMUM) {
      throw minimumWssDeliveryProfileTourListExceptionSupplier(wssDeliveryProfileId,
          DELIVERY_PROFILE_TOUR_MINIMUM).get();
    }
    wssDeliveryProfile.getWssDeliveryProfileTours().remove(wssDeliveryProfileTours);
    wssDeliveryProfileRepo.save(wssDeliveryProfile);
    wssDeliveryProfileToursRepo.delete(wssDeliveryProfileTours);

  }

  @Override
  @Transactional
  public WssDeliveryProfileDto getWssDeliveryProfileDetail(Integer wssDeliveryProfileTourId,
      Integer orgId) throws WssDeliveryProfileValidationException {
    if (wssDeliveryProfileTourId == null || orgId == null) {
      throw deliveryProfileIdAndOrgIdRequiredExceptionSupplier().get();
    }
    return WssDeliveryProfileConverters.convertToDeliveryProfileDto(
        wssDeliveryProfileRepo.findByIdAndOrgId(wssDeliveryProfileTourId, orgId).orElseThrow(
            () -> notFoundWssDeliveryProfileExceptionSupplier(wssDeliveryProfileTourId).get()));
  }

  private void validate(final WssDeliveryProfileRequestDto request, final boolean isCreatingMode,
      final int orgId)
      throws WssDeliveryProfileValidationException {
    validateDeliveryProfileName(request, isCreatingMode, orgId);
    if (isCreatingMode) {
      validateDeliveryTour(request);
    }
  }

  private void validateDeliveryTour(WssDeliveryProfileRequestDto request)
      throws WssDeliveryProfileValidationException {
    if (request.getPickupWaitDuration() == null && request.getWssTourId() == null) {
      throw pickUpDurationOrTourRequiredExceptionSupplier().get();
    }
  }

  private void checkDuplicateDeliveryProfileTour(WssDeliveryProfileRequestDto request, int orgId, boolean isCreatingMode)
      throws WssDeliveryProfileValidationException {
    if (isCreatingMode) {
      if (wssDeliveryProfileToursRepo
          .checkExistDeliveryProfileTourByProfileIdOrgIdAndSupplierDayAndSupplierTour(
              request.getId(), orgId,
              request.getSupplierTourDay().name(), request.getSupplierDepartureTime())) {
        throw profileTourDuplicatedExceptionSupplier(request.getSupplierTourDay(),
            request.getSupplierDepartureTime()).get();
      }
    } else {
      if (wssDeliveryProfileToursRepo
          .checkExistDeliveryProfileTourByByProfileIdAndIdAndOrgIdAndSupplierDayAndSupplierTour(
              request.getId(), request.getWssDeliveryProfileTourId(), orgId,
              request.getSupplierTourDay().name(), request.getSupplierDepartureTime())) {
        throw profileTourDuplicatedExceptionSupplier(request.getSupplierTourDay(),
            request.getSupplierDepartureTime()).get();
      }
    }
  }


  private void validateDeliveryProfileName(WssDeliveryProfileRequestDto request,
      boolean isCreatingMode, int orgId) throws WssDeliveryProfileValidationException {
    if (isCreatingMode) {
      if (wssDeliveryProfileRepo.checkExistDeliveryProfileNameByOrgId(request.getName(), orgId)) {
        throw profileNameDuplicatedExceptionSupplier(request.getName()).get();
      }
    } else {
      if (wssDeliveryProfileRepo.checkExistDeliveryProfileNameByIdAndOrgId(request.getId(),
          request.getName(), orgId)) {
        throw profileNameDuplicatedExceptionSupplier(request.getName()).get();
      }
    }
  }

}
