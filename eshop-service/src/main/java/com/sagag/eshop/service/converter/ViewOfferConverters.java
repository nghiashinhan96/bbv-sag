package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.offer.ViewOffer;
import com.sagag.eshop.service.dto.offer.OfferGeneralDto;

import lombok.experimental.UtilityClass;

import org.springframework.core.convert.converter.Converter;

/**
 * Converter for ViewOffer
 */
@UtilityClass
public final class ViewOfferConverters {

  public static OfferGeneralDto convertToGeneralDto(final ViewOffer entity) {
    return new OfferGeneralDto(entity);
  }

  public static Converter<ViewOffer, OfferGeneralDto> pageOfferConverter() {
    return ViewOfferConverters::convertToGeneralDto;
  }
}
