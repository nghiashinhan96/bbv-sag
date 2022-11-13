package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.eshop.service.exception.OrganisationCollectionException;
import com.sagag.services.domain.eshop.backoffice.dto.CollectionSearchResultDto;
import com.sagag.services.domain.eshop.criteria.CollectionSearchCriteria;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.AffiliateExternalPartSettingDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingAvailabiltyDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrganisationCollectionService {

  /**
   * Returns the collection permission by short-name.
   *
   * @param collectionShortName
   * @return the result object
   */
  OrganisationCollectionDto getCollectionPermissionForCustomer(String collectionShortName)
      throws OrganisationCollectionException;

  /**
   * Returns the list of organisation collection by affiliate id.
   *
   * @param affiliateId
   * @return the list of result.
   */
  List<OrganisationCollection> findByAffiliateId(Integer affiliateId);

  /**
   * Returns the collection info by short-name.
   *
   * @param collectionShortName
   * @return the result object
   * @throws OrganisationCollectionException
   */
  OrganisationCollectionDto getCollectionInfo(String collectionShortName)
      throws OrganisationCollectionException;

  /**
   * Returns the optional organisation collection by short-name.
   *
   * @param shortName
   * @return the optional of organisation collection
   */
  Optional<OrganisationCollection> getCollectionByShortName(String shortName);

  /**
   * Updates the organisation collection by request.
   *
   * @param request
   * @throws OrganisationCollectionException
   */
  void update(OrganisationCollectionDto request) throws OrganisationCollectionException;

  /**
   * Returns organisation collection template by affiliate short-name.
   *
   * @param affiliateShortName
   * @return the selected object
   */
  OrganisationCollectionDto getCollectionTemplate(String affiliateShortName);

  /**
   * Creates the organisation collection by request
   *
   * @param request
   * @throws OrganisationCollectionException
   */
  void create(OrganisationCollectionDto request) throws OrganisationCollectionException;

  /**
   * Returns the optional organisation collection by org id.
   *
   * @param orgId
   * @return the optional result
   */
  Optional<OrganisationCollection> getCollectionByOrgId(Integer orgId);

  /**
   * Returns the optional organisation collection.
   *
   * @param shortName
   * @return the optional result
   */
  Optional<OrganisationCollection> getCollectionByOrgShortName(String shortName);

  /**
   * Performs action add customer and all its final customer into collection
   *
   * @param customerOrgId customer to add
   * @param collectionId  collection to add
   */
  void addCustomerToCollection(Integer customerOrgId, Integer collectionId);

  /**
   * Returns the setting by collection id and key.
   *
   * @param collectionId
   * @param key
   * @return the optional result
   */
  Optional<OrganisationCollectionsSettings> findSettingsByCollectionIdAndKey(Integer collectionId,
      String key);

  /**
   * Returns the settings by organisation short-name.
   *
   * @param shortname
   * @return the settings map
   */
  Map<String, String> findSettingsByOrgShortname(String shortname);

  /**
   * Returns the settings by collection short-name.
   *
   * @param collectionShortname
   * @return the settings map
   */
  Map<String, String> findSettingsByCollectionShortname(String collectionShortname);

  /**
   * Returns the setting value optional by short-name and selected key.
   *
   * @param collectionShortname
   * @param key
   * @return the optional result
   */
  Optional<String> findSettingValueByCollectionShortnameAndKey(String collectionShortname,
      String key);

  /**
   * Returns the settings values by short-name and the list of keys.
   *
   * @param collectionShortname
   * @param keys
   * @return the result map
   */
  Map<String, String> findSettingValuesByCollectionShortnameAndKeys(String collectionShortname,
      List<String> keys);

  /**
   * Builds organisation collection settings map.
   *
   * @param orgCollectionSettings organisation collection settings list.
   * @return the result map
   */
  default Map<String, String> buildOrgCollectionSettingsMap(
      List<OrganisationCollectionsSettings> orgCollectionSettings) {
    if (CollectionUtils.isEmpty(orgCollectionSettings)) {
      return Collections.emptyMap();
    }
    final Map<String, String> settingsMap = new HashMap<>();
    for (OrganisationCollectionsSettings setting : orgCollectionSettings) {
      settingsMap.putIfAbsent(setting.getSettingKey(), setting.getSettingValue());
    }
    return settingsMap;
  }

  /**
   * Returns collection which matched with criteria.
   *
   * @param searchCriteria for searching.
   * @return Page of {@link CollectionSearchResultDto}.
   */
  Page<CollectionSearchResultDto> searchCollection(CollectionSearchCriteria searchCriteria);

  /**
   * Get all customer which matched with collection id.
   *
   * @param collectionShortName.
   * @param pageable.
   * @return Page of {@link String}.
   */
  Page<String> getAllCustomerNr(String collectionShortName, Pageable pageable);

  /**
   * Updates ABS setting for collection.
   *
   * @param collectionId
   * @param customerAbsEnabled
   */
  void updateAbsCustomerSetting(Integer collectionId, Boolean customerAbsEnabled);

  /**
   * Updates VAT setting for collection.
   *
   * @param collectionId
   * @param vat
   */
  void updateVatSetting(Integer collectionId, Double vat);

  /**
   * Updates ABS setting for collection.
   *
   * @param collectionId
   * @param salesAbsEnabled
   */
  void updateAbsSalesSetting(Integer collectionId, Boolean salesAbsEnabled);

  /**
   * Updates kso setting for collection.
   *
   * @param collectionId
   * @param isKsoEnabled
   */
  void updateKsoSetting(Integer collectionId, Boolean isKsoEnabled);

  /**
   * Updates invoiceRequestAllowed for collection
   *
   * @param collectionId
   * @param isInvoiceRequestAllowed
   */
  void updateInvoiceRequestAllowed(Integer collectionId, Boolean isInvoiceRequestAllowed);

  /**
   * Updates invoiceRequestEmail for collection
   *
   * @param collectionId
   * @param invoiceRequestEmail
   */
  void updateInvoiceRequestEmail(Integer collectionId, String invoiceRequestEmail);

  /**
   * Updates Vat Type Display setting.
   *
   * @param collectionId
   * @param vatTypeDisplay
   */
  void updateVatTypeDisplay(Integer collectionId, String vatTypeDisplay);
  
  
  /**
   * Updates customer brand priority availability filter setting.
   *
   * @param collectionId
   * @param customerBrandPriorityAvailFilter
   */
  void updateCustomerBrandPriorityAvailFilter(Integer collectionId,
      String customerBrandPriorityAvailFilter);

  /**
   * Updates customer for sales brand priority availability filter setting.
   *
   * @param collectionId
   * @param c4sBrandPriorityAvailFilter
   */
  void updateC4sBrandPriorityAvailFilter(Integer collectionId,
      String c4sBrandPriorityAvailFilter);

  /**
   * Updates availability for for selected affiliate.
   *
   * @param collectionId
   * @param availSetting
   */
  void updateAvailabilitySetting(Integer collectionId, BoAffiliateSettingAvailabiltyDto availSetting);

  /**
   * Updates availability display for for selected affiliate.
   *
   * @param collectionId
   * @param availDisplaySettings
   */
  void updateAvailabilityDisplaySetting(Integer collectionId,
      List<AffiliateAvailabilityDisplaySettingDto> availDisplaySettings);
  
  void updateBrandFilterCustomerSetting(Integer collectionId, Boolean customerBrandFilterEnabled);

  void updateBrandFilterSalesSetting(Integer collectionId, Boolean salesBrandFitlerEnabled);
  
  void updateDisabledBrandPriorityAvailabilitySetting(Integer collectionId,
      final Boolean disabledBrandPriorityAvailability);

  void updateExternalPartSetting(Integer collectionId, AffiliateExternalPartSettingDto extPartSetting);
}
