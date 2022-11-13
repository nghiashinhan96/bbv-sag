package com.sagag.services.elasticsearch.query.articles.article;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.AggregationPathMultiLevel;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;

import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AggregationFreeTextQueryBuilder implements IAggregationBuilder {

  @Override
  public void buildAggregation(final NativeSearchQueryBuilder queryBuilder) {
    AggregationPathMultiLevel cvp = AggregationPathMultiLevel.builder()
        .path(ArticleField.CRITERIA_CVP_RAW)
        .build();
    AggregationPathMultiLevel cid = AggregationPathMultiLevel.builder()
        .path(ArticleField.CRITERIA_CID)
        .subPaths(Arrays.asList(cvp)) // Aggregated criteria cvp inside criteria cid
        .build();
    AggregationPathMultiLevel gaId = AggregationPathMultiLevel.builder()
        .path(ArticleField.GA_ID)
        .subPaths(Arrays.asList(cid)) // Aggregated criteria cid inside gaid
        .build();
    AggregationPathMultiLevel supplierRaw = AggregationPathMultiLevel.builder()
        .path(ArticleField.SUPPLIER_RAW)
        .build();
    aggregated(queryBuilder, gaId, supplierRaw);
  }
}
