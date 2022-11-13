package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BackOfficeAffiliateInfoDto extends BackOfficeAffiliateShortInfoDto
    implements Serializable {

  private static final long serialVersionUID = 3004033891531377450L;

  private String description;

  private String orgCode;

  private double vat;

  private boolean customerAbsEnabled;

  private boolean salesAbsEnabled;
  
  private String vatTypeDisplay;

  private String customerBrandPriorityAvailFilter;

  private String c4sBrandPriorityAvailFilter;

  private BoAffiliateSettingAvailabiltyDto availSetting;
  
  private List<AffiliateAvailabilityDisplaySettingDto> availDisplaySettings;

  private AffiliateExternalPartSettingDto externalPartSettings;

  private Boolean ksoEnabled;

  private boolean customerBrandFilterEnabled;

  private boolean salesBrandFilterEnabled;

  private boolean invoiceRequestAllowed;

  private String invoiceRequestEmail;
  
  private Boolean disabledBrandPriorityAvailability;

  private String optimizelyId;
}
