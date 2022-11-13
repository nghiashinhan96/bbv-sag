package com.sagag.services.ivds.converter.fitment;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.FitmentArticleDto;
import com.sagag.services.elasticsearch.config.ArticleIdFieldMapper;
import com.sagag.services.elasticsearch.domain.article.FitmentArticle;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FitmentArticleMapper {

  @Autowired
  private ArticleIdFieldMapper articleIdFieldMapper;

  public Function<FitmentArticle, String> keyExtractor() {
    return articleIdFieldMapper.articleIdExtractor();
  }

  public Function<FitmentArticle, FitmentArticleDto> fitmentConverter() {
    return fitment -> {
      FitmentArticleDto fArtDto = SagBeanUtils.map(fitment, FitmentArticleDto.class);
      fArtDto.setArtId(keyExtractor().apply(fitment));
      return fArtDto;
    };
  }

}
