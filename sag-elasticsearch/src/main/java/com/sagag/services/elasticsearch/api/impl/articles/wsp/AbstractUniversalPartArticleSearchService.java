package com.sagag.services.elasticsearch.api.impl.articles.wsp;

import com.sagag.services.common.contants.UniversalPartConstants;
import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.UniversalPartArticleResponse;
import com.sagag.services.elasticsearch.utils.EsResultUtils;

import java.util.function.Supplier;

public abstract class AbstractUniversalPartArticleSearchService<T extends UniversalPartArticleSearchCriteria>
    extends AbstractArticleElasticsearchService
    implements IArticleSearchService<T, UniversalPartArticleResponse> {

  protected ArticleFilteringResponse filterOrThrowIfResultOverLimit(
      Supplier<ArticleFilteringResponse> supplier) {
    return EsResultUtils.getOrThrowIfResultOverLimit(supplier,
        UniversalPartConstants.VIEW_WSP_ARTICLES_MAX_PAGE_SIZE);
  }

}
