package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.collection.CollectionPermissionRepository;
import com.sagag.eshop.repo.api.collection.CollectionRelationRepository;
import com.sagag.eshop.repo.api.collection.OrganisationCollectionRepository;
import com.sagag.eshop.repo.api.collection.OrganisationCollectionsSettingsRepository;
import com.sagag.eshop.repo.api.collection.VCollectionRepository;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate;
import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate.Settings;
import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate.Theme;
import com.sagag.eshop.repo.entity.VCollectionSearch;
import com.sagag.eshop.repo.entity.collection.CollectionPermission;
import com.sagag.eshop.repo.entity.collection.CollectionRelation;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.eshop.repo.specification.VCollectionSearchSpecifications;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.VatTypeDisplaySettingService;
import com.sagag.eshop.service.converter.CollecitonConverters;
import com.sagag.eshop.service.converter.OrganisationCollectionConverters;
import com.sagag.eshop.service.exception.OrganisationCollectionException;
import com.sagag.eshop.service.permission.configuration.BackOfficeCollectionPermissionConfiguration;
import com.sagag.eshop.service.utils.OrganisationCollectionUtils;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.OrganisationTypeEnum;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.backoffice.dto.CollectionSearchResultDto;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.criteria.CollectionSearchCriteria;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.AffiliateExternalPartSettingDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingAvailabiltyDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto.OrganisationCollectionDtoBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class OrganisationCollectionServiceImpl implements OrganisationCollectionService {

  private static final String MISSING_ORG_ID = "OrganisationId cannot be null";

  private static final String MISSING_ORG_SHORTNAME = "Organisation short name cannot be empty";

  private static final Integer MAX_CUSTOMER_ALLOW_TO_UPDATE = 100;

  private static final String[] UPDATE_SETTING_KEYS = { Theme.LOGO_IMAGE.toLowerName(),
      Settings.TITLE.toLowerName() };

  private static final Pageable DEFAULT_PAGEABLE = 
    PageUtils.defaultPageable(MAX_CUSTOMER_ALLOW_TO_UPDATE);

  @Autowired
  private OrganisationCollectionRepository orgCollectionRepo;

  @Autowired
  private CollectionRelationRepository collectionRelationRepo;

  @Autowired
  private CollectionPermissionRepository collPermissionRepo;

  @Autowired
  private OrganisationCollectionsSettingsRepository orgCollectionSettingsRepo;

  @Autowired
  private BackOfficeCollectionPermissionConfiguration collectionPermissionConfiguration;

  @Autowired
  private OrganisationRepository organisationRepository;

  @Autowired
  private VCollectionRepository vCollectionRepo;

  @Autowired
  private LanguageRepository languageRepo;
  
  @Autowired
  private VatTypeDisplaySettingService vatTypeDisplaySettingService;
  
  @Override
  public OrganisationCollectionDto getCollectionPermissionForCustomer(String collectionShortName)
      throws OrganisationCollectionException {
    final OrganisationCollection collection = getCollectionByShortName(collectionShortName)
        .orElseThrow(OrganisationCollectionException.collectionNotFound(collectionShortName));
    final Organisation affiliate = organisationRepository.findByOrgId(collection.getAffiliateId())
        .orElseThrow(OrganisationCollectionException.affiliateNotFound());
    final List<CollectionPermission> maxPermissionForCollection =
        collPermissionRepo.findByCollectionShortName(collection.getShortname());
    final List<PermissionConfigurationDto> collecionPermissions =
        collectionPermissionConfiguration.getPermissions(affiliate.getShortname(),
            maxPermissionForCollection, true);
    return OrganisationCollectionConverters.toCollectionInfo(collecionPermissions)
        .apply(collection);
  }

  @Override
  public List<OrganisationCollection> findByAffiliateId(Integer affiliateId) {
    if (affiliateId == null) {
      return Collections.emptyList();
    }
    return orgCollectionRepo.findByAffiliateId(affiliateId);
  }

  @Override
  public OrganisationCollectionDto getCollectionInfo(String collectionShortName)
      throws OrganisationCollectionException {
    final OrganisationCollection collection = getCollectionByShortName(collectionShortName)
        .orElseThrow(OrganisationCollectionException.collectionNotFound(collectionShortName));
    final Organisation affiliate = organisationRepository.findByOrgId(collection.getAffiliateId())
        .orElseThrow(OrganisationCollectionException.affiliateNotFound());
    final String affiliateShortname = affiliate.getShortname();

    final List<CollectionPermission> maxPermissionForCollection =
        collPermissionRepo.findByCollectionShortName(collection.getShortname());
    final List<PermissionConfigurationDto> collectionPermissions =
        collectionPermissionConfiguration.getPermissions(affiliateShortname,
          maxPermissionForCollection, false);

    OrganisationCollectionDto collectionDto =
        OrganisationCollectionConverters.toCollectionInfo(collectionPermissions).apply(collection);
    collectionDto.setIsDefault(
        getCollectionByOrgId(affiliate.getId()).map(OrganisationCollection::getShortname)
            .filter(shortName -> shortName.equals(collectionShortName)).isPresent());
    Map<String, String> setting = findSettingsByCollectionShortname(collectionShortName);
    Stream.of(UPDATE_SETTING_KEYS).forEach(key -> collectionDto.addSetting(key, setting.get(key)));
    collectionDto.setAffiliateShortName(affiliateShortname);
    return collectionDto;
  }

  @Override
  public Optional<OrganisationCollection> getCollectionByShortName(String shortName) {
    if (StringUtils.isBlank(shortName)) {
      return Optional.empty();
    }
    return orgCollectionRepo.findByShortname(shortName);
  }

  @Override
  @Transactional
  public void update(OrganisationCollectionDto request) throws OrganisationCollectionException {
    // Validation
    OrganisationCollectionUtils.validateCollectionRequest(request);
    Integer affiliate = organisationRepository.findIdByShortName(request.getAffiliateShortName())
        .orElseThrow(OrganisationCollectionException.affiliateNotFound());
    OrganisationCollection existedCollection =
        orgCollectionRepo.findByShortname(request.getCollectionShortName())
            .orElseThrow(OrganisationCollectionException
              .collectionNotFound(request.getCollectionShortName()));
    if (!existedCollection.getName().equals(request.getName())
        && orgCollectionRepo.findByName(request.getName()).isPresent()) {
      throw OrganisationCollectionException.duplicatedCollectionName().get();
    }
    if (existedCollection.getAffiliateId() != affiliate) {
      throw OrganisationCollectionException.cannotChangeAffiliate().get();
    }

    // Save org collection
    existedCollection.setName(request.getName());
    orgCollectionRepo.save(existedCollection);
    updateSettings(request, existedCollection);

    // Update permission if change
    List<CollectionPermission> existedPermission =
        collPermissionRepo.findByCollectionShortName(request.getCollectionShortName());
    if (OrganisationCollectionUtils.isPermissionChanged(
      existedPermission, request.getPermissions())) {
      updatePermission(request, existedCollection, existedPermission);
    }
  }

  private void updateSettings(OrganisationCollectionDto request,
    OrganisationCollection existedCollection) {
    Map<String, String> affiliateSetting = findSettingsByOrgShortname(request.getAffiliateShortName());
    List<OrganisationCollectionsSettings> collectionSettings = Stream.of(UPDATE_SETTING_KEYS)
        .map(key -> orgCollectionSettingsRepo.findByCollectionIdAndKey(existedCollection.getId(), key)
            .orElseGet(OrganisationCollectionUtils.simpleSetting(existedCollection, key)))
        .collect(Collectors.toList());

    collectionSettings.stream().forEach(setting -> {
      String settingValue = StringUtils.defaultIfBlank(
          request.getSettings().get(setting.getSettingKey()),
          affiliateSetting.get(setting.getSettingKey()));
      setting.setSettingValue(settingValue);
    });
    orgCollectionSettingsRepo.saveAll(collectionSettings);
  }

  private void updatePermission(OrganisationCollectionDto request,
    OrganisationCollection existedCollection,
      List<CollectionPermission> existedPermission) throws OrganisationCollectionException {
    if (collectionRelationRepo
        .countByCollectionId(existedCollection.getId()) > MAX_CUSTOMER_ALLOW_TO_UPDATE) {
      throw OrganisationCollectionException.customerExtendLimit().get();
    }
    List<Integer> customerSettingsIds = orgCollectionRepo.findOrganisationSettingIdBy(
        existedCollection.getShortname(),
        OrganisationTypeEnum.CUSTOMER.name(),
        DEFAULT_PAGEABLE);

    List<Integer> finalCustomerSettingsIds = orgCollectionRepo.findOrganisationSettingIdBy(
        existedCollection.getShortname(),
        OrganisationTypeEnum.FINAL_CUSTOMER.name(),
        DEFAULT_PAGEABLE);
    request.setCustomerSettingIds(customerSettingsIds);
    request.setFinalCustomerSettingIds(finalCustomerSettingsIds);

    // validate and update permission for customer
    collectionPermissionConfiguration.updatePermisions(request);

    // remove old permission and create new one
    collPermissionRepo.deleteAll(existedPermission);
    saveCollectionPermission(request, existedCollection);
  }

  private void saveCollectionPermission(OrganisationCollectionDto request,
    OrganisationCollection existedCollection) {
    List<CollectionPermission> permissions = request.getPermissions().stream()
        .filter(PermissionConfigurationDto::isEnable)
        .map(p -> CollectionPermission.builder()
            .collectionId(existedCollection.getId())
            .eshopPermissionId(p.getPermissionId())
            .build())
        .collect(Collectors.toList());
    collPermissionRepo.saveAll(permissions);
  }

  @Override
  public OrganisationCollectionDto getCollectionTemplate(String affiliateShortName) {
    OrganisationCollectionDtoBuilder builder =
        OrganisationCollectionDto.builder().affiliateShortName(affiliateShortName);
    builder.permissions(collectionPermissionConfiguration.getPermissions(builder.build()));
    return builder.build();
  }

  @Override
  @Transactional
  public void create(OrganisationCollectionDto request) throws OrganisationCollectionException {
    // Validation
    OrganisationCollectionUtils.validateCollectionRequest(request);
    Integer affiliate = organisationRepository.findIdByShortName(request.getAffiliateShortName())
        .orElseThrow(OrganisationCollectionException.affiliateNotFound());
    if (orgCollectionRepo.findByName(request.getName()).isPresent()) {
      throw OrganisationCollectionException.duplicatedCollectionName().get();
    }

    //build shortname
    request.setCollectionShortName(
        OrganisationCollectionUtils.buildCollectionShortName(request.getAffiliateShortName()));

    // save org collection
    OrganisationCollection createdCollection =
        orgCollectionRepo.save(OrganisationCollection.builder().affiliateId(affiliate)
            .description("Description")
            .shortname(request.getCollectionShortName())
            .name(request.getName())
            .build());

    // save org collection setting base on affiliate
    List<OrganisationCollectionsSettings> settings =
        orgCollectionSettingsRepo.findSettingsByOrgShortname(request.getAffiliateShortName())
            .stream()
            .map(OrganisationCollectionUtils
              .toCollectionSettings(request.getSettings(), createdCollection))
            .collect(Collectors.toList());
    orgCollectionSettingsRepo.saveAll(settings);

    // validate permission
    collectionPermissionConfiguration.updatePermisions(request);

    // save the collection permission
    saveCollectionPermission(request, createdCollection);
  }

  @Override
  public Optional<OrganisationCollection> getCollectionByOrgId(Integer orgId) {
    if (orgId == null) {
      return Optional.empty();
    }
    return orgCollectionRepo.findCollectionByOrgId(orgId);
  }

  @Override
  public Optional<OrganisationCollection> getCollectionByOrgShortName(String shortName) {
    Assert.hasText(shortName, MISSING_ORG_SHORTNAME);
    return orgCollectionRepo.findByShortname(shortName);
  }

  @Override
  public void addCustomerToCollection(final Integer organisationId, final Integer collectionId) {
    Assert.notNull(organisationId, MISSING_ORG_ID);
    Assert.notNull(collectionId, "collection cannot be empty");

    List<CollectionRelation> relations = 
      collectionRelationRepo.findAllByWholesalerOrgId(organisationId);

    relations.forEach(r -> r.setCollectionId(collectionId));
    collectionRelationRepo.saveAll(relations);
  }

  @Override
  public Map<String, String> findSettingsByOrgShortname(String orgShortname) {
    Assert.hasText(orgShortname, MISSING_ORG_SHORTNAME);
    List<OrganisationCollectionsSettings> orgSettings = orgCollectionSettingsRepo
        .findSettingsByOrgShortname(orgShortname);
    
    vatTypeDisplaySettingService.findSettingsByOrgShortname(orgShortname)
        .ifPresent(orgSettings::add);
    
    return buildOrgCollectionSettingsMap(orgSettings);
  }

  @Override
  @LogExecutionTime
  public Map<String, String> findSettingsByCollectionShortname(String collectionShortname) {
    Assert.hasText(collectionShortname, "Collection short name cannot be empty");
    List<OrganisationCollectionsSettings> orgSettings =
      orgCollectionSettingsRepo.findByCollectionShortname(collectionShortname);
    
    vatTypeDisplaySettingService.findSettingsByCollectionShortname(collectionShortname)
      .ifPresent(orgSettings::add);
    
    return buildOrgCollectionSettingsMap(orgSettings);
  }

  @Override
  public Optional<String> findSettingValueByCollectionShortnameAndKey(String collectionShortname,
      String key) {
    if (StringUtils.isAnyBlank(collectionShortname, key)) {
      return Optional.empty();
    }
    return orgCollectionSettingsRepo.findSettingValueByCollectionShortnameAndSettingKey(
        collectionShortname, key);
  }

  @Override
  public Map<String, String> findSettingValuesByCollectionShortnameAndKeys(
      String collectionShortname, List<String> keys) {
    return orgCollectionSettingsRepo.findByCollectionShortname(collectionShortname).stream()
        .filter(item -> keys.contains(item.getSettingKey()))
        .collect(Collectors.toMap(OrganisationCollectionsSettings::getSettingKey,
            OrganisationCollectionsSettings::getSettingValue));
  }

  @Override
  public Optional<OrganisationCollectionsSettings> findSettingsByCollectionIdAndKey(
      Integer collectionId, String key) {
    return orgCollectionSettingsRepo.findByCollectionIdAndKey(collectionId, key);
  }

  @Override
  public Page<CollectionSearchResultDto> searchCollection(CollectionSearchCriteria searchCriteria) {
    final Pageable pageable = searchCriteria.buildPageable();

    final Specification<VCollectionSearch> spec;
    if (StringUtils.isBlank(searchCriteria.getCustomerNr())) {
      spec = VCollectionSearchSpecifications.searchCollections(searchCriteria,
          Collections.emptyList());
      return vCollectionRepo.findAll(spec, pageable)
        .map(CollecitonConverters.collectionsConverter());
    }

    final List<String> collectionsByOrgCode = orgCollectionRepo.findCollectionNameByOrgCode(
        searchCriteria.getCustomerNr());
    if (CollectionUtils.isEmpty(collectionsByOrgCode)) {
      return Page.empty();
    }
    spec = VCollectionSearchSpecifications.searchCollections(searchCriteria, collectionsByOrgCode);
    return vCollectionRepo.findAll(spec, pageable).map(CollecitonConverters.collectionsConverter());
  }

  @Override
  public Page<String> getAllCustomerNr(String collectionShortName, Pageable pageable) {
    if (StringUtils.isBlank(collectionShortName) || pageable == null) {
      return Page.empty();
    }
    return orgCollectionRepo.findCustomersByCollectionShortName(collectionShortName, pageable);
  }

  @Override
  @Transactional
  public void updateVatSetting(final Integer collectionId, final Double vat) {
    Assert.notNull(vat, "vat should not be null!");

    final String vatSettingKey = Affiliate.Settings.DEFAULT_VAT_RATE.toLowerName();
    final String vatValue = vat.toString();
    updateOrCreateSetting(collectionId, vatSettingKey, vatValue);
  }

  @Override
  @Transactional
  public void updateAbsCustomerSetting(Integer collectionId, final Boolean customerAbsEnabled) {
    Assert.notNull(customerAbsEnabled, "customerAbsEnabled should not be null!");

    final String customerAbsEnabledSettingKey =
        Affiliate.Settings.IS_CUSTOMER_ABS_ENABLED.toLowerName();
    final String customerAbsEnabledValue = customerAbsEnabled.toString();
    updateOrCreateSetting(collectionId, customerAbsEnabledSettingKey, customerAbsEnabledValue);
  }
  
  @Override
  @Transactional
  public void updateDisabledBrandPriorityAvailabilitySetting(Integer collectionId,
      final Boolean disabledBrandPriorityAvailability) {
    Assert.notNull(disabledBrandPriorityAvailability,
        "disabledBrandPriorityAvailability should not be null!");
    final String disabledBrandPriorityAvailabilityKey =
        Affiliate.Settings.DISABLED_BRAND_PRIORITY_AVAILABILITY.toLowerName();
    final String disabledBrandPriorityAvailabilityValue =
        disabledBrandPriorityAvailability.toString();
    updateOrCreateSetting(collectionId, disabledBrandPriorityAvailabilityKey,
        disabledBrandPriorityAvailabilityValue);
  }

  @Override
  public void updateExternalPartSetting(Integer collectionId, AffiliateExternalPartSettingDto extPartSetting) {
    final String extPartSettingKey = Settings.EXTERNAL_PART.toLowerName();
    if(!Objects.isNull(extPartSetting) && !extPartSetting.isUseExternalParts()) {
      extPartSetting.setShowInReferenceGroup(false);
    }
    String extPartValue = SagJSONUtil.convertObjectToJson(extPartSetting);
    updateOrCreateSetting(collectionId, extPartSettingKey, extPartValue);
  }

  @Override
  @Transactional
  public void updateAbsSalesSetting(Integer collectionId, final Boolean salesAbsEnabled) {
    Assert.notNull(salesAbsEnabled, "salesAbsEnabled should not be null!");

    final String salesAbsEnabledSettingKey =
        Affiliate.Settings.IS_SALES_ABS_ENABLED.toLowerName();
    final String salesAbsEnabledValue = salesAbsEnabled.toString();
    updateOrCreateSetting(collectionId, salesAbsEnabledSettingKey, salesAbsEnabledValue);
  }

  @Override
  @Transactional
  public void updateKsoSetting(Integer collectionId, final Boolean isKsoEnabled) {
    Assert.notNull(isKsoEnabled, "isKsoLEnabled should not be null!");

    final String ksoEnabledSettingKey =
        Affiliate.Settings.IS_KSO_ENABLED.toLowerName();
    final String isKsoEnabledValue = isKsoEnabled.toString();
    updateOrCreateSetting(collectionId, ksoEnabledSettingKey, isKsoEnabledValue);
  }

  @Override
  public void updateInvoiceRequestEmail(Integer collectionId, String invoiceRequestEmail) {
    final String invoiceRequestEmailKey = Settings.INVOICE_REQUEST_EMAIL.toLowerName();
    updateOrCreateSetting(collectionId, invoiceRequestEmailKey,
        StringUtils.defaultString(invoiceRequestEmail, StringUtils.EMPTY));
  }

  @Override
  public void updateInvoiceRequestAllowed(Integer collectionId,
      final Boolean isInvoiceRequestAllowed) {
    Assert.notNull(isInvoiceRequestAllowed, "isInvoiceRequestAllowed should not be null!");
    final String invoiceRequestAllowedSettingKey = Settings.INVOICE_REQUEST_ALLOWED.toLowerName();
    updateOrCreateSetting(collectionId, invoiceRequestAllowedSettingKey,
        isInvoiceRequestAllowed.toString());
  }

  @Override
  @Transactional
  public void updateVatTypeDisplay(Integer collectionId,
      final String vatTypeDisplay) {
    final String vatTypeDisplaySettingKey =
        Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName();
    updateOrCreateSetting(collectionId, vatTypeDisplaySettingKey,
        vatTypeDisplay);
  }

  @Override
  @Transactional
  public void updateCustomerBrandPriorityAvailFilter(Integer collectionId,
      final String customerBrandPriorityAvailFilter) {
    final String customerBrandPriorityAvailFilterSettingKey =
        Affiliate.Settings.CUSTOMER_BRAND_PRIORITY_AVAIL_FILTER.toLowerName();
    updateOrCreateSetting(collectionId, customerBrandPriorityAvailFilterSettingKey,
        customerBrandPriorityAvailFilter);
  }

  @Override
  @Transactional
  public void updateC4sBrandPriorityAvailFilter(Integer collectionId,
      String c4sBrandPriorityAvailFilter) {
    final String c4sBrandPriorityAvailFilterSettingKey =
        Affiliate.Settings.C4S_BRAND_PRIORITY_AVAIL_FILTER.toLowerName();
    updateOrCreateSetting(collectionId, c4sBrandPriorityAvailFilterSettingKey,
        c4sBrandPriorityAvailFilter);
  }

  @Override
  public void updateAvailabilitySetting(Integer collectionId,
      BoAffiliateSettingAvailabiltyDto availSetting) {
    if (Objects.isNull(availSetting)) {
      return;
    }
    updateOrCreateSetting(collectionId, Affiliate.Availability.AVAILABILITY_ICON.toLowerName(),
        BooleanUtils.toString(availSetting.getAvailIcon(), Boolean.toString(true),
            Boolean.toString(false), Boolean.toString(false)));
    updateOrCreateSetting(collectionId,
        Affiliate.Availability.DROP_SHIPMENT_AVAILABILITY.toLowerName(),
        BooleanUtils.toString(availSetting.getDropShipmentAvail(), Boolean.toString(true),
            Boolean.toString(false), Boolean.toString(false)));
    findSupportedLanguagesIso().forEach(langIso -> {
      updateOrCreateSetting(collectionId,
          Affiliate.Availability.DETAIL_AVAIL_TEXT
          .appendWithString(SagConstants.UNDERSCORE + langIso),
          availSetting.getDetailAvailTextByLanguage(langIso).getContent());
      updateOrCreateSetting(collectionId,
          Affiliate.Availability.LIST_AVAIL_TEXT
          .appendWithString(SagConstants.UNDERSCORE + langIso),
          availSetting.getListAvailTextByLanguage(langIso).getContent());
    });
  }
		
  @Override
  public void updateAvailabilityDisplaySetting(Integer collectionId,
      List<AffiliateAvailabilityDisplaySettingDto> availDisplaySettings) {
    if (CollectionUtils.isEmpty(availDisplaySettings)) {
      return;
    }

    updateOrCreateSetting(collectionId, Affiliate.Availability.AVAILABILITY_DISPLAY.toLowerName(),
        SagJSONUtil.convertObjectToJson(availDisplaySettings));
  }

  @Override
  @Transactional
  public void updateBrandFilterCustomerSetting(Integer collectionId, final Boolean customerBrandFilterEnabled) {
    Assert.notNull(customerBrandFilterEnabled, "customerBrandFilterEnabled should not be null!");

    final String customerBrandFitlerEnabledSettingKey =
        Affiliate.Settings.IS_CUSTOMER_BRAND_FILTER_ENABLED.toLowerName();
    final String customerBrandFitlerEnabledValue = customerBrandFilterEnabled.toString();
    updateOrCreateSetting(collectionId, customerBrandFitlerEnabledSettingKey, customerBrandFitlerEnabledValue);
  }

  @Override
  @Transactional
  public void updateBrandFilterSalesSetting(Integer collectionId,
    final Boolean salesBrandFilterEnabled) {
    Assert.notNull(salesBrandFilterEnabled, "salesBrandFitlerEnabled should not be null!");

    final String salesBrandFitlerEnabledSettingKey =
        Affiliate.Settings.IS_SALES_BRAND_FILTER_ENABLED.toLowerName();
    final String salesBrandFilterEnabledValue = salesBrandFilterEnabled.toString();
    updateOrCreateSetting(collectionId, salesBrandFitlerEnabledSettingKey,
      salesBrandFilterEnabledValue);
  }

  private List<String> findSupportedLanguagesIso() {
    return CollectionUtils.emptyIfNull(languageRepo.findAll()).stream().map(Language::getLangiso)
        .collect(Collectors.toList());
  }

  private void updateOrCreateSetting(final Integer collectionId, final String settingKey,
      final String settingValue) {
    OrganisationCollectionsSettings settings =
        findSettingsByCollectionIdAndKey(collectionId, settingKey)
            .orElseGet(() -> getNewSettings(collectionId, settingKey));

    saveSettings(settings, settingValue);
  }

  private OrganisationCollectionsSettings getNewSettings(final Integer collectionId,
      final String settingKey) {
    return OrganisationCollectionsSettings.builder().collectionId(collectionId)
        .settingKey(settingKey).build();
  }

  private void saveSettings(final OrganisationCollectionsSettings collectionSettings,
      final String settingValue) {
    final String existingValue = collectionSettings.getSettingValue();
    if (StringUtils.isBlank(existingValue) || !existingValue.equalsIgnoreCase(settingValue)) {
      collectionSettings.setSettingValue(settingValue);
      orgCollectionSettingsRepo.saveAll(Arrays.asList(collectionSettings));
    }
  }

}
