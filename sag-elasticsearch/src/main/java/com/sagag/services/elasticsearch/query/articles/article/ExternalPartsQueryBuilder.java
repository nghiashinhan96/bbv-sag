package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.criteria.ExternalPartsSearchCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.parser.ExternalPartsStringParser;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;
import com.sagag.services.elasticsearch.utils.ElasticsearchConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExternalPartsQueryBuilder
    implements IAggregationBuilder, ISearchQueryBuilder<ExternalPartsSearchCriteria> {

  @Autowired
  private ExternalPartsStringParser extPartParser;

  @Override
  public SearchQuery buildQuery(ExternalPartsSearchCriteria criteria, Pageable pageable,
      String... indices) {
    final String normalizedText = extPartParser.apply(criteria.getText(), null);

    final BoolQueryBuilder mustFilterQuery = QueryBuilders.boolQuery()
        .must(createFilterQueryForPartNumber(normalizedText));
    final BoolQueryBuilder searchQuery = QueryBuilders.boolQuery().must(mustFilterQuery);

    log.debug("Query = {}", searchQuery);

    return ArticleQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(searchQuery).build();
  }

  private QueryBuilder createFilterQueryForPartNumber(String partNr) {
    final Map<String, Float> attributeBoost = new HashMap<>();
    attributeBoost.put(Index.ExternalParts.PRNR.fullQField(), NumberUtils.FLOAT_ONE);
    final QueryBuilder queryBuilder = buildFilterQuery(partNr,
        new String[] { Index.ExternalParts.PRNR.field() }, attributeBoost, true);
    return queryBuilder;
  }

  private static QueryBuilder buildFilterQuery(final String freetext, final String[] searchFields,
      final Map<String, Float> attributesBoost, final boolean splitOnWhitespace) {

    final Map<String, Float> boosts = MapUtils.emptyIfNull(attributesBoost);
    QueryStringQueryBuilder stringQueryBuilder =
        QueryBuilders.queryStringQuery(freetext).defaultOperator(Operator.AND);

    Stream.of(searchFields).forEach(field -> stringQueryBuilder.field(field,
        boosts.getOrDefault(field, ElasticsearchConstants.DEFAULT_BOOST)));

    stringQueryBuilder.lenient(true);
    stringQueryBuilder.splitOnWhitespace(splitOnWhitespace);
    return stringQueryBuilder;
  }
}
