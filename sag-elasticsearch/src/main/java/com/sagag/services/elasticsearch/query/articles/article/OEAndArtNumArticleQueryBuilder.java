package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.config.ArticleIdFieldMapper;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.parser.OeAndArtNumStringParser;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryBuilders;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class OEAndArtNumArticleQueryBuilder
    extends AbstractArticleQueryBuilder<KeywordArticleSearchCriteria>
    implements IAggregationBuilder {

  private static final String[] PART_NUM = { ArticleField.PARTS_NUMBER.value() };

  private static final String[] PARTS_EXT_NUM = { ArticleField.PARTS_EXT_NUMBER.value() };

  @Autowired
  private ArticleIdFieldMapper articleIdFieldMapper;

  @Autowired
  private AggregationOEAndArtNumQueryBuilder aggregationOEAndArtNumQueryBuilder;

  @Autowired
  private OeAndArtNumStringParser strParser;

  @Autowired
  private AggregationArticleListQueryBuilder articleListQuery;

  @Override
  public SearchQuery buildQuery(KeywordArticleSearchCriteria criteria, Pageable pageable,
      String... indices) {
    final BoolQueryBuilder searchQuery = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
        .apply(oeAndArtNumQueryBuilder(criteria.getText(), criteria.isUsePartsExt(), criteria.isDirectMatch()));

    aggFilterBuilders.forEach(builder -> builder.addFilter(searchQuery, criteria));

    log.debug("indices: {} \nQuery = {}", indices, searchQuery);

    final NativeSearchQueryBuilder queryBuilder = ArticleQueryUtils.nativeQueryBuilder(
        pageable, indices).withQuery(searchQuery);

    if (!criteria.isAggregated()) {
      return queryBuilder.build();
    }
    if (!criteria.isUseMultipleAggregation()) {
      articleListQuery.buildAggregation(queryBuilder);
      return queryBuilder.build();
    }
    if (!criteria.isNeedSubAggregated()) {
      aggregated(queryBuilder, ArticleField.GA_ID);
      return queryBuilder.build();
    }
    aggregationOEAndArtNumQueryBuilder.buildAggregation(queryBuilder);
    return queryBuilder.build();
  }

  private BoolQueryBuilder oeAndArtNumQueryBuilder(String searchText, boolean usePartsExt, boolean directMatch) {
    String modifiedSearchText = strParser.apply(searchText, null);
    List<QueryBuilder> queries = new ArrayList<>();
    if (directMatch) {
      queries.add(createFilterQueryForArticleNumber(modifiedSearchText));
      queries.add(createFilterQueryForGoldenRecordId(modifiedSearchText));
      queries.add(createFilterQueryForArticleId(modifiedSearchText,
          new String[]{articleIdFieldMapper.getField().value()}));
    } else { // By pass this step if not direct match
      queries.add(createFilterQueryForPartRefNum(modifiedSearchText, usePartsExt));
    }
    final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    queries.forEach(boolQuery::should);
    return QueryBuilders.boolQuery().must(boolQuery);
  }

   private static QueryBuilder createFilterQueryForPartRefNum(final String freetext,
            boolean usePartsExt) {
          final ArticleField partField = usePartsExt ? ArticleField.PARTS_EXT : ArticleField.PARTS;
          final String[] partNumValue = usePartsExt ? PARTS_EXT_NUM : PART_NUM;
          return ArticleQueryBuilders.filterQuery(Collections.emptyMap(), IndexFieldType.NESTED,
              partField, true).apply(freetext, partNumValue);
  }
}
