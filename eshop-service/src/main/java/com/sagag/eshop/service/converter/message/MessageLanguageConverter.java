package com.sagag.eshop.service.converter.message;

import com.sagag.eshop.repo.entity.message.MessageLanguage;
import com.sagag.services.domain.eshop.message.dto.MessageLanguageDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MessageLanguageConverter {

  public static MessageLanguageDto fromEntity(MessageLanguage entity) {
    // @formatter:off
    return MessageLanguageDto.builder()
        .langIso(entity.getLangIso())
        .content(entity.getContent())
        .build();
    // @formatter:on
  }

  public static List<MessageLanguageDto> fromEntities(List<MessageLanguage> entities) {
    // @formatter:off
    return CollectionUtils.emptyIfNull(entities)
        .stream()
        .map(MessageLanguageConverter::fromEntity)
        .collect(Collectors.toList());
    // @formatter:on
  }
}
