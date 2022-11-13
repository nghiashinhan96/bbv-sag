package com.sagag.services.ivds.filter.articles;

import com.hazelcast.util.function.BiConsumer;
import com.sagag.services.elasticsearch.criteria.article.ArticleAggregateCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface class to provide filter articles by request.
 */
@FunctionalInterface
public interface IArticleFilter {

  /**
   * Returns the list of articles by request.
   *
   * @param request the search request
   * @param pageable the paging object
   * @param affNameLocks
   * @param isSaleOnBehalf
   * @return the result of {@link ArticleFilteringResponse}
   */
  ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf);

  /**
   * Returns the current mode of query.
   *
   */
  default FilterMode mode() {
    return null;
  }

  default BiConsumer<ArticleFilterRequest, ArticleAggregateCriteria> aggregationFilterMapper() {
    return (request, criteria) -> {
      Optional.ofNullable(request.getSuppliers()).filter(CollectionUtils::isNotEmpty)
      .ifPresent(criteria::setSupplierRaws);

      Optional.ofNullable(request.getGaIds()).filter(CollectionUtils::isNotEmpty)
      .ifPresent(criteria::setGaIds);

      Optional.ofNullable(request.getCids()).filter(CollectionUtils::isNotEmpty)
      .ifPresent(criteria::setCids);

      Optional.ofNullable(request.getGaIdsMultiLevels()).filter(CollectionUtils::isNotEmpty)
      .ifPresent(criteria::setAggregateMultiLevels);

      criteria.setUseMultipleAggregation(request.isUseMultipleLevelAggregation());
      criteria.setNeedSubAggregated(request.isNeedSubAggregated());
    };
  }
}
