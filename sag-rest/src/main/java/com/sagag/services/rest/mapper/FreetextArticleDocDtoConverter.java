package com.sagag.services.rest.mapper;

import com.sagag.services.domain.article.ArticleDocDto;

import org.springframework.core.convert.converter.Converter;

/**
 * Converter class to convert the ArticleDocDto to ArticleDto using for Freetext search.
 */
public class FreetextArticleDocDtoConverter implements Converter<ArticleDocDto, ArticleDocDto> {

  @Override
  public ArticleDocDto convert(ArticleDocDto source) {
    final ArticleDocDto dto = new ArticleDocDto();
    dto.setId(source.getId());
    dto.setIdSagsys(source.getIdSagsys());
    dto.setGenArtTxts(source.getGenArtTxts());
    dto.setSupplier(source.getSupplier());
    dto.setArtnrDisplay(source.getArtnrDisplay());
    dto.setAvailRequested(source.isAvailRequested());
    dto.setRelevanceGroupType(source.getRelevanceGroupType());
    dto.setProductAddon(source.getProductAddon());
    dto.setIcat(source.getIcat());
    dto.setIcat2(source.getIcat2());
    dto.setIcat3(source.getIcat3());
    dto.setIcat4(source.getIcat4());
    dto.setIcat5(source.getIcat5());
    return dto;
  }

}
