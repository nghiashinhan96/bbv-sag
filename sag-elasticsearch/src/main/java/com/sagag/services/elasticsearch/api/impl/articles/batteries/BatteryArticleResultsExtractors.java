package com.sagag.services.elasticsearch.api.impl.articles.batteries;

import static com.sagag.services.elasticsearch.common.BatteryConstants.AMPERE_HOUR_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.AMPERE_HOUR_MAP_NAME;
import static com.sagag.services.elasticsearch.common.BatteryConstants.HEIGHT_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.HEIGHT_MAP_NAME;
import static com.sagag.services.elasticsearch.common.BatteryConstants.INTERCONNECTION_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.INTERCONNECTION_MAP_NAME;
import static com.sagag.services.elasticsearch.common.BatteryConstants.LENGTH_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.LENGTH_MAP_NAME;
import static com.sagag.services.elasticsearch.common.BatteryConstants.POLE_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.POLE_MAP_NAME;
import static com.sagag.services.elasticsearch.common.BatteryConstants.VOLTAGE_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.VOLTAGE_MAP_NAME;
import static com.sagag.services.elasticsearch.common.BatteryConstants.WIDTH_CID;
import static com.sagag.services.elasticsearch.common.BatteryConstants.WIDTH_MAP_NAME;
import static com.sagag.services.elasticsearch.extractor.ResultsExtractorUtils.extractPageResult;
import static com.sagag.services.elasticsearch.extractor.ResultsExtractorUtils.removeUnnecessaryValue;

import com.sagag.services.elasticsearch.common.BatteryConstants;
import com.sagag.services.elasticsearch.criteria.article.BatteryArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.BatteryArticleResponse;
import com.sagag.services.elasticsearch.dto.SliderChartDto;
import com.sagag.services.elasticsearch.extractor.ArticleFilteringResultsExtractors;
import com.sagag.services.elasticsearch.extractor.ResultsExtractorUtils;
import com.sagag.services.elasticsearch.extractor.SagBucket;

import lombok.experimental.UtilityClass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public final class BatteryArticleResultsExtractors {

  /**
   * Extracts ES result in batteries.
   *
   * @param criteria the search criteria
   * @param pageable the pagination
   * @return the result extractor of {@link BatteryArticleResponse}
   */
  public static ResultsExtractor<BatteryArticleResponse> extractBatteries(
      final BatteryArticleSearchCriteria criteria, final Pageable pageable) {
    return response -> {
      final Page<ArticleDoc> articles =
          extractPageResult(response, ArticleDoc.class, pageable);

      // Build the map of aggregations
      final Map<String, List<Object>> aggregations = new HashMap<>();

      final Set<String> voltageSet = new HashSet<>();
      final Set<String> interconnectionSet = new HashSet<>();
      final Set<String> poleSet = new HashSet<>();

      final Map<String, Integer> ampereHourCount = new LinkedHashMap<>();
      final Set<String> ampereHourValueSet = new HashSet<>();
      final Map<String, Integer> lengthCount = new LinkedHashMap<>();
      final Set<String> lengthValueSet = new HashSet<>();
      final Map<String, Integer> widthCount = new LinkedHashMap<>();
      final Set<String> widthValueSet = new HashSet<>();
      final Map<String, Integer> heightCount = new LinkedHashMap<>();
      final Set<String> heightValueSet = new HashSet<>();

      articles.getContent().stream().flatMap(article -> article.getCriteria().stream())
      .forEach(cr -> {
        if (ResultsExtractorUtils.equalsCriteria(
            String.valueOf(VOLTAGE_CID), cr)) {
          voltageSet.add(cr.getCvp());
        } else if (ResultsExtractorUtils.equalsCriteria(
            String.valueOf(AMPERE_HOUR_CID), cr)) {
              ResultsExtractorUtils.countAndCollectArticlesByCriteria(ampereHourValueSet,
                  ampereHourCount, cr.getCvp());
        } else if (ResultsExtractorUtils.equalsCriteria(
            String.valueOf(LENGTH_CID), cr)) {
              ResultsExtractorUtils.countAndCollectArticlesByCriteria(lengthValueSet,
                     lengthCount, cr.getCvp());
        } else if (ResultsExtractorUtils.equalsCriteria(
            String.valueOf(WIDTH_CID), cr)) {
              ResultsExtractorUtils.countAndCollectArticlesByCriteria(widthValueSet,
                      widthCount, cr.getCvp());
        } else if (ResultsExtractorUtils.equalsCriteria(
            String.valueOf(HEIGHT_CID), cr)) {
              ResultsExtractorUtils.countAndCollectArticlesByCriteria(heightValueSet,
                      heightCount, cr.getCvp());
        } else if (ResultsExtractorUtils.equalsCriteria(
            String.valueOf(INTERCONNECTION_CID), cr)) {
          interconnectionSet.add(cr.getCvp());
        } else if (ResultsExtractorUtils.equalsCriteria(
            String.valueOf(POLE_CID), cr)) {
          poleSet.add(cr.getCvp());
        }
      });

      removeUnnecessaryValue(voltageSet, criteria.getVoltages());
      removeUnnecessaryValue(interconnectionSet, criteria.getInterconnections());
      removeUnnecessaryValue(poleSet, criteria.getTypeOfPoles());

      final List<SliderChartDto> ampereHourSliderChartDtos = ResultsExtractorUtils.convertToSliderChart(
          ampereHourCount, ampereHourValueSet, criteria.getAmpereHours());
      final List<SliderChartDto> lengthSliderChartDtos = ResultsExtractorUtils.convertToSliderChart(
              lengthCount, lengthValueSet, criteria.getLengths());
      final List<SliderChartDto> widthSliderChartDtos = ResultsExtractorUtils.convertToSliderChart(
              widthCount, widthValueSet, criteria.getWidths());
      final List<SliderChartDto> heightSliderChartDtos = ResultsExtractorUtils.convertToSliderChart(
              heightCount, heightValueSet, criteria.getHeights());
      
      aggregations.put(VOLTAGE_MAP_NAME,
          ResultsExtractorUtils.toNumericSortedAggregationList(voltageSet));
      aggregations.put(AMPERE_HOUR_MAP_NAME,
          ResultsExtractorUtils.toCriteriaSortedAggregationList(ampereHourSliderChartDtos));
      aggregations.put(LENGTH_MAP_NAME,
          ResultsExtractorUtils.toCriteriaSortedAggregationList(lengthSliderChartDtos));
      aggregations.put(WIDTH_MAP_NAME,
              ResultsExtractorUtils.toCriteriaSortedAggregationList(widthSliderChartDtos));
      aggregations.put(HEIGHT_MAP_NAME,
              ResultsExtractorUtils.toCriteriaSortedAggregationList(heightSliderChartDtos));
      aggregations.put(INTERCONNECTION_MAP_NAME,
          ResultsExtractorUtils.toNumericSortedAggregationList(interconnectionSet));
      aggregations.put(POLE_MAP_NAME,
          ResultsExtractorUtils.toNumericSortedAggregationList(poleSet));

      return BatteryArticleResponse.builder().aggregations(aggregations).articles(articles).build();
    };
  }

  /**
   * Extracts ES result in batteries with aggregation.
   *
   * @param pageable the pagination
   * @return the result extractor of {@link BatteryArticleResponse}
   */
  public static ResultsExtractor<ArticleFilteringResponse> extractBatteriesWithAgg(
      final Pageable pageable) {
    return response -> {
      ArticleFilteringResponse filteringRes =
          ArticleFilteringResultsExtractors.extract(pageable).extract(response);
      final List<ArticleDoc> articles = filteringRes.getArticles().getContent();
      // Add ampere hour list to aggregations
      Map<String, SagBucket> ampereBuckets = new HashMap<>();
      List<ArticleCriteria> criterias = articles.stream()
          .flatMap(article -> article.getCriteria().stream())
          .filter(filterAmpereHourCid())
          .collect(Collectors.toList());
      criterias.forEach(artCriteria -> ampereBuckets.putIfAbsent(artCriteria.getCvp(),
          SagBucket.builder().key(artCriteria.getCvp())
              .docCount(ResultsExtractorUtils.countCvp(criterias, artCriteria.getCvp())).build()));

      filteringRes.getAggregations().put(BatteryConstants.AMPERE_HOUR_MAP_NAME,
          ampereBuckets.values().stream().collect(Collectors.toList()));
      return filteringRes;
    };
  }

  private static Predicate<ArticleCriteria> filterAmpereHourCid() {
    return artCriteria -> ResultsExtractorUtils.equalsCriteria(
        String.valueOf(AMPERE_HOUR_CID), artCriteria);
  }
}
