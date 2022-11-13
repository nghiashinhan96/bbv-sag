package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BoAffiliateSettingRequest implements Serializable {

  private static final long serialVersionUID = 2107338250294132718L;

  private String shortName;

  private Double vat;

  private Boolean customerAbsEnabled;

  private Boolean salesAbsEnabled;
  
  private String vatTypeDisplay;

  private String customerBrandPriorityAvailFilter;

  private String c4sBrandPriorityAvailFilter;

  private BoAffiliateSettingAvailabiltyDto availSetting;
  
  private List<AffiliateAvailabilityDisplaySettingDto> availDisplaySettings;

  private Boolean ksoEnabled;

  private Boolean customerBrandFilterEnabled;

  private Boolean salesBrandFilterEnabled;

  private Boolean invoiceRequestAllowed;

  private String invoiceRequestEmail;
  
  private Boolean disabledBrandPriorityAvailability;
  
  private String optimizelyId;

  private AffiliateExternalPartSettingDto externalPartSettings;

}
