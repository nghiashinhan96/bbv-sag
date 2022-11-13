package com.sagag.services.elasticsearch.api.impl.articles.oils;

import com.sagag.services.elasticsearch.common.OilConstants;
import com.sagag.services.elasticsearch.criteria.article.OilArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.OilArticleResponse;
import com.sagag.services.elasticsearch.dto.SliderChartDto;
import com.sagag.services.elasticsearch.extractor.ArticleFilteringResultsExtractors;
import com.sagag.services.elasticsearch.extractor.ResultsExtractorUtils;
import com.sagag.services.elasticsearch.extractor.SagBucket;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public final class OilArticleResultsExtractors {

  public static ResultsExtractor<OilArticleResponse> extract(final OilArticleSearchCriteria criteria, boolean isSortingOilVehicleInDesc) {
    return response -> {

      final Page<ArticleDoc> articles =
          ResultsExtractorUtils.extractPageResult(response, ArticleDoc.class, null);

      final Set<String> vehicleSet = new HashSet<>();
      final Set<String> aggregateSet = new HashSet<>();
      final Set<String> viscositySet = new HashSet<>();
      final Set<String> approvedSet = new HashSet<>();
      final Set<String> specificationSet = new HashSet<>();
      final Set<String> brands = new HashSet<>();
      final Set<String> bottleSizeValueSet = new HashSet<>();
      final Map<String, Integer> bottleSizeCount = new LinkedHashMap<>();

      articles.getContent().stream().forEach(article -> brands.add(article.getSupplier()));

      articles.getContent().stream().flatMap(article -> article.getCriteria().stream())
          .forEach(cr -> {
            if (ResultsExtractorUtils.equalsCriteria(String.valueOf(OilConstants.VEHICLE_CID),
                cr)) {
              vehicleSet.add(cr.getCvp());
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(OilConstants.AGGREGATE_CID), cr)) {
              aggregateSet.add(cr.getCvp());
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(OilConstants.VISCOSITY_CID), cr)) {
              viscositySet.add(cr.getCvp());
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(OilConstants.BOTTLE_SIZE_CID), cr)) {
              ResultsExtractorUtils.countAndCollectArticlesByCriteria(bottleSizeValueSet,
                  bottleSizeCount, cr.getCvp());
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(OilConstants.APPROVED_CID), cr)) {
              approvedSet.add(cr.getCvp());
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(OilConstants.SPECIFICATION_CID), cr)) {
              specificationSet.add(cr.getCvp());
            }
          });

      final Map<String, List<Object>> aggregations = new HashMap<>();
      List<Object> vehicles = isSortingOilVehicleInDesc
          ? ResultsExtractorUtils.toStringSortedDescAggregationList(vehicleSet)
          : ResultsExtractorUtils.toSortedAscAggregationList(vehicleSet);
      final List<SliderChartDto> bottleSizeSliderChartDtos = ResultsExtractorUtils
          .convertToSliderChart(bottleSizeCount, bottleSizeValueSet, criteria.getBottleSizes());
      aggregations.put(OilConstants.VEHICLE_MAP_NAME,
          vehicles);
      aggregations.put(OilConstants.AGGREGATE_MAP_NAME,
          ResultsExtractorUtils.toSortedAscAggregationList(aggregateSet));
      aggregations.put(OilConstants.VISCOSITY_MAP_NAME,
          ResultsExtractorUtils.toSortedAscAggregationList(viscositySet));
      aggregations.put(OilConstants.BOTTLE_SIZE_MAP_NAME,
          ResultsExtractorUtils.toCriteriaSortedAggregationList(bottleSizeSliderChartDtos));
      aggregations.put(OilConstants.APPROVED_MAP_NAME,
          ResultsExtractorUtils.toSortedAscAggregationList(approvedSet));
      aggregations.put(OilConstants.SPECIFICATION_MAP_NAME,
          ResultsExtractorUtils.toSortedAscAggregationList(specificationSet));
      aggregations.put(OilConstants.BRAND_MAP_NAME,
          ResultsExtractorUtils.toSortedAscAggregationList(brands));

      return OilArticleResponse.builder().aggregations(aggregations).articles(articles).build();

    };
  }

  public static ResultsExtractor<ArticleFilteringResponse> extractOilsWithAgg(
      final Pageable pageable) {
    return response -> {
      ArticleFilteringResponse filteringRes =
          ArticleFilteringResultsExtractors.extract(pageable).extract(response);
      final List<ArticleDoc> articles = filteringRes.getArticles().getContent();
      // Add viscosity list to aggregations
      Map<String, SagBucket> viscosityBuckets = new HashMap<>();
      List<ArticleCriteria> viscosityCriterias = articles.stream()
          .flatMap(article -> article.getCriteria().stream()).filter(artCriteria -> StringUtils
              .equals(String.valueOf(OilConstants.VISCOSITY_CID), artCriteria.getCid()))
          .collect(Collectors.toList());
      viscosityCriterias.forEach(artCriteria -> viscosityBuckets.putIfAbsent(artCriteria.getCvp(),
          SagBucket.builder().key(artCriteria.getCvp())
              .docCount(ResultsExtractorUtils.countCvp(viscosityCriterias, artCriteria.getCvp()))
              .build()));

      filteringRes.getAggregations().put(OilConstants.VISCOSITY_MAP_NAME,
          viscosityBuckets.values().stream().collect(Collectors.toList()));

      // Add bottle size list to aggregations
      Map<String, SagBucket> bottleSizeBuckets = new HashMap<>();
      List<ArticleCriteria> bottleSizeCriterias = articles.stream()
          .flatMap(article -> article.getCriteria().stream()).filter(artCriteria -> StringUtils
              .equals(String.valueOf(OilConstants.BOTTLE_SIZE_CID), artCriteria.getCid()))
          .collect(Collectors.toList());
      bottleSizeCriterias.forEach(artCriteria -> bottleSizeBuckets.putIfAbsent(artCriteria.getCvp(),
          SagBucket.builder().key(artCriteria.getCvp())
              .docCount(ResultsExtractorUtils.countCvp(bottleSizeCriterias, artCriteria.getCvp()))
              .build()));

      filteringRes.getAggregations().put(OilConstants.BOTTLE_SIZE_MAP_NAME,
          bottleSizeBuckets.values().stream().collect(Collectors.toList()));
      return filteringRes;
    };
  }
}
