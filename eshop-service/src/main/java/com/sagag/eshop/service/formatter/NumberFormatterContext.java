package com.sagag.eshop.service.formatter;

import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.services.common.number.NumberFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;

@Component
public class NumberFormatterContext {

  private static final String SETTING_LOCALE =
      SettingsKeys.Affiliate.Settings.SETTING_LOCALE.toLowerName();

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private NumberFormatter numberFormatter;

  public NumberFormat getFormatterByAffiliateShortName(final String collectionShortname) {
    return numberFormatter.getNumberFormatter(findLocale(collectionShortname));
  }

  private String findLocale(final String collectionShortname) {
    return orgCollectionService
    .findSettingValueByCollectionShortnameAndKey(collectionShortname, SETTING_LOCALE)
    .orElseThrow(() -> new IllegalArgumentException(String
        .format("There is no such setting locale for collectionId: %s", collectionShortname)));
  }
}
