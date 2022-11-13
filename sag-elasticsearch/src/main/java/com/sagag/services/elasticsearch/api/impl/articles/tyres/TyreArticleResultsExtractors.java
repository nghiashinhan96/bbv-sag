package com.sagag.services.elasticsearch.api.impl.articles.tyres;

import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.common.contants.TyreConstants.FzCategory;
import com.sagag.services.common.contants.TyreConstants.MotorbikeCategory;
import com.sagag.services.common.contants.TyreConstants.Season;
import com.sagag.services.elasticsearch.domain.article.ArticleCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.TyreArticleResponse;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.ResultsExtractorUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

@UtilityClass
public final class TyreArticleResultsExtractors {

  /**
   * Extracts ES result in tyres.
   *
   * @param pageable the pagination
   * @return the result extractor of {@link TyreArticleResponse}
   */
  public static ResultsExtractor<TyreArticleResponse> extractTyres(final Pageable pageable) {
    return response -> {

      final Page<ArticleDoc> articles =
          ResultsExtractorUtils.extractPageResult(response, ArticleDoc.class, pageable);

      // Get custom aggregation list from search result
      final Map<String, List<Object>> curAggs =
          ResultsExtractorUtils.extractAggregations(response.getAggregations());
      if (MapUtils.isEmpty(curAggs)) {
        return TyreArticleResponse.builder().articles(articles).build();
      }

      // Build the map of aggregations
      final Map<String, List<Object>> aggregations = new HashMap<>();

      final Set<Double> widthCvpSet = new HashSet<>();
      final Set<Double> heightCvpSet = new HashSet<>();
      final Set<Double> radiusCvpSet = new HashSet<>();
      final Set<String> speedIndexCvpSet = new HashSet<>();
      final Set<String> tyreSegmentSet = new HashSet<>();
      final Set<Double> loadIndexSet = new HashSet<>();

      articles.getContent().stream().flatMap(article -> article.getCriteria().stream())
          .forEach(criteria -> {
            if (equalsCriteria(String.valueOf(TyreConstants.WIDTH_CID), criteria)) {
              widthCvpSet.add(NumberUtils.toDouble(criteria.getCvp()));
            } else if (equalsCriteria(String.valueOf(TyreConstants.HEIGHT_CID), criteria)) {
              heightCvpSet.add(NumberUtils.toDouble(criteria.getCvp()));
            } else if (equalsCriteria(String.valueOf(TyreConstants.RADIUS_CID), criteria)) {
              radiusCvpSet.add(NumberUtils.toDouble(criteria.getCvp()));
            } else if (equalsCriteria(String.valueOf(TyreConstants.SPEED_INDEX_CID), criteria)) {
              speedIndexCvpSet.add(criteria.getCvp());
            } else if (equalsCriteria(String.valueOf(TyreConstants.TYRE_SEGMENT_CID), criteria)) {
              tyreSegmentSet.add(criteria.getCvp());
            } else if (equalsCriteria(String.valueOf(TyreConstants.LOAD_INDEX_CID), criteria)) {
              loadIndexSet.add(NumberUtils.createDouble(criteria.getCvp()));
            }
          });

      aggregations.put(TyreConstants.WIDTH_MAP_NAME,
          widthCvpSet.stream().sorted().collect(Collectors.toList()));
      aggregations.put(TyreConstants.HEIGHT_MAP_NAME,
          heightCvpSet.stream().sorted().collect(Collectors.toList()));
      aggregations.put(TyreConstants.RADIUS_MAP_NAME,
          radiusCvpSet.stream().sorted().collect(Collectors.toList()));
      aggregations.put(TyreConstants.SPEED_INDEX_MAP_NAME,
          speedIndexCvpSet.stream().sorted().collect(Collectors.toList()));
      aggregations.put(TyreConstants.LOAD_INDEX_MAP_NAME,
          loadIndexSet.stream().sorted().collect(Collectors.toList()));


      // Remove run flat and spike value from the list of tyre_segment values
      final List<Object> tyreSegmentList =
          tyreSegmentSet.stream().sorted().collect(Collectors.toList());
      tyreSegmentList.removeIf(
          segment -> StringUtils.equalsIgnoreCase(TyreConstants.RUNFLAT_CVP, segment.toString())
              || StringUtils.equalsIgnoreCase(TyreConstants.SPIKE_CVP, segment.toString()));

      aggregations.put(TyreConstants.TYRE_SEGMENT_MAP_NAME, tyreSegmentList);

      final List<String> genArtIds = curAggs.get(ArticleField.GA_ID.name()).stream()
          .map(Object::toString).collect(Collectors.toList());

      aggregations.put(TyreConstants.SEASON_MAP_NAME, getAllSeasons());
      aggregations.put(TyreConstants.FZ_CATEGORY_MAP_NAME, getFzCategories(genArtIds));
      aggregations.put(TyreConstants.MOTOR_CATEGORY_MAP_NAME, getAllMotorbikeCategories());

      aggregations.put(TyreConstants.SUPPLIER_MAP_NAME, getSuppliers(curAggs));

      return TyreArticleResponse.builder().articles(articles).aggregations(aggregations).build();
    };
  }

  private static List<Object> getAllMotorbikeCategories() {
    return Stream.of(TyreConstants.MotorbikeCategory.values()).map(MotorbikeCategory::name).sorted()
        .collect(Collectors.toList());
  }

  private static boolean equalsCriteria(final String compareCid, final ArticleCriteria criteria) {
    if (StringUtils.isBlank(criteria.getCvp())) {
      return false;
    }
    return StringUtils.equalsIgnoreCase(compareCid, criteria.getCid());
  }

  private static List<Object> getSuppliers(Map<String, List<Object>> curAggs) {
    return curAggs.get(ArticleField.SUPPLIER_RAW.name()).stream().sorted()
        .collect(Collectors.toList());
  }

  private static List<Object> getAllSeasons() {
    return Stream.of(TyreConstants.Season.values()).map(Season::name).sorted()
        .collect(Collectors.toList());
  }

  private static List<Object> getFzCategories(List<String> genArtIds) {
    final Set<FzCategory> fzCates = new HashSet<>();
    Stream.of(TyreConstants.FzCategory.values())
        .forEach(fzCate -> fzCate.getGenArtIds().stream().forEach(genArtId -> {
          if (genArtIds.contains(genArtId)) {
            fzCates.add(fzCate);
          }
        }));
    return fzCates.stream().map(FzCategory::name).sorted().collect(Collectors.toList());
  }

  /**
   * Extracts ES result in motor tyres.
   *
   * @param pageable the pagination
   * @return the result extractor of {@link TyreArticleResponse}
   */
  public static ResultsExtractor<TyreArticleResponse> extractMotorTyres(final Pageable pageable) {
    return response -> {

      final Page<ArticleDoc> articles =
          ResultsExtractorUtils.extractPageResult(response, ArticleDoc.class, pageable);

      // Get custom aggregation list from search result
      final Map<String, List<Object>> curAggs =
          ResultsExtractorUtils.extractAggregations(response.getAggregations());
      if (MapUtils.isEmpty(curAggs)) {
        return TyreArticleResponse.builder().articles(articles).build();
      }

      // Build the map of aggregations
      final Map<String, List<Object>> aggregations = new HashMap<>();

      final Set<Double> widthCvpSet = new HashSet<>();
      final Set<Double> heightCvpSet = new HashSet<>();
      final Set<Double> radiusCvpSet = new HashSet<>();
      final Set<String> speedIndexCvpSet = new HashSet<>();
      final Set<String> tyreSegmentSet = new HashSet<>();
      final Set<Double> loadIndexSet = new HashSet<>();

      articles.getContent().stream().flatMap(article -> article.getCriteria().stream())
          .forEach(criteria -> {
            if (ResultsExtractorUtils.equalsCriteria(String.valueOf(TyreConstants.WIDTH_CID),
                criteria)) {
              widthCvpSet.add(NumberUtils.toDouble(criteria.getCvp()));
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(TyreConstants.HEIGHT_CID), criteria)) {
              heightCvpSet.add(NumberUtils.toDouble(criteria.getCvp()));
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(TyreConstants.RADIUS_CID), criteria)) {
              radiusCvpSet.add(NumberUtils.toDouble(criteria.getCvp()));
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(TyreConstants.SPEED_INDEX_CID), criteria)) {
              speedIndexCvpSet.add(criteria.getCvp());
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(TyreConstants.TYRE_SEGMENT_CID), criteria)) {
              tyreSegmentSet.add(criteria.getCvp());
            } else if (ResultsExtractorUtils
                .equalsCriteria(String.valueOf(TyreConstants.LOAD_INDEX_CID), criteria)) {
              loadIndexSet.add(NumberUtils.createDouble(criteria.getCvp()));
            }
          });

      aggregations.put(TyreConstants.WIDTH_MAP_NAME,
          widthCvpSet.stream().sorted().collect(Collectors.toList()));
      aggregations.put(TyreConstants.HEIGHT_MAP_NAME,
          heightCvpSet.stream().sorted().collect(Collectors.toList()));
      aggregations.put(TyreConstants.RADIUS_MAP_NAME,
          radiusCvpSet.stream().sorted().collect(Collectors.toList()));
      aggregations.put(TyreConstants.SPEED_INDEX_MAP_NAME,
          speedIndexCvpSet.stream().sorted().collect(Collectors.toList()));
      aggregations.put(TyreConstants.LOAD_INDEX_MAP_NAME,
          loadIndexSet.stream().sorted().collect(Collectors.toList()));

      // Remove run flat and spike value from the list of tyre_segment values
      final List<Object> tyreSegmentList =
          tyreSegmentSet.stream().sorted().collect(Collectors.toList());
      tyreSegmentList.removeIf(
          segment -> StringUtils.equalsIgnoreCase(TyreConstants.RUNFLAT_CVP, segment.toString())
              || StringUtils.equalsIgnoreCase(TyreConstants.SPIKE_CVP, segment.toString()));

      aggregations.put(TyreConstants.TYRE_SEGMENT_MAP_NAME, tyreSegmentList);

      aggregations.put(TyreConstants.MOTOR_CATEGORY_MAP_NAME, getAllMotorbikeCategories());

      aggregations.put(TyreConstants.SUPPLIER_MAP_NAME, getSuppliers(curAggs));

      return TyreArticleResponse.builder().articles(articles).aggregations(aggregations).build();
    };
  }

}