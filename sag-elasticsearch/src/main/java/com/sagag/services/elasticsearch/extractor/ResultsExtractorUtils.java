package com.sagag.services.elasticsearch.extractor;

import com.google.common.primitives.Doubles;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.elasticsearch.domain.article.ArticleCriteria;
import com.sagag.services.elasticsearch.dto.SliderChartDto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.metrics.scripted.InternalScriptedMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Search result extractor utility class.
 */
@UtilityClass
@Slf4j
public final class ResultsExtractorUtils {

  /**
   * Returns the page results from the search response with class type.
   *
   *
   * @param response the search response
   * @param clazz the class entity to map the fields.
   * @param pageable the page request to elasticsearch
   * @return the page results
   */
  public static <T> Page<T> extractPageResult(final SearchResponse response, final Class<T> clazz,
      final Pageable pageable) {
    if (response == null) {
      return Page.empty();
    }
    log.debug("The search ES query took: {} ms", response.getTookInMillis());
    return new DefaultResultMapper().mapResults(response, clazz, pageable);
  }

  /**
   * Returns a list of buckets from the aggregation.
   *
   *
   * @param aggregation the aggregation terms.
   * @return a list of buckets found.
   */
  public static List<Object> toAggregationBuckets(final Aggregation aggregation) {
    if (aggregation instanceof InternalScriptedMetric) {
      InternalScriptedMetric script = (InternalScriptedMetric) aggregation;
      return extractScriptResult(script.aggregation());
    }
    if (!TypeUtils.isInstance(aggregation, MultiBucketsAggregation.class)) {
      return Collections.emptyList();
    }
    final MultiBucketsAggregation multi = (MultiBucketsAggregation) aggregation;
    return multi.getBuckets().stream().map(Bucket::getKey).collect(Collectors.toList());
  }

  /**
   * Why @SuppressWarnings("unchecked"):
   * <br>
   * The result is get from ScriptedMetric written by "painless scripting language"
   * there for they don't have the type of object return so we have to
   * cast it to it's true form
   *
   * @param aggregation
   * @return List of object
   */
  @SuppressWarnings("unchecked")
  private List<Object> extractScriptResult(Object aggregation) {

    List<Object> listArrayResult = (List<Object>) ((List<Object>)aggregation).stream()
        .map(HashMap.class::cast)
        .map(HashMap::values)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    return (List<Object>) listArrayResult.stream()
        .map(Collection.class::cast)
        .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toList());
  }

  /**
   * Returns the buckets map with terms keys.
   *
   *
   * @param aggregations the aggregations map.
   * @return the buckets map on terms keys.
   */
  public static Map<String, List<Object>> toAggregationBucketsMap(
      final Map<String, Aggregation> aggregations) {
    return aggregations.entrySet().parallelStream().collect(Collectors
        .toMap(Entry<String, Aggregation>::getKey, e -> toAggregationBuckets(e.getValue())));
  }

  /**
   * Returns the buckets map with terms keys by aggregations.
   *
   * @param aggregations the aggregations map.
   * @return the buckets map on terms keys.
   */
  public static Map<String, List<Object>> extractAggregations(final Aggregations aggregations) {
    if (Objects.isNull(aggregations)) {
      return Collections.emptyMap();
    }
    return toAggregationBucketsMap(aggregations.asMap());
  }

  public static boolean equalsCriteria(final String compareCid, final ArticleCriteria criteria) {
    if (StringUtils.isBlank(criteria.getCvp())) {
      return false;
    }
    return StringUtils.equalsIgnoreCase(compareCid, criteria.getCid());
  }

  public static void removeUnnecessaryValue(Set<String> aggregation, List<String> selectedValues) {
    if (CollectionUtils.isEmpty(aggregation)
        || CollectionUtils.size(aggregation) <= NumberUtils.INTEGER_ONE
        || CollectionUtils.isEmpty(selectedValues)) {
      return;
    }
    selectedValues.stream().forEach(selectedValue -> aggregation
        .removeIf(value -> !StringUtils.equalsIgnoreCase(selectedValue, value)));
  }

  public static List<Object> toSortedAscAggregationList(Set<?> objs) {
    if (CollectionUtils.isEmpty(objs)) {
      return Collections.emptyList();
    }
    return objs.stream().sorted().collect(Collectors.<Object>toList());
  }

  public static List<Object> toStringSortedDescAggregationList(Set<String> objs) {
    if (CollectionUtils.isEmpty(objs)) {
      return Collections.emptyList();
    }
    return objs.stream().sorted(Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER))
        .collect(Collectors.<Object>toList());
  }

  public static List<Object> toNumericSortedAggregationList(Set<String> objs) {
    if (CollectionUtils.isEmpty(objs)) {
      return Collections.emptyList();
    }
    return objs.stream().sorted((obj1, obj2) -> Doubles.compare(NumberUtils.toDouble(obj1),
        NumberUtils.toDouble(obj2))).collect(Collectors.toList());
  }

  public static List<Object> toSortedIteAggregationListByFirstNumeric(Set<String> objs) {
    if (CollectionUtils.isEmpty(objs)) {
      return Collections.emptyList();
    }
    return objs.stream().sorted((obj1, obj2) -> Doubles.compare(getFirstNumeric(obj1),
        getFirstNumeric(obj2))).collect(Collectors.toList());
  }

  /**
   * Gets Double value from string.
   *
   * Treats string as Germany for mat (comma for decimal separator)
   * Get first item splitted by forward slash (/) only
   *
   * @param text the input string
   * @return double value
   */
  private static double getFirstNumeric(String text) {
    double val = 0;
    if (StringUtils.isBlank(text)) {
      return 0;
    }
    try {
      NumberFormat firstNumeric = NumberFormat.getNumberInstance(Locale.GERMANY);
      val = firstNumeric.parse(text.split(SagConstants.SLASH)[0]).doubleValue();
    } catch (ParseException e) {
      return 0;
    }
    return val;
  }

  public static long countCvp(List<ArticleCriteria> criteria, String cvp) {
    if (CollectionUtils.isEmpty(criteria)) {
      return NumberUtils.LONG_ZERO;
    }
    return criteria.stream().filter(cr -> StringUtils.equals(cvp, cr.getCvp())).count();
  }

  public static void countAndCollectArticlesByCriteria(final Set<String> criteriaValueSet,
      Map<String, Integer> criteriaCount, String criteriaCvp) {
    if (MapUtils.isEmpty(criteriaCount) || !criteriaCount.containsKey(criteriaCvp)) {
      criteriaCount.put(criteriaCvp, 1);
    } else {
      criteriaCount.put(criteriaCvp, criteriaCount.get(criteriaCvp) + 1);
    }
    criteriaValueSet.add(criteriaCvp);
  }

  public static List<SliderChartDto> convertToSliderChart(Map<String, Integer> criteriaCount,
      Set<String> criteriaValueSet, List<String> filterCriteria) {
    if (MapUtils.isEmpty(criteriaCount)) {
      return Collections.emptyList();
    }
    if (!CollectionUtils.isEmpty(filterCriteria) && !CollectionUtils.isEmpty(criteriaValueSet)) {
      criteriaValueSet.removeIf(resAmp -> !filterCriteria.stream()
          .anyMatch(amp -> StringUtils.equalsIgnoreCase(amp, resAmp)));
    }
    final List<SliderChartDto> sliderChartDtos = new ArrayList<>();
    criteriaCount.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
        Map.Entry::getValue, (v1, v2) -> v2, LinkedHashMap::new)).forEach((criVal, searchCount) -> {
          if (criteriaValueSet.contains(criVal)) {
            sliderChartDtos.add(SliderChartDto.builder().criteriaValue(criVal)
                .numberOfArticles(searchCount).build());
          }
        });
    return sliderChartDtos;
  }

  public static List<Object> toCriteriaSortedAggregationList(List<SliderChartDto> sliderChartDtos) {
    if (CollectionUtils.isEmpty(sliderChartDtos)) {
      return Collections.emptyList();
    }
    return sliderChartDtos.stream()
        .sorted((obj1, obj2) -> Doubles.compare(NumberUtils.toDouble(obj1.getCriteriaValue()),
            NumberUtils.toDouble(obj2.getCriteriaValue())))
        .collect(Collectors.toList());
  }
}