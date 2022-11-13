package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.CsvDeliveryProfileDto;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException;

import java.util.List;

/**
 * Interface to define services for Delivery Profile.
 */
public interface DeliveryProfileBusinessService {

  /**
   * Imports the CSV file for delivery profile.
   *
   * @param csvDeliveryProfiles the list CsvDeliveryProfileDto
   * @throws DeliveryProfileValidationException
   */
  void importAndRefreshCacheDeliveryProfile(List<CsvDeliveryProfileDto> csvDeliveryProfiles)
      throws DeliveryProfileValidationException;

  /**
   * Reloads the cache data of delivery profile list.
   *
   */
  void refreshCacheDeliveryProfile();
}
