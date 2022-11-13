package com.sagag.eshop.service.dto;

import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.AffiliateExternalPartSettingDto;
import com.sagag.services.domain.eshop.dto.SettingLanguageDto;

import java.io.Serializable;
import java.util.List;

public interface ISettings extends Serializable {

  boolean isNetPriceView();

  boolean isEmailOrderConfirmation();

  String getAffiliateEmail();

  String getAffiliateDefaultUrl();

  String getAffiliateEhPortalUrl();

  String getGaTrackingCode();

  boolean isShowTyresDiscount();

  boolean isShowTyresGrossPriceHeader();

  int getSessionTimeoutSeconds();

  double getVatRate();

  boolean isViewBilling();

  boolean isNetPriceConfirm();

  boolean isShowHappyPoints();

  boolean isAcceptHappyPointTerm();

  String getOciEwbSchemaType();

  String getOciVendorId();

  String getBranchName();

  boolean isCustomerAbsEnabled();

  boolean isSalesAbsEnabled();
  
  String getAffVatTypeDisplay();

  String getCustomerBrandPriorityAvailFilter();

  String getC4sBrandPriorityAvailFilter();

  boolean isAvailIcon();

  boolean isDropShipmentAvailability();

  List<SettingLanguageDto> getDetailAvailText();

  List<SettingLanguageDto> getListAvailText();
  
  List<AffiliateAvailabilityDisplaySettingDto> getAvailDisplaySettingsConverted();

  AffiliateExternalPartSettingDto getExternalPartSettingsConverted();

  boolean isKsoEnabled();

  boolean isCustomerBrandFilterEnabled();

  boolean isSalesBrandFilterEnabled();

  boolean isAdditionalRecommendationEnabled();

  boolean isInvoiceRequestAllowed();

  String getInvoiceRequestEmail();
  
  String getGoogleTagManagerSetting();
  
  boolean isDisabledBrandPriorityAvailability();
  
  String getOptimizelyId();

}
