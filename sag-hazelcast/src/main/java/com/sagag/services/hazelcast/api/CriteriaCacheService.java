package com.sagag.services.hazelcast.api;

import com.hazelcast.core.IMap;
import com.sagag.services.elasticsearch.domain.CriteriaDoc;
import com.sagag.services.elasticsearch.domain.CriteriaTxt;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import java.util.List;
import java.util.Map;

/**
 * Interface to define services for criteria caching.
 */
public interface CriteriaCacheService extends CacheService {

  /**
   * Returns a map of {@link CriteriaTxt} by criteria ids.
   *
   * @param criteriaIds the criteria ids to find..
   * @return a map of {@link CriteriaTxt}. Returns empty map if no criteria found.
   */
  Map<String, CriteriaTxt> searchCriteriaByIds(final List<String> criteriaIds);

  @Override
  default String defName() {
    return HazelcastMaps.CRITERIA_MAP.name();
  }

  /**
   * Refreshes the list of criteria docs to cache.
   *
   * @param criteriaDocs the list of criteria docs to refresh
   * @return a refresh map of criteria
   */
  IMap<String, CriteriaDoc> refreshCacheCriteria(List<CriteriaDoc> criteriaDocs);
}
