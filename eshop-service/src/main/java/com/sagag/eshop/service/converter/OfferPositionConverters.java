package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.offer.OfferPosition;
import com.sagag.eshop.service.dto.offer.OfferPositionDto;

import lombok.experimental.UtilityClass;

/**
 * Converter for offer
 */
@UtilityClass
public final class OfferPositionConverters {

  public static OfferPositionDto convert(final OfferPosition entity) {
    return new OfferPositionDto(entity);
  }

}
