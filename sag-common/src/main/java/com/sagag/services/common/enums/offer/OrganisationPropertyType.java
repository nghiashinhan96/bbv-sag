package com.sagag.services.common.enums.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrganisationPropertyType {

  OFFER_FOOTER_TEXT("offerFooterText"),
  OFFER_FORMAT_ALIGNMENT("offerFormatAlignment"),
  OFFER_GENERAL_CROSSING("offerGeneralCrossing"),
  OFFER_PRINT_VENDOR_ADDR("offerPrintVendorAddr");

  private String value;

}
