package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.collection.OrganisationCollectionsSettingsRepository;
import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.VatTypeDisplaySettingService;
import com.sagag.eshop.service.utils.AffiliateSettingConstants;
import com.sagag.eshop.service.validator.vat.display.VatTypeDisplayValidator;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.profiles.EnablePriceDiscountPromotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@EnablePriceDiscountPromotion(true)
public class DefaultVatTypeDisplaySettingServiceImpl implements VatTypeDisplaySettingService {

  @Autowired
  private OrganisationCollectionsSettingsRepository orgCollectionSettingsRepo;

  @Autowired
  private OrganisationCollectionService collectionService;

  @Autowired
  private VatTypeDisplayValidator vatTypeDisplayValidator;

  @Override
  public Optional<OrganisationCollectionsSettings> findSettingsByOrgShortname(String orgShortname) {
    Assert.hasText(orgShortname, "Organisaion short name cannot be empty");
    String value = orgCollectionSettingsRepo
        .findSettingValueByOrgShortnameAndSettingKey(orgShortname,
            Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName())
        .orElse(AffiliateSettingConstants.DEFAULT_VAT_TYPE_DISPLAY);
    return Optional.of(OrganisationCollectionsSettings.builder()
        .settingKey(Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName()).settingValue(value).build());
  }

  @Override
  @LogExecutionTime
  public Optional<OrganisationCollectionsSettings> findSettingsByCollectionShortname(
      String collectionShortname) {
    Assert.hasText(collectionShortname, "Collection short name cannot be empty");
    String value = orgCollectionSettingsRepo
        .findSettingValueByCollectionShortnameAndSettingKey(collectionShortname,
            Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName())
        .orElse(AffiliateSettingConstants.DEFAULT_VAT_TYPE_DISPLAY);
    return Optional.of(OrganisationCollectionsSettings.builder()
        .settingKey(Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName()).settingValue(value).build());
  }

  @Override
  @Transactional
  public void updateVatTypeDisplay(Integer collectionId, final String vatTypeDisplay) {
    if (!vatTypeDisplayValidator.validate(vatTypeDisplay)) {
      throw new IllegalArgumentException(
          String.format("Invalid VatTypeDisplay %s", vatTypeDisplay));
    }
    collectionService.updateVatTypeDisplay(collectionId, vatTypeDisplay);
  }
}
