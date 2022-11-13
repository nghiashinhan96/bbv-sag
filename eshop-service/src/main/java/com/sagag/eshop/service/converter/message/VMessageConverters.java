package com.sagag.eshop.service.converter.message;

import com.sagag.eshop.repo.entity.message.VMessage;
import com.sagag.services.domain.eshop.message.dto.MessageSearchResultDto;

import lombok.experimental.UtilityClass;

import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

@UtilityClass
public class VMessageConverters {

  private static MessageSearchResultDto convert(VMessage vMessage) {
    return MessageSearchResultDto.builder()
        .id(vMessage.getId())
        .title(vMessage.getTitle())
        .type(vMessage.getType())
        .area(vMessage.getArea())
        .subArea(vMessage.getSubArea())
        .locationValue(vMessage.getLocationValue())
        .active(vMessage.isActive())
        .dateValidFrom(vMessage.getDateValidFrom())
        .dateValidTo(vMessage.getDateValidTo())
        .build();
  }

  public static Converter<VMessage, MessageSearchResultDto> toMessageSearchResultItemDto() {
    return VMessageConverters::convert;
  }

  public static Function<VMessage, MessageSearchResultDto> toDto() {
    return VMessageConverters::convert;
  }
}
