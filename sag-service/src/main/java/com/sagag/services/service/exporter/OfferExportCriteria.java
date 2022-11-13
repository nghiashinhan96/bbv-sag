package com.sagag.services.service.exporter;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;

import lombok.Data;

/**
 * Criteria class for export offer.
 */
@Data
public class OfferExportCriteria {

  private final UserInfo user;

  private final OfferDto offer;

  public static OfferExportCriteria of(final UserInfo user, OfferDto offer) {
    return new OfferExportCriteria(user, offer);
  }
}
