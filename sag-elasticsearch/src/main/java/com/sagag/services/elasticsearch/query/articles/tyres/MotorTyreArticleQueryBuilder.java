package com.sagag.services.elasticsearch.query.articles.tyres;

import com.sagag.services.elasticsearch.criteria.article.MotorTyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.AggregationPathMultiLevel;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MotorTyreArticleQueryBuilder extends AbstractArticleQueryBuilder<MotorTyreArticleSearchCriteria> implements
    IAggregationBuilder {

  @Autowired
  private TyreSearchQueryConverter queryConverter;

  @Override
  public SearchQuery buildQuery(MotorTyreArticleSearchCriteria criteria, Pageable pageable,
    String... indices) {

    final BoolQueryBuilder queryBuilder = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
      .apply(commonQueryBuilder(criteria));

    aggFilterBuilders.forEach(builder -> builder.addFilter(queryBuilder, criteria));

    final NativeSearchQueryBuilder searchQueryBuilder = ArticleQueryUtils.nativeQueryBuilder(
        defaultPageable(pageable), indices)
        .withQuery(queryBuilder);

    AggregationPathMultiLevel supplierRaw = new AggregationPathMultiLevel(ArticleField.SUPPLIER_RAW);
    AggregationPathMultiLevel gaId = new AggregationPathMultiLevel(ArticleField.GA_ID);
    AggregationPathMultiLevel criteriaSubCvpRaw =
        new AggregationPathMultiLevel(ArticleField.CRITERIA_CID, ArticleField.CRITERIA_CVP_RAW);

    aggregated(searchQueryBuilder, supplierRaw, gaId, criteriaSubCvpRaw);

    return searchQueryBuilder.build();
  }

  private BoolQueryBuilder commonQueryBuilder(MotorTyreArticleSearchCriteria criteria) {
    // Build the list of genArtIds
    final Set<String> genArtIds = new HashSet<>();
    if (!CollectionUtils.isEmpty(criteria.getCategoryGenArtIds())) {
      genArtIds.addAll(criteria.getCategoryGenArtIds());
    }
    return queryConverter.apply(criteria, genArtIds);
  }
}
