package com.sagag.services.haynespro.config;

import com.sagag.services.common.enums.SupportedAffiliate;

import lombok.Data;

@Data
public class HaynesProLicenseProperty {

  private String derendingerChEmail;

  private String technomagEmail;

  private String defaultEmail;

  private String atEmail;

  public String getEmailByAffiliate(SupportedAffiliate aff) {
    if (aff == null) {
      return defaultEmail;
    }
    switch (aff) {
      case DERENDINGER_CH:
        return derendingerChEmail;
      case TECHNOMAG:
        return technomagEmail;
      case DERENDINGER_AT:
      case MATIK_AT:
        return atEmail;
      default:
          return defaultEmail;
    }
  }

}
