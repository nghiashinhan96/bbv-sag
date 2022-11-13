package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.offer.ViewOfferPerson;
import com.sagag.eshop.service.dto.offer.ViewOfferPersonDto;

import lombok.experimental.UtilityClass;

import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

/**
 * Converter for view offer person
 */
@UtilityClass
public final class ViewOfferPersonConverters {

  public static ViewOfferPersonDto convert(final ViewOfferPerson entity) {
    return new ViewOfferPersonDto(entity);
  }

  public static Converter<ViewOfferPerson, ViewOfferPersonDto> pageOfferPersonConverter() {
    return ViewOfferPersonConverters::convert;
  }

  public static Function<ViewOfferPerson, ViewOfferPersonDto> optionalOfferPersonConverter() {
    return ViewOfferPersonConverters::convert;
  }
}
