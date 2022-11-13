package com.sagag.services.elasticsearch.query.unitrees.unitree.freetext;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.elasticsearch.criteria.unitree.KeywordUnitreeSearchCriteria;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.enums.UnitreeField;
import com.sagag.services.elasticsearch.parser.UnitreeFreetextStringParser;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.unitrees.UnitreeQueryBuilders;
import com.sagag.services.elasticsearch.query.unitrees.UnitreeQueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.assertj.core.util.Arrays;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@Slf4j
public class FreetextUnitreeQueryBuilder
    implements ISearchQueryBuilder<KeywordUnitreeSearchCriteria> {

  @Autowired
  private UnitreeFreetextStringParser freetextStringParser;

  @Override
  public SearchQuery buildQuery(KeywordUnitreeSearchCriteria criteria, Pageable pageable,
      String... indices) {

    final BoolQueryBuilder searchQuery = freetextBoolQueryBuilder(criteria);

    final NativeSearchQueryBuilder queryBuilder =
        UnitreeQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(searchQuery);
    return queryBuilder.build();
  }

  protected BoolQueryBuilder freetextBoolQueryBuilder(final KeywordUnitreeSearchCriteria criteria) {

    QueryBuilder queryBuilder =
        createFilterQueryForNodeName(criteria.getText(), criteria.isPerfectMatched());

    final BoolQueryBuilder shouldQueryBuilder = QueryBuilders.boolQuery();
    shouldQueryBuilder.should(queryBuilder);
    return QueryBuilders.boolQuery().must(shouldQueryBuilder);
  }

  private QueryBuilder createFilterQueryForNodeName(final String freetext,
      boolean isPerfectMatched) {
    String normalizedText = freetextStringParser.apply(freetext, new Object[] { isPerfectMatched });
    log.debug("Searching unitrees by free text = {}", normalizedText);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(SagConstants.WILDCARD).append(normalizedText)
        .append(SagConstants.WILDCARD);

    return buildFilteredQuery(stringBuilder.toString(),
        Arrays.array(UnitreeField.NODE_NAME.fullQField(), UnitreeField.NODE_KEYWORDS.fullQField()),
        Collections.emptyMap(), IndexFieldType.NESTED, UnitreeField.NODES);
  }

  private static QueryBuilder buildFilteredQuery(final String freetext, final String[] fields,
      final Map<String, Float> attrBoost, final IndexFieldType fieldType,
      final UnitreeField artField) {
    return UnitreeQueryBuilders.filterQuery(attrBoost, fieldType, artField, true).apply(freetext,
        fields);
  }

}
