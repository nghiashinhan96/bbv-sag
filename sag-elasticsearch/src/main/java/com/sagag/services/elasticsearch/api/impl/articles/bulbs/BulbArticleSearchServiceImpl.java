package com.sagag.services.elasticsearch.api.impl.articles.bulbs;

import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.BulbArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.BulbArticleResponse;
import com.sagag.services.elasticsearch.query.articles.bulbs.BulbArticleQueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class BulbArticleSearchServiceImpl extends AbstractArticleElasticsearchService
  implements IArticleSearchService<BulbArticleSearchCriteria, BulbArticleResponse> {

  @Autowired
  private BulbArticleQueryBuilder queryBuilder;

  /**
   * Returns the bulb articles information and its aggregation attributes information.
   *
   * @param criteria the criteria from aggregation
   * @param pageable the paging request
   * @return the {@link BulbArticleResponse}
   */
  @Override
  public BulbArticleResponse search(BulbArticleSearchCriteria criteria, Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return search(searchQuery, BulbArticleResultsExtractors.extract(criteria, pageable));
  }

  @Override
  public ArticleFilteringResponse filter(BulbArticleSearchCriteria criteria, Pageable pageable) {
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    return search(searchQuery, filteringExtractor(pageable));
  }

  @Override
  public ResultsExtractor<ArticleFilteringResponse> filteringExtractor(Pageable pageable) {
    return BulbArticleResultsExtractors.extractBulbsWithAgg(pageable);
  }
}
