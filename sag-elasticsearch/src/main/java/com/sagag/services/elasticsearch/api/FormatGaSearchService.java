package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Elasticsearch service for Format Generic article document.
 */
public interface FormatGaSearchService extends IFetchAllDataService<FormatGaDoc> {

  /**
   * Returns all format ga to cache.
   *
   * @return list of {@link FormatGaDoc}
   */
  Page<FormatGaDoc> getAllFormatGa(Pageable pageable);

}
