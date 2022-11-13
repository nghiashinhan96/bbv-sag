package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.GenArtDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Elasticsearch service for Generic article document.
 */
public interface GenArtSearchService extends IFetchAllDataService<GenArtDoc> {

  /**
   * Returns all available generic articles.
   *
   * @return a list of {@link GenArtDoc}
   */
  Page<GenArtDoc> getAllGenArts(Pageable pageable);

}
