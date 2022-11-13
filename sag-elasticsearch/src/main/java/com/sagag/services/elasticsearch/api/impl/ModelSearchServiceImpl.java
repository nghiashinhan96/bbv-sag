package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.ModelSearchService;
import com.sagag.services.elasticsearch.domain.ModelDoc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Elasticsearch service implementation for model document.
 */
@Service
public class ModelSearchServiceImpl extends AbstractElasticsearchService
    implements ModelSearchService {

  @Override
  public String keyAlias() {
    return "models";
  }

  @Override
  public Page<ModelDoc> getAllModels(Pageable pageable) {
    final NativeSearchQueryBuilder queryBuilder = allSearchQueryBuilder(index())
      .withPageable(pageable);
    return searchPage(queryBuilder.build(), ModelDoc.class);
  }

  @Override
  public List<ModelDoc> getAll() {
    return searchStream(allSearchQueryBuilder(index()).build(), ModelDoc.class);
  }

}
