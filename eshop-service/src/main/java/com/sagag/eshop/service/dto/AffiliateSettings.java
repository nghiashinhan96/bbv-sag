package com.sagag.eshop.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.utils.AffiliateSettingConstants;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.AffiliateExternalPartSettingDto;
import com.sagag.services.domain.eshop.dto.SettingLanguageDto;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Affiliate settings class at organisation level.
 */
@Data
@AllArgsConstructor
public class AffiliateSettings implements ISettings {

  private static final long serialVersionUID = -7480149136487790681L;

  @JsonIgnore
  private Map<String, String> affSettings;

  /**
   * Default constructor.
   */
  public AffiliateSettings() {
    affSettings = new HashMap<>();
  }

  @Override
  public boolean isNetPriceView() {
    return false;
  }

  @Override
  public boolean isEmailOrderConfirmation() {
    return false;
  }

  @Override
  public String getAffiliateEmail() {
    return affSettings.get(SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName());
  }

  public String getEhDefaultEmail() {
    return affSettings.get(SettingsKeys.Affiliate.Settings.EH_DEFAULT_EMAIL.toLowerName());
  }

  @Override
  public String getAffiliateDefaultUrl() {
    return affSettings.get(SettingsKeys.Affiliate.Settings.DEFAULT_URL.toLowerName());
  }

  @Override
  public String getAffiliateEhPortalUrl() {
    return affSettings.get(SettingsKeys.Affiliate.Settings.EH_PORTAL_URL.toLowerName());
  }

  @Override
  public String getGaTrackingCode() {
    return affSettings
        .get(SettingsKeys.Affiliate.Settings.GOOGLE_ANALYTICS_TRACKING_CODE.toLowerName());
  }

  @Override
  public String getGoogleTagManagerSetting() {
    return affSettings
        .get(SettingsKeys.Affiliate.Settings.GOOGLE_TAG_MANAGER_SETTING.toLowerName());
  }

  @Override
  public boolean isShowTyresDiscount() {
    return toBooleanValue(
        affSettings.get(SettingsKeys.Affiliate.Settings.SHOW_TYRES_DISCOUNT.toLowerName()));
  }

  @Override
  public boolean isShowTyresGrossPriceHeader() {
    return toBooleanValue(affSettings
        .get(SettingsKeys.Affiliate.Settings.SHOW_TYRES_GROSS_PRICE_HEADER.toLowerName()));
  }

  @Override
  public boolean isCustomerAbsEnabled() {
    return toBooleanValue(
        affSettings.get(SettingsKeys.Affiliate.Settings.IS_CUSTOMER_ABS_ENABLED.toLowerName()));
  }

  @Override
  public boolean isSalesAbsEnabled() {
    return toBooleanValue(
        affSettings.get(SettingsKeys.Affiliate.Settings.IS_SALES_ABS_ENABLED.toLowerName()));
  }

  @Override
  public boolean isCustomerBrandFilterEnabled() {
    return toBooleanValue(
        affSettings.get(SettingsKeys.Affiliate.Settings.IS_CUSTOMER_BRAND_FILTER_ENABLED.toLowerName()));
  }

  @Override
  public boolean isSalesBrandFilterEnabled() {
    return toBooleanValue(
        affSettings.get(SettingsKeys.Affiliate.Settings.IS_SALES_BRAND_FILTER_ENABLED.toLowerName()));
  }

  @Override
  public boolean isAdditionalRecommendationEnabled() {
    return toBooleanValue(
        affSettings.get(SettingsKeys.Affiliate.Settings.IS_ADDITIONAL_RECOMMENDATION_ENABLED.toLowerName()));
  }

  @Override
  public int getSessionTimeoutSeconds() {
    return 0;
  }

  @JsonIgnore
  private static boolean toBooleanValue(String value) {
    if (StringUtils.isEmpty(value)) {
      return false;
    }
    return Boolean.valueOf(value);
  }

  /**
   * Returns the affiliate settings for vat rate.
   *
   * @return the vat rate
   */
  @Override
  public double getVatRate() {
    return NumberUtils
        .toDouble(affSettings.get(SettingsKeys.Affiliate.Settings.DEFAULT_VAT_RATE.toLowerName()));
  }

  @Override
  public boolean isViewBilling() {
    return false;
  }

  @Override
  public boolean isNetPriceConfirm() {
    return false;
  }

  @Override
  public boolean isShowHappyPoints() {
    return false;
  }

  @Override
  public boolean isAcceptHappyPointTerm() {
    return false;
  }

  @Override
  public boolean isInvoiceRequestAllowed() {
    return BooleanUtils.toBoolean(
        affSettings.get(SettingsKeys.Affiliate.Settings.INVOICE_REQUEST_ALLOWED.toLowerName()));
  }

  @Override
  public String getInvoiceRequestEmail() {
    return StringUtils.defaultString(
        affSettings.get(SettingsKeys.Affiliate.Settings.INVOICE_REQUEST_EMAIL.toLowerName()),
        StringUtils.EMPTY);
  }

  @Override
  public String getOciEwbSchemaType() {
    return affSettings.get(SettingsKeys.Affiliate.Settings.OCI_EWB_SCHEMA_TYPE.toLowerName());
  }

  @Override
  public String getOciVendorId() {
    return affSettings.get(SettingsKeys.Affiliate.Settings.OCI_VENDORID.toLowerName());
  }

  @Override
  public String getBranchName() {
    return affSettings.get(SettingsKeys.Affiliate.Settings.FILIALE.toLowerName());
  }

  @Override
  @JsonIgnore
  public String getAffVatTypeDisplay() {
    return StringUtils.defaultString(
        affSettings.get(SettingsKeys.Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName()),
        StringUtils.EMPTY);
  }

  @Override
  @JsonIgnore
  public List<AffiliateAvailabilityDisplaySettingDto> getAvailDisplaySettingsConverted() {
    String jsonAvailDisplay = StringUtils.defaultString(
        affSettings.get(SettingsKeys.Affiliate.Availability.AVAILABILITY_DISPLAY.toLowerName()),
        StringUtils.EMPTY);
    return SagJSONUtil.convertArrayJsonToList(jsonAvailDisplay,
        AffiliateAvailabilityDisplaySettingDto.class);
  }

  @Override
  @JsonIgnore
  public AffiliateExternalPartSettingDto getExternalPartSettingsConverted() {
    String jsonExtPart = StringUtils.defaultString(
        affSettings.get(SettingsKeys.Affiliate.Settings.EXTERNAL_PART.toLowerName()),
        StringUtils.EMPTY);
    return StringUtils.isEmpty(jsonExtPart) ? AffiliateExternalPartSettingDto.builder()
            .useExternalParts(false)
            .showInReferenceGroup(false)
            .headerNames(Collections.emptyList())
            .orderTexts(Collections.emptyList())
            .build()
            : SagJSONUtil.convertJsonToObject(jsonExtPart, AffiliateExternalPartSettingDto.class);
  }

  @Override
  @JsonIgnore
  public String getCustomerBrandPriorityAvailFilter() {
    return StringUtils.defaultString(
        affSettings.get(
            SettingsKeys.Affiliate.Settings.CUSTOMER_BRAND_PRIORITY_AVAIL_FILTER.toLowerName()),
        AffiliateSettingConstants.DEFAULT_BRAND_PRIORITY_AVAIL_FILTER);
  }

  @Override
  @JsonIgnore
  public String getC4sBrandPriorityAvailFilter() {
    return StringUtils.defaultString(
        affSettings
            .get(SettingsKeys.Affiliate.Settings.C4S_BRAND_PRIORITY_AVAIL_FILTER.toLowerName()),
        AffiliateSettingConstants.DEFAULT_BRAND_PRIORITY_AVAIL_FILTER);
  }

  @Override
  public boolean isAvailIcon() {
    return BooleanUtils.toBoolean(
        affSettings.get(SettingsKeys.Affiliate.Availability.AVAILABILITY_ICON.toLowerName()));
  }

  @Override
  public boolean isDropShipmentAvailability() {
    return BooleanUtils.toBoolean(affSettings
        .get(SettingsKeys.Affiliate.Availability.DROP_SHIPMENT_AVAILABILITY.toLowerName()));
  }

  @Override
  public List<SettingLanguageDto> getDetailAvailText() {
    List<SettingLanguageDto> detailAvailText = new ArrayList<>();
    String pattern = SettingsKeys.Affiliate.Availability.DETAIL_AVAIL_TEXT
        .appendWithString(SagConstants.UNDERSCORE);
    Map<String, String> detailAvailTextMap = getDetailAvailTextMap();
    if (!detailAvailTextMap.isEmpty()) {
      detailAvailTextMap.entrySet().forEach(t -> detailAvailText.add(
          SettingLanguageDto.builder()
          .langIso(getLangIsoFromAvailSettingKeyByPattern(t.getKey(), pattern))
          .content(t.getValue())
          .build()));
    }

    return detailAvailText;
  }

  @Override
  public List<SettingLanguageDto> getListAvailText() {
    List<SettingLanguageDto> listAvailText = new ArrayList<>();
    String pattern =
        SettingsKeys.Affiliate.Availability.LIST_AVAIL_TEXT.appendWithString(SagConstants.UNDERSCORE);
    Map<String, String> listAvailTextMap = getListAvailTextMap();
    if (!listAvailTextMap.isEmpty()) {
      listAvailTextMap.entrySet().forEach(t -> listAvailText.add(
          SettingLanguageDto.builder()
          .langIso(getLangIsoFromAvailSettingKeyByPattern(t.getKey(), pattern))
          .content(t.getValue())
          .build()));
    }

    return listAvailText;
  }

  @JsonIgnore
  private Map<String, String> getListAvailTextMap() {
    Map<String, String> findByKeyPattern =
        findByKeyPattern(SettingsKeys.Affiliate.Availability.LIST_AVAIL_TEXT
            .appendWithString(SagConstants.UNDERSCORE));
    Map<String, String> result = new HashMap<>();
    if (findByKeyPattern.size() > 0) {
      findByKeyPattern.entrySet().forEach(t -> result.put(t.getKey(), t.getValue()));
    }
    return result;
  }

  @JsonIgnore
  private Map<String, String> findByKeyPattern(String pattern) {
    Map<String, String> result = new HashMap<>();
     affSettings.entrySet().forEach(t -> {
      if (t.getKey().contains(pattern)) {
        result.put(t.getKey(), t.getValue());
      }
    });
     return result;
  }

  @JsonIgnore
  private Map<String, String> getDetailAvailTextMap() {
    Map<String, String> findByKeyPattern =
        findByKeyPattern(SettingsKeys.Affiliate.Availability.DETAIL_AVAIL_TEXT
            .appendWithString(SagConstants.UNDERSCORE));
    Map<String, String> result = new HashMap<>();
    if (findByKeyPattern.size() > 0) {
      findByKeyPattern.entrySet().forEach(t -> result.put(t.getKey(), t.getValue()));
    }
    return result;

  }

  @JsonIgnore
  public String getLangIsoFromAvailSettingKeyByPattern(String key, String pattern) {
    if (key.contains(pattern)) {
      String[] keyAndLangIso = key.split(pattern);
      if (keyAndLangIso.length > 1) {
        return keyAndLangIso[1];
      }
    }
    return null;
  }

  @Override
  public boolean isKsoEnabled() {
    return toBooleanValue(
        affSettings.get(SettingsKeys.Affiliate.Settings.IS_KSO_ENABLED.toLowerName()));
  }

  @Override
  public boolean isDisabledBrandPriorityAvailability() {
    return toBooleanValue(affSettings
        .get(SettingsKeys.Affiliate.Settings.DISABLED_BRAND_PRIORITY_AVAILABILITY.toLowerName()));
  }

  @Override
  public String getOptimizelyId() {
    return affSettings.get(SettingsKeys.Affiliate.Settings.OPTIMIZELY_ID.toLowerName());
  }

}
