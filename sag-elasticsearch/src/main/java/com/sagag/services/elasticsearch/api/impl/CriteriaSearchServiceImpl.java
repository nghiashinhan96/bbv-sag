package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.CriteriaSearchService;
import com.sagag.services.elasticsearch.domain.CriteriaDoc;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Elasticsearch service for Criteria document.
 */
@Service
public class CriteriaSearchServiceImpl extends AbstractElasticsearchService
    implements CriteriaSearchService {

  @Override
  public String keyAlias() {
    return "criteria";
  }

  @Override
  public Page<CriteriaDoc> getAllCriteria(Pageable pageable) {
    final NativeSearchQueryBuilder queryBuilder = allSearchQueryBuilder(index())
      .withPageable(pageable);
    return searchPage(queryBuilder.build(), CriteriaDoc.class);
  }

  @Override
  public List<CriteriaDoc> getTop10Criteria() {
    final NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
      .withIndices(index())
      .withPageable(PageUtils.DEF_PAGE)
      .withQuery(QueryBuilders.matchAllQuery());
    return searchList(queryBuilder.build(), CriteriaDoc.class);
  }

  @Override
  public List<CriteriaDoc> getAll() {
    return searchStream(allSearchQueryBuilder(index()).build(), CriteriaDoc.class);
  }
}
