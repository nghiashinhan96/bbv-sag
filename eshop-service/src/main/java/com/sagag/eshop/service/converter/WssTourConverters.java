package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.WssTour;
import com.sagag.services.domain.eshop.dto.WssTourDto;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

/**
 * Converter for WSS Tour
 */
@UtilityClass
public final class WssTourConverters {

  public static WssTourDto convertToWssTourDto(final WssTour wssTour) {
    if (wssTour == null) {
      return null;
    }
    return WssTourDto.builder().id(wssTour.getId()).name(wssTour.getName())
        .orgId(wssTour.getOrgId()).wssTourTimesDtos(
            WssTourTimesConverters.convertWssTourTimesToDto(wssTour.getWssTourTimes()))
        .build();
  }

  public static WssTour convertFromDto(final WssTourDto wssTourDto) {
    return WssTour.builder().orgId(wssTourDto.getOrgId()).name(wssTourDto.getName())
        .wssTourTimes(
            WssTourTimesConverters.convertWssTourTimesFromDtos(wssTourDto.getWssTourTimesDtos()))
        .build();
  }

  public static Function<WssTour, WssTourDto> optionalTourConverter() {
    return WssTourConverters::convertToWssTourDto;
  }

}
