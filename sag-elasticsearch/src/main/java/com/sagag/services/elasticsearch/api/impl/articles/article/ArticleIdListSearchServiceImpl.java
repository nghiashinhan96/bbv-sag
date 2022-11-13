package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.ArticleIdListSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.query.articles.article.ArticleIdListArticleQueryBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class ArticleIdListSearchServiceImpl extends AbstractArticleElasticsearchService
  implements IArticleSearchService<ArticleIdListSearchCriteria, Page<ArticleDoc>> {

  @Autowired
  private ArticleIdListArticleQueryBuilder queryBuilder;

  @Override
  public Page<ArticleDoc> search(ArticleIdListSearchCriteria criteria, Pageable pageable) {
    if (CollectionUtils.isEmpty(criteria.getValidArticleIdList())) {
      return Page.empty();
    }
    final SearchQuery query = queryBuilder.buildQuery(criteria, pageable, index());
    return searchPage(query, ArticleDoc.class);
  }

  @Override
  public ArticleFilteringResponse filter(ArticleIdListSearchCriteria criteria, Pageable pageable) {
    if (CollectionUtils.isEmpty(criteria.getArticleIdList())) {
      return ArticleFilteringResponse.empty();
    }
    criteria.onAggregated();
    final SearchQuery query = queryBuilder.buildQuery(criteria, pageable, index());
    return search(query, filteringExtractor(pageable));
  }
}
