package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.eshop.service.api.VatTypeDisplaySettingService;
import com.sagag.services.common.profiles.EnablePriceDiscountPromotion;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@EnablePriceDiscountPromotion(false)
public class EmptyVatTypeDisplaySettingServiceImpl implements VatTypeDisplaySettingService {

  private static final String WARN_MSG = "No support for this System";

  @Override
  public void updateVatTypeDisplay(Integer collectionId, String vatTypeDisplay) {
    log.debug(WARN_MSG);
  }

  @Override
  public Optional<OrganisationCollectionsSettings> findSettingsByOrgShortname(String shortname) {
    log.debug(WARN_MSG);
    return Optional.empty();
  }

  @Override
  public Optional<OrganisationCollectionsSettings> findSettingsByCollectionShortname(
      String collectionShortname) {
    log.debug(WARN_MSG);
    return Optional.empty();
  }
}
