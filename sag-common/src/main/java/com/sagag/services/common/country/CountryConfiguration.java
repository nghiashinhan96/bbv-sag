package com.sagag.services.common.country;

import com.sagag.services.common.domain.AttachedArticle;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.enums.country.ErpType;

public interface CountryConfiguration {

  String getCode();

  String[] getSupportedIso3Countries();

  HashType getHashType();

  String[] languages();

  String[] getExcludeCountries();

  AttachedArticle getSupportedAttachedArticle();
  String getCountryCode();

  int getCustomerSessionTimeoutSeconds();

  boolean getCustomerNetPriceViewSetting();

  boolean getCustomerNetPriceConfirmSetting();

  String getDefaultUserLanguageByCountry();

  ErpType getSupportedErpType();

  boolean isSortingOilVehicleInDesc();

  String defaultLanguage();
}
