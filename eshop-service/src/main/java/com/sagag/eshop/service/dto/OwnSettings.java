package com.sagag.eshop.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.enums.LinkPartnerEnum;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;

import com.sagag.services.domain.eshop.dto.AffiliateExternalPartSettingDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * User settings class at employee of garage level.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OwnSettings extends CustSettings {

  private static final long serialVersionUID = -4617184409231201843L;

  @JsonIgnore
  private UserSettings userSettings;

  private boolean dvseUserActive;

  private Map<String, String> externalUrls;

  @JsonIgnore
  private PriceDisplayTypeEnum priceTypeDisplayEnum;

  private boolean showDiscount;

  private boolean showGross;

  private boolean priceDisplayChanged;

  private String brandPriorityAvailFilter;

  private String vatTypeDisplay;

  private Integer mouseOverFlyoutDelay;

  private boolean enhancedUsedPartsReturnProcEnabled;
  
  private boolean couponModuleEnabled;
  
  private List<AffiliateAvailabilityDisplaySettingDto> availDisplaySettings;

  private AffiliateExternalPartSettingDto externalPartSettings;

  @Override
  public boolean isNetPriceView() {
    return userSettings.isNetPriceView();
  }

  public boolean isCurrentStateNetPriceView() {
    return userSettings.isCurrentStateNetPriceView();
  }

  @Override
  public boolean isEmailOrderConfirmation() {
    return userSettings.isEmailNotificationOrder();
  }

  @Override
  public boolean isViewBilling() {
    return userSettings.isViewBilling();
  }

  @JsonIgnore
  public String getDeliveryAddressId() {
    if (!hasUserSettings()) {
      return StringUtils.EMPTY;
    }
    return this.userSettings.getDeliveryAddressId();
  }

  @JsonIgnore
  public String getBillingAddressId() {
    if (!hasUserSettings()) {
      return StringUtils.EMPTY;
    }
    return this.userSettings.getBillingAddressId();
  }

  @JsonIgnore
  private boolean hasUserSettings() {
    return !Objects.isNull(this.userSettings);
  }

  @Override
  public boolean isNetPriceConfirm() {
    return userSettings.isNetPriceConfirm();
  }

  @Override
  public boolean isShowHappyPoints() {
    return userSettings.isShowHappyPoints();
  }

  @Override
  public boolean isAcceptHappyPointTerm() {
    return userSettings.isAcceptHappyPointTerm();
  }

  public void addExternalUrls(LinkPartnerEnum linkPartner, String extUrl) {
    if (this.externalUrls == null) {
      this.externalUrls = new HashMap<>();
    }
    if (StringUtils.isBlank(extUrl) || linkPartner == null) {
      return;
    }
    this.externalUrls.put(linkPartner.name().toLowerCase(), extUrl);
  }

  @JsonProperty("dvseCatalogUri")
  public String getDvseCatalogUri() {
    return this.getExternalUrls().get(LinkPartnerEnum.DVSE.name().toLowerCase());
  }

  @JsonProperty("thuleDealerUrl")
  public String getThuleDealerUrl() {
    return this.getExternalUrls().get(LinkPartnerEnum.THULE.name().toLowerCase());
  }

  public void removeThuleDealerUrl() {
    this.getExternalUrls().remove(LinkPartnerEnum.THULE.name().toLowerCase());
  }

  public Map<String, String> getExternalUrls() {
    return MapUtils.emptyIfNull(this.externalUrls);
  }

  public boolean hasWssDeliveryProfile() {
    return getWssDeliveryProfile() != null;
  }

}
