package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.AffiliateSettingsService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.VatTypeDisplaySettingService;
import com.sagag.eshop.service.validator.avail.filter.AffiliateAvailabilityDisplaySettingValidator;
import com.sagag.eshop.service.validator.avail.filter.AffiliateSettingAvailabilityValidator;
import com.sagag.eshop.service.validator.avail.filter.BrandPriorityAvailabilityFilterValidator;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.AvailabilitySettingMasterDataDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingAvailabiltyDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingRequest;
import com.sagag.services.domain.eshop.dto.LanguageDto;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AffiliateSettingsServiceImpl implements AffiliateSettingsService {

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private OrganisationRepository organisationRepo;

  @Autowired
  private VatTypeDisplaySettingService vatTypeDisplaySettingService;

  @Autowired
  private BrandPriorityAvailabilityFilterValidator brandPriorityAvailabilityFilterValidator;

  @Autowired
  private AffiliateAvailabilityDisplaySettingValidator availabilityDisplaySettingValidator;

  @Autowired
  private AffiliateSettingAvailabilityValidator affiliateAvailabilitySettingValidator;

  @Autowired
  private LanguageRepository languageRepo;

  @Override
  @Transactional
  public void updateSettings(final BoAffiliateSettingRequest requestBody) {
    final String affiliateShortName = requestBody.getShortName();
    Assert.hasText(affiliateShortName, "affiliate name should not be empty!");
    if (!brandPriorityAvailabilityFilterValidator
        .validate(requestBody.getC4sBrandPriorityAvailFilter())) {
      throw new IllegalArgumentException(String.format("Invalid C4sBrandPriorityAvailFilter %s",
          requestBody.getC4sBrandPriorityAvailFilter()));
    }

    if (!brandPriorityAvailabilityFilterValidator
        .validate(requestBody.getCustomerBrandPriorityAvailFilter())) {
      throw new IllegalArgumentException(
          String.format("Invalid customerBrandPriorityAvailFilter %s",
              requestBody.getCustomerBrandPriorityAvailFilter()));
    }

    List<AffiliateAvailabilityDisplaySettingDto> availSettings = requestBody.getAvailDisplaySettings();
    if (CollectionUtils.isNotEmpty(availSettings)) {
      List<String> supportedLanguagesIso =
          findSupportedLanguages().stream().map(LanguageDto::getLangiso).collect(Collectors.toList());
      availSettings.stream().forEach(availSetting -> {
        availSetting.setSupportedLanguagesIso(supportedLanguagesIso);
        if(!availabilityDisplaySettingValidator.validate(availSetting)) {
          throw new IllegalArgumentException("Display option is not suitable with avail state");
        }
      });
    }

    BoAffiliateSettingAvailabiltyDto availSetting =
        CollectionUtils.emptyIfNull(requestBody.getAvailDisplaySettings()).stream()
            .filter(displaySettingDto -> displaySettingDto.getAvailState().isNotAvailable())
            .findAny().map(toAvailSetting()).orElse(null);

    if (!affiliateAvailabilitySettingValidator.validate(availSetting)) {
      throw new IllegalArgumentException("Only one availability setting can be set");
    }


    Organisation affiliate = organisationRepo.findOneByShortname(affiliateShortName)
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("Does not exist this affiliate %s", affiliateShortName)));

    List<OrganisationCollection> collections =
        orgCollectionService.findByAffiliateId(affiliate.getId());

    collections.forEach(c -> {
      orgCollectionService.updateVatSetting(c.getId(), requestBody.getVat());
      orgCollectionService.updateAbsCustomerSetting(c.getId(), requestBody.getCustomerAbsEnabled());
      orgCollectionService.updateAbsSalesSetting(c.getId(), requestBody.getSalesAbsEnabled());
      orgCollectionService.updateKsoSetting(c.getId(), requestBody.getKsoEnabled());
      orgCollectionService.updateC4sBrandPriorityAvailFilter(c.getId(),
          requestBody.getC4sBrandPriorityAvailFilter());
      orgCollectionService.updateCustomerBrandPriorityAvailFilter(c.getId(),
          requestBody.getCustomerBrandPriorityAvailFilter());
      orgCollectionService.updateAvailabilitySetting(c.getId(), availSetting);
      orgCollectionService.updateAvailabilityDisplaySetting(c.getId(), availSettings);
      orgCollectionService.updateInvoiceRequestAllowed(c.getId(), requestBody.getInvoiceRequestAllowed());
      orgCollectionService.updateInvoiceRequestEmail(c.getId(), requestBody.getInvoiceRequestEmail());

      orgCollectionService.updateBrandFilterCustomerSetting(c.getId(),
          requestBody.getCustomerBrandFilterEnabled());
      orgCollectionService.updateBrandFilterSalesSetting(c.getId(),
          requestBody.getSalesBrandFilterEnabled());
      orgCollectionService.updateDisabledBrandPriorityAvailabilitySetting(c.getId(),
          requestBody.getDisabledBrandPriorityAvailability());
      orgCollectionService.updateExternalPartSetting(c.getId(), requestBody.getExternalPartSettings());

      vatTypeDisplaySettingService.updateVatTypeDisplay(c.getId(), requestBody.getVatTypeDisplay());
    });
  }

  private Function<AffiliateAvailabilityDisplaySettingDto, BoAffiliateSettingAvailabiltyDto> toAvailSetting() {
    return availDisplay -> {
      BoAffiliateSettingAvailabiltyDto availabiltyDto = new BoAffiliateSettingAvailabiltyDto();
      availabiltyDto.setDetailAvailText(availDisplay.getDetailAvailText());
      availabiltyDto.setListAvailText(availDisplay.getListAvailText());
      availabiltyDto.setSupportedLanguagesIso(availDisplay.getSupportedLanguagesIso());
      availabiltyDto.setAvailIcon(false);
      availabiltyDto.setDropShipmentAvail(false);

      switch (availDisplay.getDisplayOption()) {
        case DOT:
          availabiltyDto.setAvailIcon(true);
          break;
        case DROP_SHIPMENT:
          availabiltyDto.setDropShipmentAvail(true);
          break;
        default:
          break;
      }
      return availabiltyDto;
    };
  }

  @Override
  public AvailabilitySettingMasterDataDto getAvailabilitySettingMasterData() {
    return AvailabilitySettingMasterDataDto.builder().supportedLanguages(findSupportedLanguages())
        .build();
  }

  private List<LanguageDto> findSupportedLanguages() {
    return CollectionUtils.emptyIfNull(languageRepo.findAll()).stream()
        .map(item -> SagBeanUtils.map(item, LanguageDto.class)).collect(Collectors.toList());
  }

}
