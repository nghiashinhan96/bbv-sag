package com.sagag.services.ivds.filter.aggregation.impl;

import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.hazelcast.api.FormatGaCacheService;
import com.sagag.services.hazelcast.api.GenArtCacheService;
import com.sagag.services.ivds.filter.aggregation.SubAggregationResultBuilder;
import com.sagag.services.ivds.filter.aggregation.SubAggregationResultUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GenArtAndSubAggregationResultBuilderImpl extends GenArtAggregationResultBuilderImpl {

  @Autowired
  private GenArtCacheService genArtCacheService;

  @Autowired
  @Qualifier("criteriaAggregationResultBuilderImpl")
  private SubAggregationResultBuilder criteriaAggregationResultBuilder;

  @Autowired
  private FormatGaCacheService gaCacheService;

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

    buildAggregationResult(filters, gaidSagBuckets, getSubArtFilterItemMapper(cateDescMap), gaids);
  }

  private Function<SagBucket, ArticleFilterItem> getSubArtFilterItemMapper(
      final Map<String, String> cateDescMap) {
    return bucket -> {
      final String key = bucket.getKey().toString();
      final String cateDesc = StringUtils.defaultString(cateDescMap.get(key));
      final String genArtTxt = StringUtils.capitalize(cateDesc);

      final ArticleFilterItem subArticleFilterItem = new ArticleFilterItem();
      subArticleFilterItem.setId(key);
      subArticleFilterItem.setAmountItem(bucket.getDocCount());
      subArticleFilterItem.setDescription(buildDescription(genArtTxt, bucket.getDocCount()));
      subArticleFilterItem.setUuid(UUID.randomUUID().toString());
      subArticleFilterItem.setType("category");
      subArticleFilterItem.setSelected(false);

      subArticleFilterItem.setSubFilters(buildSubArticleFilterItems(bucket, key));
      final List<ArticleFilterItem> children = SubAggregationResultUtils.addShowMoreItem(
          subArticleFilterItem.getSubFilters().get(CriteriaAggregationResultBuilderImpl.AGGS_NAME));
      subArticleFilterItem.setChildren(ListUtils.emptyIfNull(children));
      subArticleFilterItem.setHasChildren(CollectionUtils.isNotEmpty(children));
      return subArticleFilterItem;
    };
  }

  private Map<String, List<ArticleFilterItem>> buildSubArticleFilterItems(SagBucket bucket,
      String gaId) {
    final Map<String, List<ArticleFilterItem>> articleFilterMap = new HashMap<>();
    ListUtils.emptyIfNull(bucket.getSubBuckets()).stream()
    .forEach(addSubAggResultConsumer(articleFilterMap));

    sortCriteriaFilter(articleFilterMap.get(CriteriaAggregationResultBuilderImpl.AGGS_NAME), gaId);
    return articleFilterMap;
  }

  private Consumer<Map<String, List<SagBucket>>> addSubAggResultConsumer(
      final Map<String, List<ArticleFilterItem>> articleFilterMap) {
    return subBuk -> criteriaAggregationResultBuilder.buildAggregationResult(articleFilterMap,
        subBuk);
  }

  private void sortCriteriaFilter(final List<ArticleFilterItem> filterList, String gaId) {
    if (CollectionUtils.isEmpty(filterList)) {
      return;
    }
    Map<String, Integer> ordersMap =
        buildCriteriaOrdersMap(gaCacheService.searchFormatGaByGaIds(Arrays.asList(gaId)), gaId);
    filterList.sort(orderCriteriaComparator(ordersMap)
        .thenComparing(descriptionCriteriaCompartor()));
  }

  private Map<String, Integer> buildCriteriaOrdersMap(
      Map<String, FormatGaDoc> formatMap, String gaId) {
    return Optional.ofNullable(formatMap)
        .filter(Objects::nonNull)
        .map(map -> map.get(gaId))
        .filter(Objects::nonNull)
        .map(FormatGaDoc::getElements)
        .filter(CollectionUtils::isNotEmpty)
        .map(elements -> elements.stream()
            .collect(Collectors.toMap(element -> String.valueOf(element.getCid()),
                element -> Integer.parseInt(element.getOrder()))))
        .orElseGet(HashMap::new);
  }
}
