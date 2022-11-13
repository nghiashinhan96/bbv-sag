package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.WssMarginsBrand;
import com.sagag.eshop.service.dto.WssMarginBrandDto;

import lombok.experimental.UtilityClass;

/**
 * Converter for Wss Margin Brand
 */
@UtilityClass
public final class WssMarginBrandConverters {

  public static WssMarginsBrand convertToEntity(final WssMarginBrandDto dto) {
    return WssMarginsBrand.builder().brandId(dto.getBrandId()).brandName(dto.getName())
        .margin1(dto.getMargin1())
        .margin2(dto.getMargin2())
        .margin3(dto.getMargin3())
        .margin4(dto.getMargin4())
        .margin5(dto.getMargin5())
        .margin6(dto.getMargin6())
        .margin7(dto.getMargin7())
        .id(dto.getId())
        .isDefault(dto.isDefault())
        .build();

  }

  public static WssMarginBrandDto convertToDto(final WssMarginsBrand entity) {
    return WssMarginBrandDto.builder().brandId(entity.getBrandId())
        .name(entity.getBrandName()).orgId(entity.getOrgId())
        .margin1(entity.getMargin1())
        .margin2(entity.getMargin2())
        .margin3(entity.getMargin3())
        .margin4(entity.getMargin4())
        .margin5(entity.getMargin5())
        .margin6(entity.getMargin6())
        .margin7(entity.getMargin7())
        .id(entity.getId())
        .isDefault(entity.isDefault())
        .build();
  }

  public static void updateToTargetProperties(final WssMarginBrandDto updated,
      final WssMarginsBrand entity) {
    entity.setMargin1(updated.getMargin1());
    entity.setMargin2(updated.getMargin2());
    entity.setMargin3(updated.getMargin3());
    entity.setMargin4(updated.getMargin4());
    entity.setMargin5(updated.getMargin5());
    entity.setMargin6(updated.getMargin6());
    entity.setMargin7(updated.getMargin7());
  }

}
