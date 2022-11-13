package com.sagag.services.elasticsearch.query.articles.article;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;

import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class AggregationArticleListQueryBuilder implements IAggregationBuilder {

  @Override
  public void buildAggregation(final NativeSearchQueryBuilder queryBuilder) {
    aggregated(queryBuilder, ArticleField.GA_ID, ArticleField.SUPPLIER_RAW);
  }
}
