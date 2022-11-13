package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.elasticsearch.api.impl.articles.ArticleLoopSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.ArtNrAndSupplierIdQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ArtNrAndSupplierIdSearchServiceImpl extends ArticleLoopSearchService {

  @Autowired
  private ArtNrAndSupplierIdQueryBuilder queryBuilder;

  @Override
  public Page<ArticleDoc> search(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return Page.empty();
    }
    return searchArticlesLoop(criteria, pageable, false);
  }

  /**
   * Search article by number with supplier or gaid.
   *
   * @param criteria {@link KeywordArticleSearchCriteria} the article criteria for searching
   * @param pageable the searching page request
   * @return the object response of {@link ArticleFilteringResponse}
   *
   */
  @Override
  public ArticleFilteringResponse filter(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return ArticleFilteringResponse.empty();
    }
    return filterArticlesLoop(criteria, pageable, false);
  }

  @Override
  public ISearchQueryBuilder<KeywordArticleSearchCriteria> queryBuilder() {
    return queryBuilder;
  }

}
