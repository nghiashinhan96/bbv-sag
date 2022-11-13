package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.filter.AggregationFilterBuilder;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractArticleQueryBuilder<T> implements ISearchQueryBuilder<T> {

  private static final String[] ART_NUM = { ArticleField.ART_NUMBER.value() };
  private static final String[] GOLDEN_RECORD_ID = { ArticleField.ID_GOLDEN_RECORD.value() };

  @Autowired
  protected List<AggregationFilterBuilder> aggFilterBuilders;

  @Autowired
  private CountryConfiguration countryConfig;

  protected Function<BoolQueryBuilder, BoolQueryBuilder> applyCommonQueryBuilder(
      String[] affNameLocks, boolean isSaleOnBehalf) {
    return ArticleQueryBuilders.applyCommonQueryBuilder(affNameLocks,
      countryConfig.getExcludeCountries(), isSaleOnBehalf);
  }

  protected static NestedQueryBuilder buildCvpQuery(final int cid, final List<String> cvpList) {
    return ArticleQueryUtils.buildCvpListQuery().apply(cid, cvpList);
  }

  protected static NestedQueryBuilder buildCvpRawQuery(final int cid, final List<String> cvpList) {
    return ArticleQueryUtils.buildCvpRawListQuery().apply(cid, cvpList);
  }

  protected static List<String> analyzeCvps(List<String> cvpList) {
    return ArticleQueryUtils.analyzeCvps(cvpList);
  }

  protected static QueryBuilder createFilterQueryForArticleNumber(final String freetext) {
    return ArticleQueryBuilders.filterQuery(Collections.emptyMap(), IndexFieldType.NONE_NESTED,
      ArticleField.REFS, true).apply(freetext, ART_NUM);
  }

  protected QueryBuilder createFilterQueryForArticleId(final String freetext,
      final String[] articleField) {
    return ArticleQueryBuilders
        .filterQuery(Collections.emptyMap(), IndexFieldType.NONE_NESTED, ArticleField.REFS, false)
        .apply(freetext, articleField);
  }

  protected static QueryBuilder createFilterQueryForGoldenRecordId(final String freetext) {
    return ArticleQueryBuilders
        .filterQuery(Collections.emptyMap(), IndexFieldType.NONE_NESTED, ArticleField.REFS, true)
        .apply(freetext, GOLDEN_RECORD_ID);
  }
}
