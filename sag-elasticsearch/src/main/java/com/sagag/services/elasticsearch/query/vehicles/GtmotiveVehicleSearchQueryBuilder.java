package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.elasticsearch.criteria.vehicle.GtmotiveVehicleSearchCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GtmotiveVehicleSearchQueryBuilder
  implements ISearchQueryBuilder<GtmotiveVehicleSearchCriteria> {

  @Override
  public SearchQuery buildQuery(GtmotiveVehicleSearchCriteria criteria, Pageable pageable,
      String... indices) {
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notEmpty(indices, "The given indices must not be empty");
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

    queryBuilder.must(buildMatchUmcQuery(criteria.getUmc()));

    if (!CollectionUtils.isEmpty(criteria.getModelCodes())) {
      queryBuilder.must(buildMatchAnyModelCodeQuery(criteria.getModelCodes()));
    }

    if (!CollectionUtils.isEmpty(criteria.getEngineCodes())) {
      queryBuilder.must(buildMatchAnyEngineCodeQuery(criteria.getEngineCodes()));
    }

    // for transmission codes, we must check not null & null matching
    final List<String> drvWithNullCodes = new ArrayList<>(criteria.getTransmisionCodes());
    drvWithNullCodes.add(SagConstants.NULL_STR);
    if (!CollectionUtils.isEmpty(drvWithNullCodes)) {
      queryBuilder.must(buildMatchAnyTransmisionCodeQuery(drvWithNullCodes));
    }

    log.debug("Query = {}", queryBuilder);

    return new NativeSearchQueryBuilder().withQuery(queryBuilder)
        .withPageable(defaultPageable(pageable)).withIndices(indices).build();
  }

  private static NestedQueryBuilder buildMatchAnyTransmisionCodeQuery(
      final List<String> transmisionCodes) {
    return QueryBuilders.nestedQuery(Index.Vehicle.GT_DRV.path(),
        QueryBuilders.termsQuery(Index.Vehicle.GT_DRV.fullQField(), transmisionCodes), ScoreMode.None);
  }

  private static NestedQueryBuilder buildMatchAnyEngineCodeQuery(final List<String> engineCodes) {
    return QueryBuilders.nestedQuery(Index.Vehicle.GT_ENG.path(),
        QueryBuilders.termsQuery(Index.Vehicle.GT_ENG.fullQField(), engineCodes), ScoreMode.None);
  }

  private static BoolQueryBuilder buildMatchAnyModelCodeQuery(final List<String> modelCodes) {
    final BoolQueryBuilder orModQuery = QueryBuilders.boolQuery();
    orModQuery.should(QueryBuilders.nestedQuery(Index.Vehicle.GT_MOD.path(),
        QueryBuilders.termsQuery(Index.Vehicle.GT_MOD.fullQField(), modelCodes), ScoreMode.None));
    modelCodes.stream().forEach(
        mod -> orModQuery.should(QueryBuilders.nestedQuery(Index.Vehicle.GT_MOD_ALT.path(),
            QueryBuilders.wildcardQuery(Index.Vehicle.GT_MOD_ALT.fullQField(),
                SagConstants.WILDCARD + mod + SagConstants.WILDCARD),
            ScoreMode.None)));
    return orModQuery;
  }

  private static NestedQueryBuilder buildMatchUmcQuery(final String umc) {
    return QueryBuilders.nestedQuery(Index.Vehicle.GT_UMC.path(),
        QueryBuilders.termQuery(Index.Vehicle.GT_UMC.fullQField(), umc), ScoreMode.None);
  }
}
