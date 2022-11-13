package com.sagag.services.rest.mapper;

import com.sagag.services.ivds.response.FreetextResponseDto;

import org.mapstruct.Mapper;

import java.util.Objects;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GenericFreetextResponseDtoMapper
    extends EntityMapper<FreetextResponseDto, FreetextResponseDto> {


  @Override
  default FreetextResponseDto toDto(FreetextResponseDto entity) {
    if (Objects.isNull(entity)) {
      return null;
    }
    final FreetextResponseDto retDto = new FreetextResponseDto();
    if (Objects.nonNull(entity.getArticles())) {
      FreetextArticleDocDtoConverter convert = new FreetextArticleDocDtoConverter();
      retDto.setArticles(entity.getArticles().map(convert::convert));
      retDto.setContextKey(entity.getContextKey());
    }
    retDto.setVehData(entity.getVehData());
    retDto.setUnitreeData(entity.getUnitreeData());
    return retDto;
  }

}
