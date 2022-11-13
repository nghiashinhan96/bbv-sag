package com.sagag.eshop.service.converter.message;

import com.sagag.eshop.repo.entity.message.MessageRoleType;
import com.sagag.services.domain.eshop.message.dto.MessageRoleTypeDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class support for convert MessageRoleType.
 *
 *
 */
@UtilityClass
public class MessageRoleTypeConverter {

  public static MessageRoleTypeDto fromEntity(MessageRoleType entity) {
    // @formatter:off
    return MessageRoleTypeDto.builder()
        .id(entity.getId())
        .roleType(entity.getRoleType())
        .description(entity.getDescription())
        .accessRights(MessageAccessRightConverter.fromEntities(entity.getMessageAccessRights()))
        .build();
    // @formatter:on
  }

  public static List<MessageRoleTypeDto> fromEntities(List<MessageRoleType> entities) {
    // @formatter:off
    return CollectionUtils.emptyIfNull(entities)
        .stream()
        .map(MessageRoleTypeConverter::fromEntity)
        .collect(Collectors.toList());
    // @formatter:on
  }
}
