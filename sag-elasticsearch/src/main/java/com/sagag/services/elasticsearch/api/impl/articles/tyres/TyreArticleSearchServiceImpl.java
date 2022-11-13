package com.sagag.services.elasticsearch.api.impl.articles.tyres;

import com.sagag.services.elasticsearch.criteria.article.TyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.TyreArticleResponse;
import com.sagag.services.elasticsearch.query.articles.tyres.TyreArticleQueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class TyreArticleSearchServiceImpl
  extends AbstractTyreArticleSearchService<TyreArticleSearchCriteria> {

  @Autowired
  private TyreArticleQueryBuilder queryBuilder;

  /**
   * Returns the tyre articles information and its aggregation attributes information.
   *
   * @param criteria the criteria from aggregation
   * @param pageable the paging request
   * @return the {@link TyreArticleResponse}, nullable
   */
  @Override
  public TyreArticleResponse search(TyreArticleSearchCriteria criteria, Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    final Supplier<TyreArticleResponse> supplier =
        () -> search(searchQuery, TyreArticleResultsExtractors.extractTyres(pageable));
    return supplier.get();
  }

  @Override
  public ArticleFilteringResponse filter(TyreArticleSearchCriteria criteria, Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return filterOrThrowIfResultOverLimit(
        () -> search(searchQuery, filteringExtractor(pageable)));
  }
}
