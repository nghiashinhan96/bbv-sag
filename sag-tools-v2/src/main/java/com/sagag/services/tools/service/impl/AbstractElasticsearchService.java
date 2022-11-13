package com.sagag.services.tools.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

/**
 * Abstract elasticsearch service to inherits common properties and apis.
 */
public abstract class AbstractElasticsearchService {

  @Autowired
  private ElasticsearchTemplate esTemplate;

  /**
   * Returns elasticsearch template.
   * 
   * @return the {@link ElasticsearchTemplate}
   */
  protected ElasticsearchTemplate getEsTemplate() {
    return esTemplate;
  }

  protected <T> T search(SearchQuery searchQuery, ResultsExtractor<T> extractor) {
    return getEsTemplate().query(searchQuery, extractor);
  }

  protected <T> Page<T> searchPage(SearchQuery searchQuery, Class<T> clazz) {
    return getEsTemplate().queryForPage(searchQuery, clazz);
  }

}
