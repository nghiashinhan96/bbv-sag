package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.eshop.service.dto.offer.OfferPersonDto;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

/**
 * Converter for offer person
 */
@UtilityClass
public final class OfferPersonConverters {

  public static OfferPersonDto convert(final OfferPerson entity) {
    return new OfferPersonDto(entity);
  }

  public static Function<OfferPerson, OfferPersonDto> optionalOfferPersonConverter() {
    return OfferPersonConverters::convert;
  }
}
