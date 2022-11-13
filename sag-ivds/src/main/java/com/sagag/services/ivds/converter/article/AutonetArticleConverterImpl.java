package com.sagag.services.ivds.converter.article;

import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;

import org.springframework.stereotype.Component;

@Component
@AutonetProfile
public class AutonetArticleConverterImpl implements ArticleConverter {

  @Override
  public ArticleDocDto apply(ArticleDoc article) {
    ArticleDocDto articleDto = SagBeanUtils.map(article, ArticleDocDto.class);
    articleDto.setOriginIdSagsys(article.getIdSagsys());
    articleDto.setIdSagsys(article.getIdAutonet());
    articleDto.setId(article.getIdAutonet());
    articleDto.setArtid(article.getIdAutonet());
    return articleDto;
  }

}
