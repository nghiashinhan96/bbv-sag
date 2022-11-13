package com.sagag.services.common.domain.country;

import lombok.Data;

@Data
public class CustomCountryValueConfigProperties {

  private String erpType;

  private String userLoaderMode;

  private boolean enableShoppingCartMapStore;
  
  private boolean enablePriceDiscountPromotion;

  private String esExternalArticleService;

  private String categoryConverter;

  private String oauth2ExternalAuthenticator;

  private String currency;

  private String defaultLanguage;

  private CustomCountryRegisterCustomerAndUserConfigProperties registerCustomerUserSetting;

  private boolean sortingOilVehicleInDesc;

}
