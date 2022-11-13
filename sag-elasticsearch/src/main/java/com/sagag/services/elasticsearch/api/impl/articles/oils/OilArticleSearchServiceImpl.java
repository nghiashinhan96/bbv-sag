package com.sagag.services.elasticsearch.api.impl.articles.oils;

import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.common.OilConstants;
import com.sagag.services.elasticsearch.criteria.article.OilArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.OilArticleResponse;
import com.sagag.services.elasticsearch.query.articles.oils.OilArticleQueryBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OilArticleSearchServiceImpl extends AbstractArticleElasticsearchService
  implements IArticleSearchService<OilArticleSearchCriteria, OilArticleResponse> {

  @Autowired
  private OilArticleQueryBuilder queryBuilder;

  @Autowired
  private CountryConfiguration countryConfiguration;

  /**
   * Returns the oil articles information and its aggregation attributes information.
   *
   * @param criteria the criteria from aggregation
   * @param pageable the paging request
   * @return the {@link OilArticleResponse}
   */
  @Override
  public OilArticleResponse search(OilArticleSearchCriteria criteria, Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    OilArticleResponse originResponse = search(searchQuery, OilArticleResultsExtractors
        .extract(criteria, countryConfiguration.isSortingOilVehicleInDesc()));
    if (CollectionUtils.size(criteria.getBottleSizes()) <= 1) {
      return originResponse;
    }
    OilArticleSearchCriteria cloneCriteria = new OilArticleSearchCriteria();
    SagBeanUtils.copyProperties(criteria, cloneCriteria);
    cloneCriteria.setBottleSizes(Collections.emptyList());
    final SearchQuery extraSearchQuery = queryBuilder.buildQuery(cloneCriteria, pageable, index());
    OilArticleResponse extraResponse = search(extraSearchQuery, OilArticleResultsExtractors
        .extract(cloneCriteria, countryConfiguration.isSortingOilVehicleInDesc()));
    List<Object> extraBottleSizes =
        extraResponse.getAggregations().get(OilConstants.BOTTLE_SIZE_MAP_NAME);
    originResponse.getAggregations().put(OilConstants.BOTTLE_SIZE_MAP_NAME, extraBottleSizes);
    return originResponse;
  }

  @Override
  public ArticleFilteringResponse filter(OilArticleSearchCriteria criteria, Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return search(searchQuery, filteringExtractor(pageable));
  }

  @Override
  public ResultsExtractor<ArticleFilteringResponse> filteringExtractor(Pageable pageable) {
    return OilArticleResultsExtractors.extractOilsWithAgg(pageable);
  }
}
