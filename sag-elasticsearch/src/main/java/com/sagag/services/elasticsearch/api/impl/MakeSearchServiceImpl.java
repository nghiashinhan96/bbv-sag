package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.MakeSearchService;
import com.sagag.services.elasticsearch.domain.MakeDoc;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Elasticsearch service implementation for Make document.
 */
@Service
public class MakeSearchServiceImpl extends AbstractElasticsearchService
    implements MakeSearchService {

  @Override
  public String keyAlias() {
    return "makes";
  }

  @Override
  public List<MakeDoc> getTop10Makes() {
    final NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
        .withIndices(index())
        .withPageable(PageUtils.DEF_PAGE)
        .withQuery(QueryBuilders.matchAllQuery());
    return searchList(queryBuilder.build(), MakeDoc.class);
  }

  @Override
  public List<MakeDoc> getAll() {
    return searchStream(allSearchQueryBuilder(index()).build(), MakeDoc.class);
  }
}
