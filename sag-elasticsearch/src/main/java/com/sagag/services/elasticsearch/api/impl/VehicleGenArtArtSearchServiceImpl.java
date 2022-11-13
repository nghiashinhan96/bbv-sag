package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.VehicleGenArtArtSearchService;
import com.sagag.services.elasticsearch.config.ArticleIdFieldMapper;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArt;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtArtDoc;
import com.sagag.services.elasticsearch.enums.Index;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VehicleGenArtArtSearchServiceImpl extends AbstractElasticsearchService
  implements VehicleGenArtArtSearchService {

  @Autowired
  private ArticleIdFieldMapper articleIdFieldMapper;

  @Override
  public String keyAlias() {
    return "vehicle_genart_art";
  }

  @Override
  public List<VehicleGenArtArtDoc> searchFitmentsByIds(List<String> fitmentIds) {
    if (CollectionUtils.isEmpty(fitmentIds)) {
      return Collections.emptyList();
    }
    final NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
      .withIndices(index())
      .withPageable(PageUtils.defaultPageable(CollectionUtils.size(fitmentIds)))
      .withQuery(QueryBuilders.termsQuery(Index.Fitment.ID.field(), fitmentIds));

    return searchList(queryBuilder.build(), VehicleGenArtArtDoc.class)
      .stream().map(converter()).collect(Collectors.toList());
  }

  private static Function<VehicleGenArtArtDoc, VehicleGenArtArtDoc> converter() {
    return articleDoc -> {
      articleDoc.getVgas().add(SagBeanUtils.map(articleDoc, VehicleGenArt.class));
      return articleDoc;
    };
  }

  @Override
  public List<VehicleGenArtArtDoc> searchFitments(final String vehId,
      final List<String> gaIds) {
    if (StringUtils.isBlank(vehId) || CollectionUtils.isEmpty(gaIds)) {
      return Collections.emptyList();
    }
    return searchFitmentsByIds(gaIds.stream().map(gaId -> vehId + gaId)
        .collect(Collectors.toList()));
  }

  @Override
  public List<VehicleGenArtArtDoc> searchFitmentsByVehIdAndArticleIds(String vehId,
    List<String> articleIds) {
    if (StringUtils.isEmpty(vehId) || SagConstants.KEY_NO_VEHICLE.equals(vehId)
      || CollectionUtils.isEmpty(articleIds)) {
      return Collections.emptyList();
    }
    final BoolQueryBuilder query = QueryBuilders.boolQuery()
      .must(QueryBuilders.termQuery(Index.Fitment.VEHID.field(), vehId))
      .must(QueryBuilders.nestedQuery(articleIdFieldMapper.getArtIdFitment().path(),
        QueryBuilders.termsQuery(articleIdFieldMapper.getArtIdFitment().field(), articleIds),
        ScoreMode.None));

    log.debug("Query = {}", query);

    final NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
      .withIndices(index())
      .withPageable(PageUtils.defaultPageable(articleIds.size()))
      .withQuery(query);
    return searchList(queryBuilder.build(), VehicleGenArtArtDoc.class);
  }
}
