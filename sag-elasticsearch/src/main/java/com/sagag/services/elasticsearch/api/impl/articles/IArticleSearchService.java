package com.sagag.services.elasticsearch.api.impl.articles;

import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.extractor.ArticleFilteringResultsExtractors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

/**
 * The interface of article search service.
 *
 * @param <T> the input generic class
 * @param <R> the result generic class
 */
public interface IArticleSearchService<T, R> {

  /**
   * Returns the search results by criteria.
   *
   * @param criteria the search criteria
   * @param pageable the pagination
   * @return the result object
   */
  R search(T criteria, Pageable pageable);

  /**
   * Filters the search results by criteria.
   *
   * @param criteria the search criteria
   * @param pageable the pagination
   * @return the result of {@link ArticleFilteringResponse}
   */
  ArticleFilteringResponse filter(T criteria, Pageable pageable);

  default ResultsExtractor<ArticleFilteringResponse> filteringExtractor(final Pageable pageable) {
    return ArticleFilteringResultsExtractors.extract(pageable);
  }
}
