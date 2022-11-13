package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;

import java.util.Optional;

public interface VatTypeDisplaySettingService {

  /**
   * Returns the settings vat display by Org Short Name
   *
   * @param shortname
   * @return the settings map
   */
  Optional<OrganisationCollectionsSettings> findSettingsByOrgShortname(String shortname);

  /**
   * Returns the settings vat display by collection short-name.
   *
   * @param collectionShortname
   * @return the settings map
   */
  Optional<OrganisationCollectionsSettings> findSettingsByCollectionShortname(
      String collectionShortname);

  /**
   * Updates Vat Type Display setting.
   *
   * @param collectionId
   * @param vatTypeDisplay
   */
  void updateVatTypeDisplay(Integer collectionId, String vatTypeDisplay);
}
