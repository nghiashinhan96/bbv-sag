package com.sagag.services.domain.eshop.dto;

import com.sagag.services.common.enums.offer.OfferFormatAlignmentType;
import com.sagag.services.common.enums.offer.OrganisationPropertyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * DTO organisation properties for offer.
 *
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrgPropertyOfferDto implements Serializable {

  private static final String DEFAULT_PRINT_VENDOR_ADDR = "true";

  private static final long serialVersionUID = -8810032615608733612L;

  private String footerText;

  private String formatAlign;

  private String generalCrossing;

  private String printVendorAddr;

  public String getFormatAlign() {
    return this.formatAlign.toUpperCase();
  }

  public String getPrintVendorAddr() {
    return this.printVendorAddr.toUpperCase();
  }

  public OrgPropertyOfferDto(final List<OrganisationPropertyDto> orgPropDto) {

    final Map<String, String> orgPropMap = orgPropDto.stream().collect(
        Collectors.toMap(OrganisationPropertyDto::getType, OrganisationPropertyDto::getValue));

    this.footerText = StringUtils
        .defaultString(orgPropMap.get(OrganisationPropertyType.OFFER_FOOTER_TEXT.getValue()));

    this.formatAlign = StringUtils.defaultString(
        orgPropMap.get(OrganisationPropertyType.OFFER_FORMAT_ALIGNMENT.getValue()),
        OfferFormatAlignmentType.RIGHT.getValue());

    this.generalCrossing = StringUtils
        .defaultString(orgPropMap.get(OrganisationPropertyType.OFFER_GENERAL_CROSSING.getValue()));

    this.printVendorAddr = StringUtils.defaultString(
        orgPropMap.get(OrganisationPropertyType.OFFER_PRINT_VENDOR_ADDR.getValue()),
        DEFAULT_PRINT_VENDOR_ADDR);
  }
}
