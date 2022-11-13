package com.sagag.services.hazelcast.api;

import com.hazelcast.core.IMap;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import java.util.List;
import java.util.Map;

/**
 * Interface to define services for format ga caching.
 */
public interface FormatGaCacheService extends CacheService {

  /**
   * Returns the map of {@link FormatGaDoc} from its ids.
   *
   * @param gaIds the searching generic article ids
   * @return a list of {@link FormatGaDoc}. Returns empty map if no generic articles found.
   */
  Map<String, FormatGaDoc> searchFormatGaByGaIds(final List<String> gaIds);

  @Override
  default String defName() {
    return HazelcastMaps.FORMAT_GA_MAP.name();
  }

  /**
   * Refreshes the cache with a list of format generic articles.
   *
   * @return the {@link IMap} of refresh format generic articles
   */
  IMap<String, FormatGaDoc> refreshCacheFormatGas(List<FormatGaDoc> formatGas);
}
