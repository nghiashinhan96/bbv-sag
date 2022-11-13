package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.AffiliateService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.dto.AffiliateSettings;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.OrganisationTypeEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.SettingLanguageDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateInfoDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateSettingDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateShortInfoDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingAvailabiltyDto;
import com.sagag.services.domain.eshop.dto.OrganisationDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AffiliateServiceImpl implements AffiliateService {

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  private OrganisationRepository organisationRepo;

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Autowired
  private LanguageRepository languageRepo;

  @Override
  public List<OrganisationDto> getAllAffiliates() {
    List<Organisation> organisations =
        organisationService.findOrganisationByTypeName(OrganisationTypeEnum.AFFILIATE.name());
    return organisations.stream().map(org -> SagBeanUtils.map(org, OrganisationDto.class))
        .collect(Collectors.toList());
  }

  @Override
  public List<BackOfficeAffiliateInfoDto> getInfos(String shortName) {
    List<Organisation> organisations = new ArrayList<>();

    if (!StringUtils.isEmpty(shortName)) {
      organisations.add(organisationRepo.findOneByShortname(shortName)
          .orElseThrow(() -> new IllegalArgumentException("Does not exist this affiliate")));
    } else {
      organisations.addAll(
          organisationService.findOrganisationByTypeName(OrganisationTypeEnum.AFFILIATE.name()));
    }

    return organisations.stream().map(this::createAffInfoFromOrg).collect(Collectors.toList());
  }

  private BackOfficeAffiliateInfoDto createAffInfoFromOrg(Organisation org) {
    BackOfficeAffiliateInfoDto info = new BackOfficeAffiliateInfoDto();
    info.setDescription(org.getDescription());
    info.setName(org.getName());
    info.setOrgCode(org.getOrgCode());
    info.setShortName(org.getShortname());
    return info;
  }

  // @formatter:off
  @Override
  public List<BackOfficeAffiliateShortInfoDto> getShortInfos() {
    return organisationService.findOrganisationByTypeName(OrganisationTypeEnum.AFFILIATE.name())
        .stream()
        .map(org -> BackOfficeAffiliateShortInfoDto.builder()
            .name(org.getName())
            .shortName(org.getShortname()).build())
        .collect(Collectors.toList());
  }

  @Override
  public BackOfficeAffiliateSettingDto getSettings(String affShortName) {
    Organisation org = organisationRepo.findOneByShortname(affShortName)
        .orElseThrow(() -> new IllegalArgumentException("Does not exist this affiliate"));

    AffiliateSettings settings = new AffiliateSettings(
        orgCollectionService.findSettingsByOrgShortname(affShortName));
    Map<String, String> affSettings = settings.getAffSettings();
    List<String> supportedLangIso =
       CollectionUtils.emptyIfNull(languageRepo.findAll())
        .stream().map(Language::getLangiso).collect(Collectors.toList());
    BoAffiliateSettingAvailabiltyDto availSetting = BoAffiliateSettingAvailabiltyDto.builder()
        .availIcon(BooleanUtils.toBoolean(settings.isAvailIcon()))
        .detailAvailText(getDetailAvailText(affSettings, supportedLangIso))
        .listAvailText(getListAvailText(affSettings, supportedLangIso))
        .dropShipmentAvail(BooleanUtils.toBoolean(settings.isDropShipmentAvailability()))
        .build();

    setAvailIconSettingAsDefault(availSetting);

    BackOfficeAffiliateSettingDto setting = new BackOfficeAffiliateSettingDto();
    setting.setDescription(org.getDescription());
    setting.setOrgCode(org.getOrgCode());
    setting.setName(org.getName());
    setting.setShortName(org.getShortname());
    setting.setVat(settings.getVatRate());
    setting.setCustomerAbsEnabled(settings.isCustomerAbsEnabled());
    setting.setSalesAbsEnabled(settings.isSalesAbsEnabled());
    setting.setKsoEnabled(settings.isKsoEnabled());
    setting.setCustomerBrandPriorityAvailFilter(settings.getCustomerBrandPriorityAvailFilter());
    setting.setC4sBrandPriorityAvailFilter(settings.getC4sBrandPriorityAvailFilter());
    setting.setAvailSetting(availSetting);
    setting.setCustomerBrandFilterEnabled(settings.isCustomerBrandFilterEnabled());
    setting.setSalesBrandFilterEnabled(settings.isSalesBrandFilterEnabled());
    setting.setVatTypeDisplay(settings.getAffVatTypeDisplay());
    setting.setAvailDisplaySettings(settings.getAvailDisplaySettingsConverted());
    setting.setInvoiceRequestAllowed(settings.isInvoiceRequestAllowed());
    setting.setInvoiceRequestEmail(settings.getInvoiceRequestEmail());
    setting.setDisabledBrandPriorityAvailability(settings.isDisabledBrandPriorityAvailability());
    setting.setOptimizelyId(settings.getOptimizelyId());
    setting.setExternalPartSettings(settings.getExternalPartSettingsConverted());
    return setting;
  }

  private void setAvailIconSettingAsDefault(BoAffiliateSettingAvailabiltyDto availSetting) {
    if(!availSetting.getAvailIcon() && !availSetting.getDropShipmentAvail()
        && CollectionUtils.isEmpty(availSetting.getListAvailText())) {
      availSetting.setAvailIcon(true);
    }
  }

  private List<SettingLanguageDto> getDetailAvailText(Map<String, String> affSettings,
      List<String> supportedLanguages) {
    List<SettingLanguageDto> availSettings = new ArrayList<>();
    supportedLanguages.forEach(langIso -> {
      String detailText = affSettings.get(SettingsKeys.Affiliate.Availability.DETAIL_AVAIL_TEXT
          .appendWithString(SagConstants.UNDERSCORE + langIso));
      if (StringUtils.isNotEmpty(detailText)) {
        availSettings.add(
            SettingLanguageDto.builder().langIso(langIso).content(detailText).build());
      }
    });
    return availSettings;
  }

  private List<SettingLanguageDto> getListAvailText(Map<String, String> affSettings,
      List<String> supportedLanguages) {
    List<SettingLanguageDto> availSettings = new ArrayList<>();
    supportedLanguages.forEach(langIso -> {
      String detailText = affSettings
          .get(SettingsKeys.Affiliate.Availability.LIST_AVAIL_TEXT
              .appendWithString(SagConstants.UNDERSCORE + langIso));
      if (StringUtils.isNotEmpty(detailText)) {
        availSettings.add(
            SettingLanguageDto.builder().langIso(langIso).content(detailText).build());
      }
    });
    return availSettings;
  }

  @Override
  public List<BackOfficeAffiliateShortInfoDto> getShortInfosByCountry(
      final String countryShortCode) {
    Assert.notNull(countryShortCode, "The country short code must not be null");
    final List<Organisation> organisations =
        organisationService.findOrganisationByTypeName(OrganisationTypeEnum.AFFILIATE.name());
    List<String> compNames =
      supportedAffiliateRepo.findCompanyNameByCountryShortCode(countryShortCode).stream()
        .map(StringUtils::trim).collect(Collectors.toList());

    List<BackOfficeAffiliateShortInfoDto> affiliates = organisations.stream()
        .filter(org -> compNames.contains(StringUtils.trim(org.getName())))
        .map(org -> SupportedAffiliate.fromCompanyName(org.getName()))
        .map(AffiliateServiceImpl::buildAffDto).collect(Collectors.toList());

    return Optional.of(affiliates).filter(CollectionUtils::isNotEmpty)
        .orElseThrow(() -> new NoSuchElementException("Not found any affiliates"));
  }

  private static BackOfficeAffiliateShortInfoDto buildAffDto(final SupportedAffiliate aff) {
    return BackOfficeAffiliateShortInfoDto.builder()
        .name(aff.getCompanyName())
        .shortName(aff.getAffiliate())
        .build();
  }
}
