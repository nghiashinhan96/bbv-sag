package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.utils.ElasticsearchConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Common article query builders.
 *
 * <pre> Propose some common query builders for article searching </pre>
 */
@UtilityClass
public class ArticleQueryBuilders {

  /**
   * Adds some common article search query into search query builder.
   * 
   * @param affNameLocks
   * @param countryExcl
   * @param isSaleOnBehalf
   *
   */
  public Function<BoolQueryBuilder, BoolQueryBuilder> applyCommonQueryBuilder(
      final String[] affNameLocks, String[] countryExcl, boolean isSaleOnBehalf) {
    return queryBuilder -> {
      applyCommonArticleFilters(queryBuilder, affNameLocks, countryExcl, isSaleOnBehalf);
      return queryBuilder;
    };
  }

  /**
   * Adds some common article search query into search query builder.
   *
   * @param queryBuilder the query builder is appended
   * @param affNameLocks
   * @param countryExcl
   * @param isSaleOnBehalf
   */
  private static void applyCommonArticleFilters(BoolQueryBuilder queryBuilder,
      String[] affNameLocks, String[] countryExcl, boolean isSaleOnBehalf) {
    // #1788: CH-UMAR: Use the ES based locks for all versions
    locksQueryBuilder(queryBuilder, affNameLocks);
    // #5316
    locksCountryExclQueryBuilder(queryBuilder, countryExcl);
    // #4274
    if (isSaleOnBehalf) {
      locksVkQueryBuilder(queryBuilder, affNameLocks);
    } else {
      locksKuQueryBuilder(queryBuilder, affNameLocks);
    }
    // #7620
    pseudoArticleQueryBuilder(queryBuilder);
  }

  private static void pseudoArticleQueryBuilder(BoolQueryBuilder queryBuilder) {
    final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    boolQuery.must(QueryBuilders.termsQuery(ArticleField.BOM.value(), Boolean.FALSE));
    boolQuery.must(QueryBuilders.termsQuery(ArticleField.PSEUDO.value(), Boolean.TRUE));
    queryBuilder.mustNot(boolQuery);
  }

  private static void locksQueryBuilder(BoolQueryBuilder queryBuilder, String[] affNameLocks) {
    addLocksQuery(queryBuilder, ArticleQueryBuilders::wildcardLocksQueryBuilder, affNameLocks);
  }

  private static void locksVkQueryBuilder(BoolQueryBuilder queryBuilder, String[] affNameLocks) {
    addLocksQuery(queryBuilder, ArticleQueryBuilders::wildcardLocksVkQueryBuilder,
      affNameLocks);
  }

  private static void locksKuQueryBuilder(BoolQueryBuilder queryBuilder, String[] affNameLocks) {
    addLocksQuery(queryBuilder, ArticleQueryBuilders::wildcardLocksKuQueryBuilder, affNameLocks);
  }

  private static void locksCountryExclQueryBuilder(BoolQueryBuilder queryBuilder,
      String[] countryExcl) {
    addLocksQuery(queryBuilder, ArticleQueryBuilders::wildcardCountryExclQueryBuilder, countryExcl);
  }

  private static void addLocksQuery(BoolQueryBuilder queryBuilder,
      Function<String, WildcardQueryBuilder> lockQueryConverter, String... affNameLocks) {
    if (ArrayUtils.isEmpty(affNameLocks)) {
      return;
    }
    if (ArrayUtils.getLength(affNameLocks) == 1) {
      queryBuilder.mustNot(lockQueryConverter.apply(affNameLocks[0]));
      return;
    }
    BoolQueryBuilder locksFrontEndQueryBuilder = QueryBuilders.boolQuery();
    Arrays.stream(affNameLocks).forEach(
        esAffName -> locksFrontEndQueryBuilder.filter(lockQueryConverter.apply(esAffName)));
    queryBuilder.mustNot(locksFrontEndQueryBuilder);
  }

  private static WildcardQueryBuilder wildcardLocksQueryBuilder(String esAffiliateShortName) {
    return QueryBuilders.wildcardQuery(ArticleField.LOCKS.value(),
        addWildcard(esAffiliateShortName));
  }

  private static WildcardQueryBuilder wildcardLocksVkQueryBuilder(String esAffiliateShortName) {
    return QueryBuilders.wildcardQuery(ArticleField.LOCKS_VK.value(),
        addWildcard(esAffiliateShortName));
  }

  private static WildcardQueryBuilder wildcardLocksKuQueryBuilder(String esAffiliateShortName) {
    return QueryBuilders.wildcardQuery(ArticleField.LOCKS_KU.value(),
        addWildcard(esAffiliateShortName));
  }

  private static WildcardQueryBuilder wildcardCountryExclQueryBuilder(String countryExcl) {
    return QueryBuilders.wildcardQuery(ArticleField.COUNTRY_EXCL.value(), addWildcard(countryExcl));
  }

  private static String addWildcard(String value) {
    return ElasticsearchConstants.WILDCARD + value + ElasticsearchConstants.WILDCARD;
  }

  public static BiFunction<String, String[], QueryBuilder> filterQuery(
      final Map<String, Float> attributesBoost, final IndexFieldType type,
      final ArticleField pathField, final boolean splitOnWhitespace) {
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
      return QueryBuilders.nestedQuery(pathField.value(), stringQueryBuilder, ScoreMode.None);
    };
  }
}
