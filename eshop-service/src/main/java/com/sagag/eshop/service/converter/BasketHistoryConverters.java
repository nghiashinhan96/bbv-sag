package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.VBasketHistory;
import com.sagag.eshop.service.dto.BasketHistoryDto;

import lombok.experimental.UtilityClass;

import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

/**
 * Utility provide some converters of basket history.
 */
@UtilityClass
public final class BasketHistoryConverters {

  public static Converter<VBasketHistory, BasketHistoryDto> defaultBasketHistoryConverter() {
    return entity -> new BasketHistoryDto(entity, false);
  }

  public static Function<VBasketHistory, BasketHistoryDto> ignoreItemBasketHistoryConverter() {
    return entity -> new BasketHistoryDto(entity, true);
  }

  public static Function<VBasketHistory, BasketHistoryDto> optionalBasketHistoryConverter() {
    return entity -> new BasketHistoryDto(entity, false);
  }
}
