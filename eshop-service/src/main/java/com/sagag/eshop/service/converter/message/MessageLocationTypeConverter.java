package com.sagag.eshop.service.converter.message;

import com.sagag.eshop.repo.entity.message.MessageLocationType;
import com.sagag.services.domain.eshop.message.dto.MessageLocationTypeDto;
import com.sagag.services.domain.eshop.message.dto.MessageRoleTypeDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MessageLocationTypeConverter {

  public static MessageLocationTypeDto fromEntity(MessageLocationType entity) {
    // @formatter:off
    List<MessageRoleTypeDto> roleTypes =
        CollectionUtils.emptyIfNull(entity.getMessageLocationTypeRoleTypes())
            .stream()
            .map(item -> MessageRoleTypeConverter.fromEntity(item.getMessageRoleType()))
            .collect(Collectors.toList());
    return MessageLocationTypeDto.builder()
        .id(entity.getId())
        .locationType(entity.getLocationType())
        .description(entity.getDescription())
        .roleTypes(roleTypes).build();
    // @formatter:on
  }

  public static List<MessageLocationTypeDto> fromEntities(List<MessageLocationType> entities) {
    // @formatter:off
    return CollectionUtils.emptyIfNull(entities)
        .stream()
        .map(MessageLocationTypeConverter::fromEntity)
        .collect(Collectors.toList());
    // @formatter:on
  }
}
