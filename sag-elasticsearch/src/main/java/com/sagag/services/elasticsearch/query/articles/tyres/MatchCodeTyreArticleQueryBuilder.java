package com.sagag.services.elasticsearch.query.articles.tyres;

import com.sagag.services.elasticsearch.criteria.article.MatchCodeArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.ArticlePartType;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryBuilders;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Slf4j
public class MatchCodeTyreArticleQueryBuilder extends
    AbstractArticleQueryBuilder<MatchCodeArticleSearchCriteria> implements IAggregationBuilder {

  @Override
  public SearchQuery buildQuery(MatchCodeArticleSearchCriteria criteria, Pageable pageable,
      String... indices) {
    final BoolQueryBuilder queryBuilder = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
        .apply(commonQueryBuilder(criteria.getMatchCode()));
    log.debug("Query = {}", queryBuilder);
    aggFilterBuilders.forEach(builder -> builder.addFilter(queryBuilder, criteria));

    final NativeSearchQueryBuilder searchQueryBuilder = ArticleQueryUtils
        .nativeQueryBuilder(defaultPageable(pageable), indices).withQuery(queryBuilder);

    if (criteria.isAggregated()) {
      aggregated(searchQueryBuilder, ArticleField.SUPPLIER_RAW, ArticleField.GA_ID);
    }

    return searchQueryBuilder.build();
  }

  private BoolQueryBuilder commonQueryBuilder(String matchCode) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    Optional.of(matchCode).map(QueryUtils::replaceEsSlashCharacter).map(StringUtils::lowerCase)
    .ifPresent(mCode -> {
      QueryBuilder artnrDisplayQuery =
          ArticleQueryBuilders.filterQuery(Collections.emptyMap(),
            IndexFieldType.NONE_NESTED, null, true)
              .apply(mCode, ArrayUtils.toArray(ArticleField.ARTNR_DISPLAY.value()));
      queryBuilder.should(artnrDisplayQuery);

      Stream.of(ArticlePartType.MATCH_CODE, ArticlePartType.PCC)
          .map(pType -> buildPartNumberByPartTypeQueryBuilder(pType.name(), mCode))
          .forEach(queryBuilder::should);
    });

    return queryBuilder;
  }

  private static NestedQueryBuilder buildPartNumberByPartTypeQueryBuilder(String partType,
      String partNr) {
    QueryBuilder partTypeQuery =
        ArticleQueryBuilders.filterQuery(Collections.emptyMap(),
          IndexFieldType.NONE_NESTED, null, true)
            .apply(partType, ArrayUtils.toArray(ArticleField.PART_PTYPE.value()));

    QueryBuilder partNumberQuery =
        ArticleQueryBuilders.filterQuery(Collections.emptyMap(),
          IndexFieldType.NONE_NESTED, null, true)
            .apply(partNr, ArrayUtils.toArray(ArticleField.PARTS_NUMBER.value()));

    final BoolQueryBuilder boolQueryBuilder =
        QueryBuilders.boolQuery().must(partTypeQuery).must(partNumberQuery);
    return QueryBuilders.nestedQuery(ArticleField.PARTS.value(), boolQueryBuilder, ScoreMode.None);
  }
}
