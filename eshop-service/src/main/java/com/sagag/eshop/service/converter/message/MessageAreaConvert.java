package com.sagag.eshop.service.converter.message;

import com.sagag.eshop.repo.entity.message.MessageArea;
import com.sagag.services.domain.eshop.message.dto.MessageAreaDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MessageAreaConvert {

  public static MessageAreaDto fromEntity(MessageArea entity) {
    // @formatter:off
    return MessageAreaDto.builder()
        .id(entity.getId())
        .area(entity.getArea())
        .description(entity.getDescription())
        .subAreas(MessageSubAreaConverter.fromEntities(entity.getMessageSubAreas())).build();
    // @formatter:on
  }

  public static List<MessageAreaDto> fromEntities(List<MessageArea> entities) {
    // @formatter:off
    return CollectionUtils.emptyIfNull(entities)
        .stream()
        .map(MessageAreaConvert::fromEntity)
        .collect(Collectors.toList());
    // @formatter:on
  }
}
