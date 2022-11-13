package com.sagag.services.elasticsearch.api.impl.articles.batteries;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.common.BatteryConstants;
import com.sagag.services.elasticsearch.criteria.article.BatteryArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.BatteryArticleResponse;
import com.sagag.services.elasticsearch.query.articles.batteries.BatteryArticleQueryBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BatteryArticleSearchServiceImpl extends AbstractArticleElasticsearchService
  implements IArticleSearchService<BatteryArticleSearchCriteria, BatteryArticleResponse> {

  @Autowired
  private BatteryArticleQueryBuilder queryBuilder;

  /**
   * Returns the battery articles information and its aggregation attributes information.
   *
   * @param criteria the criteria from aggregation
   * @param pageable the paging request
   * @return the {@link BatteryArticleResponse}
   */
  @Override
  public BatteryArticleResponse search(BatteryArticleSearchCriteria criteria, Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    BatteryArticleResponse originSearchResult =
        search(searchQuery, BatteryArticleResultsExtractors.extractBatteries(criteria, pageable));
    List<String> sliderChartFields = extractSliderChartFields(criteria);
    if (CollectionUtils.isEmpty(sliderChartFields)) {
      return originSearchResult;
    }
    searchForAvailableSliderChartCriteriaValues(criteria, pageable, originSearchResult,
        sliderChartFields);
    return originSearchResult;
  }

  private void searchForAvailableSliderChartCriteriaValues(BatteryArticleSearchCriteria criteria,
      Pageable pageable, BatteryArticleResponse originSearchResult,
      List<String> sliderChartFields) {
    sliderChartFields.stream().forEach(sliderChartField -> {
      BatteryArticleSearchCriteria cloneCriteria = new BatteryArticleSearchCriteria();
      SagBeanUtils.copyProperties(criteria, cloneCriteria);
      switch (sliderChartField) {
        case BatteryConstants.AMPERE_HOUR_MAP_NAME:
          cloneCriteria.setAmpereHours(Collections.emptyList());
          break;
        case BatteryConstants.LENGTH_MAP_NAME:
          cloneCriteria.setLengths(Collections.emptyList());
          break;
        case BatteryConstants.WIDTH_MAP_NAME:
          cloneCriteria.setWidths(Collections.emptyList());
          break;
        case BatteryConstants.HEIGHT_MAP_NAME:
          cloneCriteria.setHeights(Collections.emptyList());
          break;
      }
      final SearchQuery extraSearchQuery =
          queryBuilder.buildQuery(cloneCriteria, pageable, index());
      BatteryArticleResponse extraSearchResult = search(extraSearchQuery,
          BatteryArticleResultsExtractors.extractBatteries(cloneCriteria, pageable));
      List<Object> extraAmpereHours = extraSearchResult.getAggregations().get(sliderChartField);
      originSearchResult.getAggregations().put(sliderChartField, extraAmpereHours);
    });
  }

  private static List<String> extractSliderChartFields(BatteryArticleSearchCriteria criteria) {
    List<String> sliderChartFields = new ArrayList<>();
    if (CollectionUtils.size(criteria.getAmpereHours()) > 1) {
      sliderChartFields.add(BatteryConstants.AMPERE_HOUR_MAP_NAME);
    }
    if (CollectionUtils.size(criteria.getLengths()) > 1) {
      sliderChartFields.add(BatteryConstants.LENGTH_MAP_NAME);
    }
    if (CollectionUtils.size(criteria.getWidths()) > 1) {
      sliderChartFields.add(BatteryConstants.WIDTH_MAP_NAME);
    }
    if (CollectionUtils.size(criteria.getHeights()) > 1) {
      sliderChartFields.add(BatteryConstants.HEIGHT_MAP_NAME);
    }
    return sliderChartFields;
  }

  @Override
  public ArticleFilteringResponse filter(BatteryArticleSearchCriteria criteria, Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return search(searchQuery, filteringExtractor(pageable));
  }

  @Override
  public ResultsExtractor<ArticleFilteringResponse> filteringExtractor(Pageable pageable) {
    return BatteryArticleResultsExtractors.extractBatteriesWithAgg(pageable);
  }
}
