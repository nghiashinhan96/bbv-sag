package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.VDeliveryProfile;
import com.sagag.eshop.service.dto.CsvDeliveryProfileDto;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException.DeliveryProfileErrorCase;
import com.sagag.services.domain.eshop.criteria.DeliveryProfileSearchCriteria;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileSavingDto;
import com.sagag.services.domain.eshop.dto.externalvendor.SupportDeliveryProfileDto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Supplier;

public interface DeliveryProfileService {

  /**
   * Creates delivery profile.
   *
   * @param profile
   * @param createdUserId
   * @throws DeliveryProfileValidationException
   */
  void createDeliveryProfile(DeliveryProfileSavingDto profile, Long createdUserId)
      throws DeliveryProfileValidationException;

  /**
   * Updates delivery profile.
   *
   * @param profile
   * @param modifiedUserId
   * @throws DeliveryProfileValidationException
   */
  void updateDeliveryProfile(DeliveryProfileSavingDto profile, Long modifiedUserId)
      throws DeliveryProfileValidationException;

  /**
   * Removes delivery profile.
   *
   * @param deliveryProfileId
   * @throws DeliveryProfileValidationException
   */
  void removeDeliveryProfile(Integer deliveryProfileId) throws DeliveryProfileValidationException;

  /**
   * Replaces the delivery profiles by CSV file.
   *
   * @param csvDeliveryProfiles
   * @throws DeliveryProfileValidationException
   */
  void replaceDeliveryProfilesByCsv(List<CsvDeliveryProfileDto> csvDeliveryProfiles)
      throws DeliveryProfileValidationException;

  default Supplier<DeliveryProfileValidationException> notFoundDeliveryProfileExceptionSupplier(
      final Integer deliveryProfileId) {
    return () -> new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_003,
        String.format("Not found delivery profile id = %d", deliveryProfileId));
  }

  default Supplier<DeliveryProfileValidationException> validationDeliveryProfileExceptionSupplier(
      final String message) {
    return () -> new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_004,
        message);
  }

  default Supplier<DeliveryProfileValidationException> duplicateDeliveryProfileExceptionSupplier(
      final Integer deliveryBranchId) {
    return () -> new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_006,
        String.format("Duplicate delivery branch id = %d", deliveryBranchId));
  }
  
  default Supplier<DeliveryProfileValidationException> duplicateDeliveryProfileNameSupplier(
      final String deliveryProfileName, Integer existedDeliveryProfileId) {
    return () -> new DeliveryProfileValidationException(DeliveryProfileErrorCase.ODE_EMP_007,
        String.format("Delivery profile name = %s existed with delivery profile id = %d",
            deliveryProfileName, existedDeliveryProfileId));
  }

  /**
   * Returns the delivery profile by criteria.
   * 
   * @param searchCriteria Search Criteria for Delivery Profile.
   * @return Page of {@link VDeliveryProfile}.
   */
  Page<VDeliveryProfile> searchDeliveryProfile(DeliveryProfileSearchCriteria searchCriteria);

  /**
   * Returns initialize the data for delivery profile.
   * 
   * @return SupportDeliveryProfileDto
   */
  SupportDeliveryProfileDto getMasterDataDeliveryProfile();
  
  /**
   * Returns delivery profile by delivery profile id
   * 
   * @param deliveryProfileId
   * @return DeliveryProfileDto 
   */
  DeliveryProfileDto findProfileNameByDeliveryProfileId (Integer deliveryProfileId);
}
