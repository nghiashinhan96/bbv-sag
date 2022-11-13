package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.config.ArticleIdFieldMapper;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

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
public class ArticleIdArticleSearchQueryBuilder
    extends AbstractArticleQueryBuilder<KeywordArticleSearchCriteria>
    implements IAggregationBuilder {

  @Autowired
  private ArticleIdFieldMapper articleIdFieldMapper;

  @Override
  public SearchQuery buildQuery(KeywordArticleSearchCriteria criteria, Pageable pageable,
      String... indices) {

    final String articleId = criteria.getText();
    final BoolQueryBuilder searchQuery = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
      .apply(QueryBuilders.boolQuery().must(
          QueryBuilders.matchQuery(articleIdFieldMapper.getField().value(), articleId)));

    log.debug("Query = {}", searchQuery);
    aggFilterBuilders.forEach(builder -> builder.addFilter(searchQuery, criteria));
    final NativeSearchQueryBuilder queryBuilder = ArticleQueryUtils.nativeQueryBuilder(
        pageable, indices).withQuery(searchQuery);

    if (criteria.isAggregated()) {
      aggregated(queryBuilder, ArticleField.GA_ID, ArticleField.CRITERIA_CID);
    }

    return queryBuilder.build();
  }
}
