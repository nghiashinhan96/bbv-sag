package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.order.VOrderHistory;
import com.sagag.eshop.service.dto.order.SaleOrderHistoryDto;

import lombok.experimental.UtilityClass;

import org.springframework.core.convert.converter.Converter;

/**
 * Converter for VOrderHistory
 */
@UtilityClass
public final class VOrderHistoryConverters {

  public static SaleOrderHistoryDto convertToGeneralDto(final VOrderHistory entity) {
    return new SaleOrderHistoryDto(entity);
  }

  public static Converter<VOrderHistory, SaleOrderHistoryDto> pageOfferConverter() {
    return VOrderHistoryConverters::convertToGeneralDto;
  }
}
