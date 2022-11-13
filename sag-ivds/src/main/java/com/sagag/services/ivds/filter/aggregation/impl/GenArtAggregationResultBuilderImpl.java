package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.hazelcast.api.GenArtCacheService;
import com.sagag.services.ivds.filter.aggregation.BaseAggregationResultBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GenArtAggregationResultBuilderImpl extends BaseAggregationResultBuilder {

  protected static final String AGGS_NAME = "gen_arts";

  @Autowired
  private GenArtCacheService genArtCacheService;

  @Autowired
  private CountryConfiguration countryConfig;

  @Override
  public void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      Map<String, List<SagBucket>> aggregations) {

    // Get gen_art filter list
    final List<SagBucket> gaidSagBuckets = aggregations.get(ArticleField.GA_ID.name());
    if (CollectionUtils.isEmpty(gaidSagBuckets)) {
      return;
    }

    // Get categories from category cache service
    final List<String> gaids = gaidSagBuckets.stream().map(bucket -> bucket.getKey().toString())
        .collect(Collectors.toList());
    final Map<String, String> cateDescMap = genArtCacheService.searchGenArtByIds(gaids).values()
        .stream().collect(Collectors.toMap(GenArtTxt::getGaid, GenArtTxt::getGatxtdech));

    buildAggregationResult(filters, gaidSagBuckets, genArtFilterItemMapper(cateDescMap), gaids);
  }

  protected void buildAggregationResult(Map<String, List<ArticleFilterItem>> filters,
      List<SagBucket> gaidSagBuckets, Function<SagBucket, ArticleFilterItem> artFilterMapper,
      List<String> gaids) {

    List<ArticleFilterItem> genArtFilterItems =
        articleFilterConverter(artFilterMapper).apply(gaidSagBuckets);
    final String[] languages = countryConfig.languages();
    Stream.of(languages).forEach(language -> {
      Map<String, GenArtTxt> genArtMap =
          genArtCacheService.searchGenArtByIdsAndLanguageCode(gaids, Optional.of(language));
      genArtFilterItems.forEach(filterItem -> {
        String description = StringUtils.EMPTY;
        if (genArtMap.containsKey(filterItem.getId())) {
          description = genArtMap.get(filterItem.getId()).getGatxtdech();
        }
        if (MapUtils.isEmpty(filterItem.getDescriptions())) {
          filterItem.setDescriptions(Maps.newHashMap(language, description));
        } else {
          filterItem.getDescriptions().put(language, description);
        }
      });
    });
    filters.put(AGGS_NAME, genArtFilterItems);
  }

  private Function<SagBucket, ArticleFilterItem> genArtFilterItemMapper(
      final Map<String, String> cateDescMap) {
    return bucket -> {
      final String key = bucket.getKey().toString();
      final String cateDesc = StringUtils.defaultString(cateDescMap.get(key));
      final String genArtTxt = StringUtils.capitalize(cateDesc);
      return ArticleFilterItem.builder().id(key).amountItem(bucket.getDocCount())
          .description(buildDescription(genArtTxt, bucket.getDocCount())).build();
    };
  }

}
