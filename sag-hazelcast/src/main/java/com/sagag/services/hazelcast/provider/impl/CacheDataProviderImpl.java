package com.sagag.services.hazelcast.provider.impl;

import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.hazelcast.api.impl.AbstractVatRateCacheService;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;
import com.sagag.services.hazelcast.provider.CacheDataProvider;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheDataProviderImpl implements CacheDataProvider {

  private static final String LOG_LANGUAGE = "Setting index language: {}";

  @Autowired
  @Qualifier("supplierCacheServiceImpl")
  private CacheDataProcessor supplierCacheDataProcessor;
  @Autowired
  @Qualifier("makeCacheServiceImpl")
  private CacheDataProcessor makeCacheDataProcessor;
  @Autowired
  @Qualifier("modelCacheServiceImpl")
  private CacheDataProcessor modelCacheDataProcessor;
  @Autowired
  @Qualifier("criteriaCacheServiceImpl")
  private CacheDataProcessor criteriaCacheDataProcessor;
  @Autowired
  @Qualifier("genArtCacheServiceImpl")
  private CacheDataProcessor genArtCacheDataProcessor;
  @Autowired
  @Qualifier("categoryCacheServiceImpl")
  private CacheDataProcessor catCacheDataProcessor;
  @Autowired
  @Qualifier("formatGaCacheServiceImpl")
  private CacheDataProcessor formatGaCacheDataProcessor;
  @Autowired
  @Qualifier("externalVendorCacheServiceImpl")
  private CacheDataProcessor externalVendorCacheDataProcessor;
  @Autowired
  @Qualifier("deliveryProfileCacheServiceImpl")
  private CacheDataProcessor deliveryProfileCacheDataProcessor;
  @Autowired
  @Qualifier("eshopGlobalSettingCacheServiceImpl")
  private CacheDataProcessor eshopGlobalSettingCacheService;
  @Autowired
  @Qualifier("brandPriorityCacheServiceImpl")
  private CacheDataProcessor brandPriorityCacheDataProcessor;
  @Autowired
  @Qualifier("oatesAdditionalRecommendationsCacheServiceImpl")
  private CacheDataProcessor oatesAdditionalRecommendationsCacheDataProcessor;
  @Autowired
  @Qualifier("tourTimeCacheServiceImpl")
  private CacheDataProcessor tourTimeCacheDataProcessor;

  @Autowired
  private AbstractVatRateCacheService vatRateCacheService;

  @Autowired
  private CountryConfiguration countryConfig;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Override
  public void refreshCacheData() {
    refreshCacheDefaultLanguage();
    refreshCacheMultilingual();
    LocaleContextHolder.setLocale(localeContextHelper.defaultAppLocale());
    log.info(LOG_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
  }

  private void refreshCacheDefaultLanguage() {
    LocaleContextHolder.setLocale(localeContextHelper.defaultAppLocale());
    log.info(LOG_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
    externalVendorCacheDataProcessor.refreshCacheAll();
    deliveryProfileCacheDataProcessor.refreshCacheAll();
    supplierCacheDataProcessor.refreshCacheAll();
    makeCacheDataProcessor.refreshCacheAll();
    eshopGlobalSettingCacheService.refreshCacheAll();
    brandPriorityCacheDataProcessor.refreshCacheAll();
    oatesAdditionalRecommendationsCacheDataProcessor.refreshCacheAll();
    vatRateCacheService.refreshCacheAll();
    tourTimeCacheDataProcessor.refreshCacheAll();
  }

  private void refreshCacheMultilingual() {
    final String[] languages = countryConfig.languages();
    for (String lang : languages) {
      LocaleContextHolder.setLocale(localeContextHelper.toLocale(lang));
      log.info(LOG_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
      modelCacheDataProcessor.refreshCacheAll();
      genArtCacheDataProcessor.refreshCacheAll();
      criteriaCacheDataProcessor.refreshCacheAll();
      catCacheDataProcessor.refreshCacheAll();
      formatGaCacheDataProcessor.refreshCacheAll();
    }
  }

}
