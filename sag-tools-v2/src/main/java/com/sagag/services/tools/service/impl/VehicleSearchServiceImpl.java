package com.sagag.services.tools.service.impl;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.elasticsearch.VehicleDoc;
import com.sagag.services.tools.service.VehicleField;
import com.sagag.services.tools.service.VehicleIndex;
import com.sagag.services.tools.service.VehicleSearchService;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@OracleProfile
public class VehicleSearchServiceImpl implements VehicleSearchService {

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  @Override
  public Optional<VehicleDoc> searchVehicleByIdSag(final String idSag) {
    log.debug("Search vehicle by id sag = {}", idSag);
    if (StringUtils.isBlank(idSag)) {
      return Optional.empty();
    }
    final List<VehicleDoc> vehicles =
      elasticsearchTemplate.queryForList(buildVehicleSearchQueryByIdSag(idSag), VehicleDoc.class);
    if (CollectionUtils.isEmpty(vehicles)) {
      return Optional.empty();
    }
    return vehicles.stream().findFirst();
  }

  private static NativeSearchQuery buildVehicleSearchQueryByIdSag(final String idSag) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.must(buildNestedIdSagVehicleQueryBuilder(idSag));
    log.debug("ES query = {}", queryBuilder.toString());
    return new NativeSearchQueryBuilder().withQuery(queryBuilder)
      .withIndices(VehicleIndex.VEHICLES_DE.getIndexName()).build();
  }

  private static NestedQueryBuilder buildNestedIdSagVehicleQueryBuilder(final String idSag) {
    final BoolQueryBuilder idSagCodeQueryBuilder = QueryBuilders.boolQuery()
      .must(QueryBuilders.termQuery(VehicleField.VEH_CODE_TYPE.getPath(), StringUtils.lowerCase(ToolConstants.SAG)))
      .must(QueryBuilders.termQuery(VehicleField.VEH_CODE_ATTR.getPath(),
          StringUtils.lowerCase(ToolConstants.VEHICLE_CODE_ID_SAG_TYPE)))
      .must(QueryBuilders.termQuery(VehicleField.VEH_CODE_VALUE.getPath(), idSag));
    return QueryBuilders.nestedQuery(VehicleField.CODES.getField(), idSagCodeQueryBuilder, ScoreMode.None);
  }

}
