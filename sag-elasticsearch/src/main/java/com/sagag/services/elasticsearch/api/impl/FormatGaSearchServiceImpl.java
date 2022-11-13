package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.FormatGaSearchService;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;

import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Elasticsearch service implementation class for Format Generic Article document.
 */
@Service
@Slf4j
public class FormatGaSearchServiceImpl extends AbstractElasticsearchService
    implements FormatGaSearchService {

  @Override
  public String keyAlias() {
    return "format_ga";
  }

  @Override
  public Page<FormatGaDoc> getAllFormatGa(Pageable pageable) {
    log.debug("Executing FormatGaSearchServiceImpl -> getAllFormatGa()");
    final NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
      .withIndices(index())
      .withPageable(pageable)
      .withQuery(QueryBuilders.matchAllQuery());
    return searchPage(queryBuilder.build(), FormatGaDoc.class);
  }

  @Override
  public List<FormatGaDoc> getAll() {
    return searchStream(allSearchQueryBuilder(index()).build(), FormatGaDoc.class);
  }
}
