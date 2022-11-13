package com.sagag.services.elasticsearch.api.impl.articles;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.elasticsearch.api.LoopSearchSupport;
import com.sagag.services.elasticsearch.api.impl.articles.external.ExternalArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.KeywordExternalArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class ArticleLoopSearchService extends KeywordArticleSearchService
  implements LoopSearchSupport<KeywordArticleSearchCriteria> {

  @Autowired
  private ExternalArticleSearchService extArticleSearchService;

  public Page<ArticleDoc> searchArticlesLoop(KeywordArticleSearchCriteria criteria,
    Pageable pageable, boolean fromFreetextSearch) {
    final SearchQuery[] searchQueries = buildLoopQueries(criteria, pageable);
    if (!criteria.isSearchExternal()) {
      return searchPageLoop(searchQueries, ArticleDoc.class);
    }

    final KeywordExternalArticleSearchCriteria extArtSearchCriteria =
        SagBeanUtils.map(criteria, KeywordExternalArticleSearchCriteria.class);
    extArtSearchCriteria.setSearchQueries(searchQueries);
    extArtSearchCriteria.setFromTextSearch(fromFreetextSearch);
    Page<ArticleDoc> result = extArticleSearchService.search(extArtSearchCriteria, pageable);
    criteria.setDoubleLoopSearch(extArtSearchCriteria.isDoubleLoopSearch());
    return result;
  }

  public ArticleFilteringResponse filterArticlesLoop(KeywordArticleSearchCriteria criteria,
    Pageable pageable, boolean fromFreetextSearch) {
    final SearchQuery[] searchQueries = buildLoopQueries(criteria, pageable);
    if (!criteria.isSearchExternal()) {
      return filterLoop(searchQueries, filteringExtractor(pageable));
    }

    final KeywordExternalArticleSearchCriteria extArtSearchCriteria =
      SagBeanUtils.map(criteria, KeywordExternalArticleSearchCriteria.class);
    extArtSearchCriteria.setSearchQueries(searchQueries);
    extArtSearchCriteria.setFromTextSearch(fromFreetextSearch);
    return extArticleSearchService.filter(extArtSearchCriteria, pageable);
  }

  @Override
  public SearchQuery[] buildLoopQueries(KeywordArticleSearchCriteria criteria,
    Pageable pageable) {
    final List<SearchQuery> queries = new ArrayList<>();
    if (queryBuilder() == null) {
      return queries.toArray(new SearchQuery[0]);
    }
    log.debug("index: {}", index());
    queries.add(queryBuilder().buildQuery(criteria, pageable, index()));
    if (criteria.isOnUsePartsExtMode()) {
      criteria.usePartsExt();
      queries.add(queryBuilder().buildQuery(criteria, pageable, index()));
    }
    return queries.toArray(new SearchQuery[0]);
  }

}
