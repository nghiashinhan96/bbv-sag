package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.ModelDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Elasticsearch service interfacing for Model document.
 */
public interface ModelSearchService extends IFetchAllDataService<ModelDoc> {

  /**
   * Returns all available models.
   *
   * @return a list of {@link ModelDoc}
   */
  Page<ModelDoc> getAllModels(Pageable pageable);

}
