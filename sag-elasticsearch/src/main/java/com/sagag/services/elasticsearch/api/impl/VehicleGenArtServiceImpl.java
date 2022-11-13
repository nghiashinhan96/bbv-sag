package com.sagag.services.elasticsearch.api.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.VehicleGenArtService;
import com.sagag.services.elasticsearch.domain.GenArts;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtDoc;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VehicleGenArtServiceImpl extends AbstractElasticsearchService
    implements VehicleGenArtService {

  @Override
  public String keyAlias() {
    return "vehicle_genart";
  }

  @Override
  public List<VehicleGenArtDoc> getVehicleGenArts(final String vehicleId) {
    log.debug("Starting VehicleGenArtServiceImpl -> getVehicleGenArts({})", vehicleId);
    final BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
    if (!StringUtils.isBlank(vehicleId)) {
      boolBuilder
          .must(QueryBuilders.matchQuery("vehicle_ga.vehid", vehicleId));
    }
    final SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
      .withQuery(boolBuilder)
      .withIndices(index()).build();
    return searchList(searchQuery, VehicleGenArtDoc.class);
  }

  @Override
  public List<String> findGenericArticlesByVehicle(final String vehicleId) {
    return getVehicleGenArts(vehicleId).stream()
        .flatMap(v -> v.getGenArts().stream())
        .map(GenArts::getGaid).collect(Collectors.toList());
  }
}
