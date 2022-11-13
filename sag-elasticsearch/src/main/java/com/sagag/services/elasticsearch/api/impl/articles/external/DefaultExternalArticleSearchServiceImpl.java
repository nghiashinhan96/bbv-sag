package com.sagag.services.elasticsearch.api.impl.articles.external;

import com.sagag.services.elasticsearch.criteria.article.KeywordExternalArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.profiles.EsExternalArticleServiceMode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@EsExternalArticleServiceMode
public class DefaultExternalArticleSearchServiceImpl extends ExternalArticleSearchService {

  @Override
  public Page<ArticleDoc> search(KeywordExternalArticleSearchCriteria criteria, Pageable pageable) {
    return Optional.of(searchPageLoop(criteria.getSearchQueries(), ArticleDoc.class))
      .filter(Page::hasContent).orElseGet(() -> super.search(criteria, pageable));
  }

  @Override
  public ArticleFilteringResponse filter(KeywordExternalArticleSearchCriteria criteria,
    Pageable pageable) {
    return Optional.of(filterLoop(criteria.getSearchQueries(), filteringExtractor(pageable)))
      .filter(ArticleFilteringResponse::hasContent)
      .orElseGet(() -> super.filter(criteria, pageable));
  }
}
