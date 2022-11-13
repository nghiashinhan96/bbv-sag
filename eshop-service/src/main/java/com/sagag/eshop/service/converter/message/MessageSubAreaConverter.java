package com.sagag.eshop.service.converter.message;

import com.sagag.eshop.repo.entity.message.MessageSubArea;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.message.dto.MessageSubAreaDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MessageSubAreaConverter {

  public static MessageSubAreaDto fromEntity(MessageSubArea entity) {
    return SagBeanUtils.map(entity, MessageSubAreaDto.class);
  }

  public static List<MessageSubAreaDto> fromEntities(List<MessageSubArea> entities) {
    // @formatter:off
    return CollectionUtils.emptyIfNull(entities)
        .stream()
        .map(MessageSubAreaConverter::fromEntity)
        .collect(Collectors.toList());
    // @formatter:on
  }
}
