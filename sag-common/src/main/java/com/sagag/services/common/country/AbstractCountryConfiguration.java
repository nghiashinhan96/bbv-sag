package com.sagag.services.common.country;

import com.sagag.services.common.domain.AttachedArticle;
import com.sagag.services.common.domain.country.CustomCountryValueConfigProperties;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.enums.country.ErpType;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "country")
public abstract class AbstractCountryConfiguration implements CountryConfiguration {

  private String code;

  private String[] supportedIso3Countries;

  private HashType hashType;

  private String[] languages;

  private String[] excludeCountries;

  private AttachedArticle attachedArticle;

  private CustomCountryValueConfigProperties config;

  private String additionalRecommendationUrl;

  @Override
  public HashType getHashType() {
    return hashType;
  }

  @Override
  public String[] languages() {
    return ArrayUtils.nullToEmpty(languages);
  }

  @Override
  public String[] getSupportedIso3Countries() {
    return ArrayUtils.nullToEmpty(supportedIso3Countries);
  }

  @Override
  public String[] getExcludeCountries() {
    return ArrayUtils.nullToEmpty(excludeCountries);
  }

  @Override
  public AttachedArticle getSupportedAttachedArticle() {
    return attachedArticle;
  }

  @Override
  public String getCountryCode() {
    return StringUtils.EMPTY;
  }
  @Override
  public int getCustomerSessionTimeoutSeconds() {
    return this.config.getRegisterCustomerUserSetting().getCustomerSetting()
        .getSessionTimeoutSeconds();
  }

  @Override
  public boolean getCustomerNetPriceViewSetting() {
    return this.config.getRegisterCustomerUserSetting().getCustomerSetting().isNetPriceView();
  }

  @Override
  public boolean getCustomerNetPriceConfirmSetting() {
    return this.config.getRegisterCustomerUserSetting().getCustomerSetting().isNetPriceConfirm();
  }

  @Override
  public String getDefaultUserLanguageByCountry() {
    return this.config.getRegisterCustomerUserSetting().getUserSetting().getLanguage();
  }

  @Override
  public String getCode() {
    return this.code;
  }

  @Override
  public ErpType getSupportedErpType() {
    if (StringUtils.isBlank(this.config.getErpType())) {
      return ErpType.NONE;
    }
    return ErpType.valueOf(this.config.getErpType());
  }

  @Override
  public boolean isSortingOilVehicleInDesc() {
    return this.config.isSortingOilVehicleInDesc();
  }

  @Override
  public String defaultLanguage() {
    return this.config.getDefaultLanguage();
  }

}
