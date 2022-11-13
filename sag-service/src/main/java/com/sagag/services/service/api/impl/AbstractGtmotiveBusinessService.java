package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.api.VinLoggingRepository;
import com.sagag.eshop.repo.entity.VinLogging;
import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.LicenseType;
import com.sagag.services.common.enums.VinLogStatus;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.GTMotiveLinkDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.VinLoggingDto;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.domain.request.AbstractGtmotiveCriteria;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;
import com.sagag.services.hazelcast.api.VinOrderCacheService;
import com.sagag.services.hazelcast.globalsetting.GtmotiveGlobalSettingPredicates;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.response.GtmotiveResponse;
import com.sagag.services.service.gtmotive.GtmotiveLastVehicleFinder;
import com.sagag.services.service.gtmotive.GtmotiveVinSecurityChecker;
import com.sagag.services.service.gtmotive.processor.GtmotiveReferenceSearchProcessor;
import com.sagag.services.service.utils.NormautoMailBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public abstract class AbstractGtmotiveBusinessService {

  @Autowired
  protected GtmotiveService gtmotiveService;

  @Autowired
  protected NormautoMailBuilder normautoMailBuilder;

  @Autowired
  protected LicenseService licenseService;

  @Autowired
  protected VinOrderCacheService vinOrderCacheService;

  @Autowired
  protected VinLoggingRepository vinLoggingRepo;

  @Autowired
  protected IvdsVehicleService ivdsVehicleService;

  @Autowired
  protected GtmotiveLastVehicleFinder gtmotiveLastVehicleFinder;

  @Autowired
  protected GtmotiveVinSecurityChecker gtmotiveVinSecurityChecker;

  @Autowired
  protected GtmotiveReferenceSearchProcessor gtmotiveReferenceSearchProcessor;

  @Autowired
  protected GtmotiveGlobalSettingPredicates gtmotiveGlobalSettingPredicates;

  /**
   * Updates the existed estimate id or generate new once.
   *
   * @param criteria the gtmotive criteria
   * @param customerNr the customer number
   * @return a existed or new estimate id {@link String}
   */
  protected void updateExistedOrDefaultEstimateId(final AbstractGtmotiveCriteria criteria,
      final String customerNr) {
    // #1022: If vin is not empty, will get used estimate id from DB instead of creating
    Optional<String> estimatedId = Optional.empty();
    if (criteria.isVinRequest()) {
      estimatedId =
          vinLoggingRepo.findByEstimateUsed(criteria.getModifiedVin(), Long.valueOf(customerNr));
    }
    if (estimatedId.isPresent()) {
      // #870: Update estimate id request
      criteria.setEstimateId(estimatedId.get());
      criteria.setUpdateEstimateIdMode(true);
    } else {
      // #576: suggest from PO, we use the customerID plus time to seconds
      criteria.setEstimateId(GtmotiveUtils.generateEstimateId(customerNr));
    }
  }

  protected BiFunction<UserInfo, GtmotiveResponse, GtmotiveResponse> processResponseData(
      final String estimateId, final String vin, final String vehicleCode) {
    return (user, responseData) -> {

      // #870: Add Vin Log, #1652 to add after receive the response
      if (!StringUtils.isBlank(vin)) {
        // #870: Update vehicle for vin log
        final VinLoggingDto loggingInfo = VinLoggingDto.buildLogInfo(
            Long.valueOf(user.getCustNrStr()), estimateId, vin, responseData.getVehicleId());
        addVinLog(user, loggingInfo);
      }

      // #1066: Set email content to send to normauto
      if (responseData.isNormauto()) {
        responseData
            .setMailContent(normautoMailBuilder.build(user, vin, vehicleCode, responseData));
      }
      return responseData;
    };
  }

  /**
   * Adds a VIN logging for historical data of GTMotive request.
   *
   * @param user the user who requests
   * @param vinInfo the VIN logging information
   * @return the VIN logging created id
   */
  protected void addVinLog(final UserInfo user, final VinLoggingDto vinInfo) {
    final String customerNr = user.getCustNrStr();
    final Long custNr = Long.valueOf(customerNr);
    final String existingEstimateId =
        vinLoggingRepo.findByEstimateUsed(vinInfo.getVin(), custNr).orElse(StringUtils.EMPTY);
    final VinLogStatus vinLogStatus = VinLogStatus.findEstimateStatus(existingEstimateId);
    vinInfo.setStatus(vinLogStatus.getStatus());

    final Long userId = user.getId();
    final VinLogging vinLogging = SagBeanUtils.map(vinInfo, VinLogging.class);
    vinLogging.setUserId(userId);
    vinLogging.setSalesUserId(user.getSalesId());
    vinLoggingRepo.save(vinLogging);

    if (vinLogStatus.isOk() && !isEnabledVinSearchForSalesOnbehalf(user)) {
      licenseService.increaseQuantityUsed(userId, customerNr, LicenseType.VIN);
      vinOrderCacheService.increaseSearchCount(user.key());
    }
  }

  protected boolean isEnabledVinSearchForSalesOnbehalf(UserInfo user) {
    final Predicate<UserInfo> predicate = UserInfo::isSaleOnBehalf;
    return gtmotiveGlobalSettingPredicates.andPredicates(Arrays.asList(predicate)).test(user);
  }

  protected static GtmotiveVehicleDto excludeSpecialEquipments(GtmotiveVehicleDto gtVehicle) {
    if (CollectionUtils.isNotEmpty(gtVehicle.getEquipmentItems())) {
      gtVehicle.getEquipmentItems()
          .removeIf(GtmotiveUtils.startsWithIgnoreCaseSpecialEquipmentPrefixPredicate());
    }
    return gtVehicle;
  }

  protected static VehicleDto findMatchedModel(VehicleDto veh, String umc) {
    List<GTMotiveLinkDto> gtLinks = veh.getGtLinks();
    if (CollectionUtils.size(gtLinks) > 1) {
      GTMotiveLinkDto matchedGtlink = gtLinks.stream()
          .filter(item -> umc.equals(item.getGtMakeCode())).findFirst().orElse(null);
      veh.setGtLinks(Arrays.asList(matchedGtlink));
    }
    return veh;
  }

}
