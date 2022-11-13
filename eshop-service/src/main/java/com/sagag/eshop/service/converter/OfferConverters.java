package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.offer.Offer;
import com.sagag.eshop.service.dto.offer.OfferDto;

import lombok.experimental.UtilityClass;

/**
 * Converter for offer
 */
@UtilityClass
public final class OfferConverters {

  public static OfferDto convert(final Offer entity) {
    return new OfferDto(entity);
  }

}
