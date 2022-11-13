package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.elasticsearch.api.impl.articles.ArticleLoopSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ArticleNumberAndSupplierIdFilterImpl implements IArticleFilter {

  @Autowired
  @Qualifier("artNrAndSupplierIdSearchServiceImpl")
  private ArticleLoopSearchService artNrAndSupplierSearchService;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(request.getKeyword(), affNameLocks);
    criteria.setIdDlnr(request.getIdDlnr());
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    return artNrAndSupplierSearchService.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.ONLY_ARTICLE_NUMBER_AND_SUPPLIER;
  }

}
