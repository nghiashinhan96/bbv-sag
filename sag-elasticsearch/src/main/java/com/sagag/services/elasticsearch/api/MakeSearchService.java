package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.MakeDoc;

import java.util.List;

/**
 * Elasticsearch service for Make document.
 */
public interface MakeSearchService extends IFetchAllDataService<MakeDoc> {

  /**
   * Returns top 10 makes from ES index.
   * <p>
   * only used for IT tests to reduce performance.
   *
   * @return a list of top 10 makes
   */
  List<MakeDoc> getTop10Makes();
}
