package com.sagag.eshop.service.api;

import com.sagag.eshop.service.exception.WssDeliveryProfileValidationException;
import com.sagag.eshop.service.exception.WssDeliveryProfileValidationException.WssDeliveryProfileErrorCase;
import com.sagag.services.common.enums.WssDeliveryProfileDay;
import com.sagag.services.domain.eshop.criteria.WssDeliveryProfileSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.function.Supplier;

public interface WssDeliveryProfileService {

  /**
   * Creates WSS delivery profile.
   *
   * @param profile {@link WssDeliveryProfileRequestDto}
   * @param orgId
   * @throws WssDeliveryProfileValidationException
   */
  void createWssDeliveryProfile(WssDeliveryProfileRequestDto profile, Integer orgId)
      throws WssDeliveryProfileValidationException;

  /**
   * Updates WSS delivery profile.
   *
   * @param profile {@link WssDeliveryProfileRequestDto}
   * @param orgId
   * @throws WssDeliveryProfileValidationException
   */
  void updateWssDeliveryProfile(WssDeliveryProfileRequestDto updatedProfile, Integer orgId)
      throws WssDeliveryProfileValidationException;

  /**
   * Remove WSS delivery profile.
   *
   * @param deliveryProfileId
   * @param orgId
   * @throws WssDeliveryProfileValidationException
   */
  void removeWssDeliveryProfile(Integer deliveryProfileId, Integer orgId)
      throws WssDeliveryProfileValidationException;

  /**
   * Add delivery tour to existing WSS delivery profile.
   *
   * @param updatedProfile {@link WssDeliveryProfileRequestDto}
   * @param orgId
   * @throws WssDeliveryProfileValidationException
   */
  void addWssDeliveryProfileTour(WssDeliveryProfileRequestDto updatedProfile, Integer orgId)
      throws WssDeliveryProfileValidationException;

  /**
   * Update delivery tour of existing WSS delivery profile.
   *
   * @param updatedProfile {@link WssDeliveryProfileRequestDto}
   * @param orgId
   * @throws WssDeliveryProfileValidationException
   */
  void updateWssDeliveryProfileTour(WssDeliveryProfileRequestDto updatedProfile, Integer orgId)
      throws WssDeliveryProfileValidationException;

  /**
   * Search existing WSS delivery profile.
   *
   * @param criteria {@link WssDeliveryProfileSearchRequestCriteria}
   * @param orgId
   * @throws WssDeliveryProfileValidationException
   */
  Page<WssDeliveryProfileDto> searchDeliveryProfileByCriteria(
      WssDeliveryProfileSearchRequestCriteria criteria, Pageable pageable);

  /**
   * Remove delivery tour of existing WSS delivery profile.
   *
   * @param deliveryProfileTourId
   * @param orgId
   * @throws WssDeliveryProfileValidationException
   */
  void removeWssDeliveryProfileTour(Integer deliveryProfileTourId, Integer orgId)
      throws WssDeliveryProfileValidationException;

  /**
   * Get existing WSS delivery profile detail by id.
   *
   * @param wssDeliveryProfileTourId
   * @param orgId
   *
   * @return {@link WssDeliveryProfileDto}
   * @throws WssDeliveryProfileValidationException
   */
  WssDeliveryProfileDto getWssDeliveryProfileDetail(Integer wssDeliveryProfileTourId, Integer orgId)
      throws WssDeliveryProfileValidationException;

  default Supplier<WssDeliveryProfileValidationException> notFoundWssDeliveryProfileExceptionSupplier(
      final Integer deliveryProfileId) {
    return () -> new WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase.ODE_EMP_003,
        String.format("Not found WSS delivery profile id = %d", deliveryProfileId));
  }

  default Supplier<WssDeliveryProfileValidationException> notFoundWssDeliveryProfileTourExceptionSupplier(
      final Integer deliveryProfileTourId) {
    return () -> new WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase.ODE_EMP_009,
        String.format("Not found WSS delivery profile tour id = %d", deliveryProfileTourId));
  }

  default Supplier<WssDeliveryProfileValidationException> notFoundWssDeliveryProfileBranchExceptionSupplier(
      final Integer deliveryProfileTourId) {
    return () -> new WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase.ODE_EMP_010,
        String.format("Not found WSS delivery profile branch id = %d", deliveryProfileTourId));
  }

  default Supplier<WssDeliveryProfileValidationException> minimumWssDeliveryProfileTourListExceptionSupplier(
      final Integer deliveryProfileId, final Integer minimumDeliveryProfileTourList) {
    return () -> new WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase.ODE_EMP_006,
        String.format("WSS delivery profile id = %d required at least %d tour", deliveryProfileId,
            minimumDeliveryProfileTourList));
  }

  default Supplier<WssDeliveryProfileValidationException> pickUpDurationOrTourRequiredExceptionSupplier() {
    return () -> new WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase.ODE_EMP_004,
        "Pick up duration or tour is required");
  }

  default Supplier<WssDeliveryProfileValidationException> profileNameDuplicatedExceptionSupplier(
      String name) {
    return () -> new WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase.ODE_EMP_002,
        String.format("WSS delivery profile name %s duplicated", name));
  }

  default Supplier<WssDeliveryProfileValidationException> deliveryProfileIdAndOrgIdRequiredExceptionSupplier() {
    return () -> new WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase.ODE_EMP_004,
        "Delivery profile id and organisation id are required");
  }

  default Supplier<WssDeliveryProfileValidationException> profileTourDuplicatedExceptionSupplier(
      WssDeliveryProfileDay supplierDay, String supplierTourTime) {
    return () -> new WssDeliveryProfileValidationException(WssDeliveryProfileErrorCase.ODE_EMP_005,
        String.format("WSS delivery profile tour duplicated %s  %s", supplierDay.name(), supplierTourTime));
  }

}
