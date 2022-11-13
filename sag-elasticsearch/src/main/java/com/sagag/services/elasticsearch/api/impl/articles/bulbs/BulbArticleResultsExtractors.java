package com.sagag.services.elasticsearch.api.impl.articles.bulbs;

import com.sagag.services.elasticsearch.common.BulbConstants;
import com.sagag.services.elasticsearch.criteria.article.BulbArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.BulbArticleResponse;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.ArticleFilteringResultsExtractors;
import com.sagag.services.elasticsearch.extractor.ResultsExtractorUtils;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

@UtilityClass
public final class BulbArticleResultsExtractors {

  public static ResultsExtractor<BulbArticleResponse> extract(BulbArticleSearchCriteria criteria,
      final Pageable pageable) {
    return response -> {
      final Page<ArticleDoc> articles =
          ResultsExtractorUtils.extractPageResult(response, ArticleDoc.class, pageable);

      // Get custom aggregation list from search result
      final Map<String, List<Object>> curAggs = extractAggregations(response.getAggregations());
      if (MapUtils.isEmpty(curAggs)) {
        return BulbArticleResponse.builder().articles(articles).build();
      }

      // Build the map of aggregations
      final Map<String, List<Object>> aggregations = new HashMap<>();

      final Set<String> voltageSet = new HashSet<>();
      final Set<String> wattSet = new HashSet<>();
      final Set<String> codeSet = new HashSet<>();

      articles.getContent().stream().flatMap(article -> article.getCriteria().stream())
          .forEach(cr -> {
            if (ResultsExtractorUtils.equalsCriteria(String.valueOf(BulbConstants.VOLTAGE_CID),
                cr)) {
              voltageSet.add(cr.getCvp());
            } else if (ResultsExtractorUtils.equalsCriteria(String.valueOf(BulbConstants.WATT_CID),
                cr)) {
              wattSet.add(cr.getCvp());
            } else if (ResultsExtractorUtils.equalsCriteria(String.valueOf(BulbConstants.CODE_CID),
                cr)) {
              codeSet.add(cr.getCvp());
            }
          });

      ResultsExtractorUtils.removeUnnecessaryValue(voltageSet, criteria.getVoltages());
      ResultsExtractorUtils.removeUnnecessaryValue(wattSet, criteria.getWatts());
      ResultsExtractorUtils.removeUnnecessaryValue(codeSet, criteria.getCodes());

      aggregations.put(BulbConstants.SUPPLIER_MAP_NAME, getSuppliers(curAggs));

      aggregations.put(BulbConstants.VOLTAGE_MAP_NAME,
          ResultsExtractorUtils.toNumericSortedAggregationList(voltageSet));
      aggregations.put(BulbConstants.WATT_MAP_NAME,
          ResultsExtractorUtils.toSortedIteAggregationListByFirstNumeric(wattSet));
      aggregations.put(BulbConstants.CODE_MAP_NAME,
          ResultsExtractorUtils.toSortedAscAggregationList(codeSet));

      return BulbArticleResponse.builder().aggregations(aggregations).articles(articles).build();
    };
  }

  private static Map<String, List<Object>> extractAggregations(final Aggregations aggregations) {
    if (Objects.isNull(aggregations)) {
      return Collections.emptyMap();
    }
    return ResultsExtractorUtils.toAggregationBucketsMap(aggregations.asMap());
  }

  private static List<Object> getSuppliers(Map<String, List<Object>> curAggs) {
    return curAggs.get(ArticleField.SUPPLIER_RAW.name()).stream().sorted()
        .collect(Collectors.toList());
  }

  public static ResultsExtractor<ArticleFilteringResponse> extractBulbsWithAgg(
      final Pageable pageable) {
    return response -> {
      ArticleFilteringResponse filteringRes =
          ArticleFilteringResultsExtractors.extract(pageable).extract(response);
      final List<ArticleDoc> articles = filteringRes.getArticles().getContent();
      // Add volt hour list to aggregations
      Map<String, SagBucket> voltBuckets = new HashMap<>();
      List<ArticleCriteria> voltCriterias = articles.stream()
          .flatMap(article -> article.getCriteria().stream()).filter(artCriteria -> StringUtils
              .equals(String.valueOf(BulbConstants.VOLTAGE_CID), artCriteria.getCid()))
          .collect(Collectors.toList());
      voltCriterias.forEach(artCriteria -> voltBuckets.putIfAbsent(artCriteria.getCvp(),
          SagBucket.builder().key(artCriteria.getCvp())
              .docCount(ResultsExtractorUtils.countCvp(voltCriterias, artCriteria.getCvp())).build()));

      filteringRes.getAggregations().put(BulbConstants.VOLTAGE_MAP_NAME,
          voltBuckets.values().stream().collect(Collectors.toList()));

      // Add watt list to aggregations
      Map<String, SagBucket> wattBuckets = new HashMap<>();
      List<ArticleCriteria> wattCriterias = articles.stream()
          .flatMap(article -> article.getCriteria().stream()).filter(artCriteria -> StringUtils
              .equals(String.valueOf(BulbConstants.WATT_CID), artCriteria.getCid()))
          .collect(Collectors.toList());
      wattCriterias.forEach(artCriteria -> wattBuckets.putIfAbsent(artCriteria.getCvp(),
          SagBucket.builder().key(artCriteria.getCvp())
              .docCount(ResultsExtractorUtils.countCvp(wattCriterias, artCriteria.getCvp())).build()));

      filteringRes.getAggregations().put(BulbConstants.WATT_MAP_NAME,
          wattBuckets.values().stream().collect(Collectors.toList()));

      // Add code and bild list to aggregations
      Map<String, SagBucket> codeBuckets = new HashMap<>();
      List<ArticleCriteria> codeCriterias = articles.stream()
          .flatMap(article -> article.getCriteria().stream()).filter(artCriteria -> StringUtils
              .equals(String.valueOf(BulbConstants.CODE_CID), artCriteria.getCid()))
          .collect(Collectors.toList());
      codeCriterias.forEach(artCriteria -> codeBuckets.putIfAbsent(artCriteria.getCvp(),
          SagBucket.builder().key(artCriteria.getCvp())
              .docCount(ResultsExtractorUtils.countCvp(codeCriterias, artCriteria.getCvp())).build()));

      filteringRes.getAggregations().put(BulbConstants.CODE_MAP_NAME,
          codeBuckets.values().stream().collect(Collectors.toList()));
      return filteringRes;
    };
  }

}
