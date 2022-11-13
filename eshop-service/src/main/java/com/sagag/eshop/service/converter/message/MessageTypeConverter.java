package com.sagag.eshop.service.converter.message;

import com.sagag.eshop.repo.entity.message.MessageType;
import com.sagag.services.domain.eshop.message.dto.MessageTypeDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MessageTypeConverter {

  public static MessageTypeDto fromEntity(MessageType entity) {
    // @formatter:off
    return MessageTypeDto.builder()
        .id(entity.getId())
        .type(entity.getType())
        .description(entity.getDescription())
        .build();
    // @formatter:on
  }

  public static List<MessageTypeDto> fromEntities(List<MessageType> entities) {
    // @formatter:off
    return CollectionUtils.emptyIfNull(entities)
        .stream()
        .map(MessageTypeConverter::fromEntity)
        .collect(Collectors.toList());
    // @formatter:on
  }
}
