package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.criteria.article.PartNumberListSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Slf4j
public class PartNumberListArticleQueryBuilder
  extends AbstractArticleQueryBuilder<PartNumberListSearchCriteria> {

  @Override
  public SearchQuery buildQuery(PartNumberListSearchCriteria criteria, Pageable pageable,
      String... indices) {

    final String[] partNumbers = Stream.of(criteria.getPartNumbers()).map(StringUtils::lowerCase)
        .toArray(String[]::new);
    final String supplier = criteria.getSupplier();
    final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
        .must(buildArtNrsOrPartNumbersQueryBuilder(partNumbers))
        .must(QueryBuilders.termQuery(ArticleField.SUPPLIER_RAW.value(), supplier));

    final BoolQueryBuilder queryBuilder = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
        .apply(boolQueryBuilder);
    log.debug("{}", queryBuilder);
    return ArticleQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(queryBuilder)
          .build();
  }

  private static BoolQueryBuilder buildArtNrsOrPartNumbersQueryBuilder(final String[] partNumbers) {
    final BoolQueryBuilder artNrsOrPartNumbersQueryBuilder = QueryBuilders.boolQuery();

    final ArticleField partNumberField = ArticleField.PARTS_NUMBER;
    final BoolQueryBuilder partNrsQueryBuilder =
        QueryBuilders.boolQuery().must(QueryBuilders.termsQuery(partNumberField.value(), partNumbers));
    final NestedQueryBuilder partNumbersQueryBuilder =
        QueryBuilders.nestedQuery(partNumberField.code(), partNrsQueryBuilder, ScoreMode.None);

    artNrsOrPartNumbersQueryBuilder.should(partNumbersQueryBuilder);
    artNrsOrPartNumbersQueryBuilder.should(
        QueryBuilders.termsQuery(ArticleField.ART_NUMBER.value(), partNumbers));

    return artNrsOrPartNumbersQueryBuilder;
  }

}
