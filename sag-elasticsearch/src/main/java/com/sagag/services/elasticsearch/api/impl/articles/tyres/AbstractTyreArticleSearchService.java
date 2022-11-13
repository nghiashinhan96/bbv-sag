package com.sagag.services.elasticsearch.api.impl.articles.tyres;

import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.BaseTyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.TyreArticleResponse;
import com.sagag.services.elasticsearch.utils.EsResultUtils;
import java.util.function.Supplier;

public abstract class AbstractTyreArticleSearchService<T extends BaseTyreArticleSearchCriteria>
  extends AbstractArticleElasticsearchService
  implements IArticleSearchService<T, TyreArticleResponse> {

  protected TyreArticleResponse searchOrThrowIfResultOverLimit(
      Supplier<TyreArticleResponse> supplier) {
    return EsResultUtils.getOrThrowIfTyreResultOverLimit(supplier,
        TyreConstants.VIEW_TYRE_ARTICLES_MAX_PAGE_SIZE);
  }

  protected ArticleFilteringResponse filterOrThrowIfResultOverLimit(
      Supplier<ArticleFilteringResponse> supplier) {
    return EsResultUtils.getOrThrowIfResultOverLimit(supplier,
        TyreConstants.VIEW_TYRE_ARTICLES_MAX_PAGE_SIZE);
  }

}
