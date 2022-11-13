package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.enums.Index.WssArticleGroup;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.utils.ElasticsearchConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.MapUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Common article group query builders.
 *
 * <pre>
 * Propose some common query builders for article group searching
 * </pre>
 */
@UtilityClass
public class WssArticleGroupQueryBuilders {
  public static BiFunction<String, String[], QueryBuilder> filterQuery(
      final Map<String, Float> attributesBoost, final IndexFieldType type,
      final WssArticleGroup pathField, final boolean splitOnWhitespace) {
    return (freetext, searchFields) -> {
      final Map<String, Float> boosts = MapUtils.emptyIfNull(attributesBoost);
      QueryStringQueryBuilder stringQueryBuilder =
          QueryBuilders.queryStringQuery(freetext).defaultOperator(Operator.AND);
      Stream.of(searchFields).forEach(field -> stringQueryBuilder.field(field,
          boosts.getOrDefault(field, ElasticsearchConstants.DEFAULT_BOOST)));
      stringQueryBuilder.lenient(true);
      stringQueryBuilder.splitOnWhitespace(splitOnWhitespace);
      if (type != IndexFieldType.NESTED) {
        return stringQueryBuilder;
      }
      return QueryBuilders.nestedQuery(pathField.field(), stringQueryBuilder, ScoreMode.None);
    };
  }

  public static void buildLockArticleGroupByFieldQuery(final BoolQueryBuilder queryBuilder,
      final WssArticleGroup field, String keyWord) {
    BoolQueryBuilder locksFrontEndQueryBuilder = QueryBuilders.boolQuery();
    locksFrontEndQueryBuilder.filter(QueryBuilders.commonTermsQuery(field.fullQField(), keyWord));
    queryBuilder.mustNot(locksFrontEndQueryBuilder);
  }
}
