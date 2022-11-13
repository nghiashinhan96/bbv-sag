package com.sagag.services.ivds.converter.article;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.converter.ExternalPartConverter;
import com.sagag.services.elasticsearch.domain.article.ExternalPartDoc;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExternalPartArticleConverter implements Function<ExternalPartDoc, ArticleDocDto> {

  @Autowired
  private ExternalPartConverter externalPartConverter;

  @Autowired
  private ArticleConverter articleConverter;

  @Override
  public ArticleDocDto apply(ExternalPartDoc externalPartDoc) {
    return externalPartConverter.andThen(articleConverter).apply(externalPartDoc);
  }

}
