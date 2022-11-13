package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.GenArtSearchService;
import com.sagag.services.elasticsearch.domain.GenArtDoc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Elasticsearch service implementation class for Generic article document.
 */
@Service
public class GenArtSearchServiceImpl extends AbstractElasticsearchService
  implements GenArtSearchService {

  @Override
  public String keyAlias() {
    return "genart";
  }

  @Override
  public Page<GenArtDoc> getAllGenArts(Pageable pageable) {
    final NativeSearchQueryBuilder queryBuilder = allSearchQueryBuilder(index())
      .withPageable(pageable);
    return searchPage(queryBuilder.build(), GenArtDoc.class);
  }

  @Override
  @LogExecutionTime
  public List<GenArtDoc> getAll() {
    return searchStream(allSearchQueryBuilder(index()).build(),
        GenArtDoc.class);
  }

}
