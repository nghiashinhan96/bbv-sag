package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.WssMarginsArticleGroup;
import com.sagag.eshop.service.dto.WssMarginArticleGroupDto;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.WssDesignationsDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Objects;

/**
 * Converter for Wss Margin Article Group
 */
@UtilityClass
public final class WssMarginArticleGroupConverters {

  public static WssMarginsArticleGroup convertToEntity(final WssMarginArticleGroupDto dto) {
    return WssMarginsArticleGroup.builder().margin1(dto.getMargin1()).margin2(dto.getMargin2())
        .margin3(dto.getMargin3()).margin4(dto.getMargin4()).margin5(dto.getMargin5())
        .margin6(dto.getMargin6()).margin7(dto.getMargin7()).id(dto.getId()).leafId(dto.getLeafId())
        .parentLeafId(dto.getParentLeafId())
        .sagArticleGroupDesc(SagJSONUtil.convertObjectToJson(dto.getSagArticleGroupDesc()))
        .sagArtGroup(dto.getSagArticleGroup()).customArticleGroup(dto.getCustomArticleGroup())
        .customArticleGroupDesc(dto.getCustomArticleGroupDesc()).isMapped(dto.isMapped())
        .isDefault(dto.isDefault()).build();

  }

  public static WssMarginArticleGroupDto convertToDto(final WssMarginsArticleGroup entity) {
    return WssMarginArticleGroupDto.builder().orgId(entity.getOrgId()).margin1(entity.getMargin1())
        .margin2(entity.getMargin2()).margin3(entity.getMargin3()).margin4(entity.getMargin4())
        .margin5(entity.getMargin5()).margin6(entity.getMargin6()).margin7(entity.getMargin7())
        .id(entity.getId()).leafId(entity.getLeafId()).parentLeafId(entity.getParentLeafId())
        .sagArticleGroup(entity.getSagArtGroup())
        .sagArticleGroupDesc(SagJSONUtil.convertArrayJsonToList(entity.getSagArticleGroupDesc(),
            WssDesignationsDto.class))
        .customArticleGroup(entity.getCustomArticleGroup())
        .customArticleGroupDesc(entity.getCustomArticleGroupDesc()).isMapped(entity.isMapped())
        .isDefault(entity.isDefault())
        .hasChild(CollectionUtils.isNotEmpty(entity.getWssMarginsArticleGroup()))
        .level(entity.getGroupLevel())
        .isRoot(Objects.isNull(entity.getParentId())).build();
  }

  public static void updateToTargetProperties(final WssMarginArticleGroupDto updated,
      final WssMarginsArticleGroup entity) {
    entity.setCustomArticleGroup(updated.getCustomArticleGroup());
    entity.setCustomArticleGroupDesc(updated.getCustomArticleGroupDesc());
    entity.setMargin1(updated.getMargin1());
    entity.setMargin2(updated.getMargin2());
    entity.setMargin3(updated.getMargin3());
    entity.setMargin4(updated.getMargin4());
    entity.setMargin5(updated.getMargin5());
    entity.setMargin6(updated.getMargin6());
    entity.setMargin7(updated.getMargin7());
    entity.setMapped(true);
  }
}
