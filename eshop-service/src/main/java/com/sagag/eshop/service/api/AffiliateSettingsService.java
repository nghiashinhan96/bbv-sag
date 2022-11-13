package com.sagag.eshop.service.api;

import com.sagag.eshop.service.exception.OrganisationCollectionException;
import com.sagag.services.domain.eshop.dto.AvailabilitySettingMasterDataDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingRequest;

public interface AffiliateSettingsService {

  /**
   * Updates affiliate setting.
   *
   * @param requestBody the setting to update
   * @throws OrganisationCollectionException the organisation collection exception
   */
  void updateSettings(BoAffiliateSettingRequest requestBody) throws OrganisationCollectionException;

  AvailabilitySettingMasterDataDto getAvailabilitySettingMasterData();
}
