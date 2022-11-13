package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.config.ArticleIdFieldMapper;
import com.sagag.services.elasticsearch.criteria.article.ArticleIdListSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArticleIdListArticleQueryBuilder
  extends AbstractArticleQueryBuilder<ArticleIdListSearchCriteria> implements IAggregationBuilder {

  @Autowired
  private ArticleIdFieldMapper articleIdFieldMapper;

  @Override
  public SearchQuery buildQuery(ArticleIdListSearchCriteria criteria, Pageable pageable,
    String... indices) {
    log.debug("Building the search query for criteria = {}", criteria);
    final List<String> articleIdList = criteria.getValidArticleIdList();
    final String[] affNameLocks = criteria.getAffNameLocks();
    final boolean isSaleOnBehalf = criteria.isSaleOnBehalf();
    String articleIdField = articleIdFieldMapper.getField().value();
    if (criteria.isUseDefaultArtId()) {
      articleIdField = ArticleField.ID_SAGSYS.value();
    }
    BoolQueryBuilder searchQuery = applyCommonQueryBuilder(affNameLocks, isSaleOnBehalf)
      .apply(QueryBuilders.boolQuery()
        .must(QueryBuilders.termsQuery(articleIdField, articleIdList)));

    aggFilterBuilders.forEach(builder -> builder.addFilter(searchQuery, criteria));

    log.debug("Query = {}", searchQuery);

    final NativeSearchQueryBuilder queryBuilder = ArticleQueryUtils.nativeQueryBuilder(
      pageable, indices).withQuery(searchQuery);

    if (criteria.isAggregated()) {
      aggregated(queryBuilder, ArticleField.GA_ID, ArticleField.CRITERIA_CID);
    }
    return queryBuilder.build();
  }
}
