package com.sagag.eshop.service.converter.message;

import com.sagag.eshop.repo.entity.message.MessageAccessRight;
import com.sagag.services.domain.eshop.message.dto.MessageAccessRightDto;
import com.sagag.services.domain.eshop.message.dto.MessageAreaDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MessageAccessRightConverter {

  public static MessageAccessRightDto fromEntity(MessageAccessRight entity) {
    // @formatter:off
    List<MessageAreaDto> areas =
        CollectionUtils.emptyIfNull(entity.getMessageAccessRightAreas()).stream()
            .map(item -> MessageAreaConvert.fromEntity(item.getMessageArea()))
            .collect(Collectors.toList());
    return MessageAccessRightDto.builder()
        .id(entity.getId())
        .userGroup(entity.getUserGroup())
        .userGroupKey(entity.getUserGroupKey())
        .areas(areas).build();
    // @formatter:on
  }

  public static List<MessageAccessRightDto> fromEntities(List<MessageAccessRight> entities) {
    // @formatter:off
    return CollectionUtils.emptyIfNull(entities)
        .stream()
        .map(MessageAccessRightConverter::fromEntity)
        .collect(Collectors.toList());
    // @formatter:on
  }
}
