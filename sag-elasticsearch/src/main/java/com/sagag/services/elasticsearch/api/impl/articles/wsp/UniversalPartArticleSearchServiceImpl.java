package com.sagag.services.elasticsearch.api.impl.articles.wsp;

import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.UniversalPartArticleResponse;
import com.sagag.services.elasticsearch.query.articles.article.wsp.UniversalPartArticleListQueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class UniversalPartArticleSearchServiceImpl
    extends AbstractUniversalPartArticleSearchService<UniversalPartArticleSearchCriteria> {

  @Autowired
  private UniversalPartArticleListQueryBuilder queryBuilder;

  /**
   * Returns the genarts of articles from unitree leaf criteria
   *
   * @param criteria the criteria from unitree leaf criteria
   * @param pageable the paging request
   * @return the {@link UniversalPartArticleResponse}
   */
  @Override
  public UniversalPartArticleResponse search(UniversalPartArticleSearchCriteria criteria,
      Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    final Supplier<UniversalPartArticleResponse> supplier = () -> search(searchQuery,
        UniversalPartArticleResultsExtractors.extractUniversalPart(pageable));
    return supplier.get();
  }

  /**
   * Returns the universal part articles information and its aggregation attributes information.
   *
   * @param criteria the criteria from request
   * @param pageable the paging request
   * @return the {@link ArticleFilteringResponse}
   */
  @Override
  public ArticleFilteringResponse filter(UniversalPartArticleSearchCriteria criteria,
      Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return search(searchQuery, filteringExtractor(pageable));
  }
}
