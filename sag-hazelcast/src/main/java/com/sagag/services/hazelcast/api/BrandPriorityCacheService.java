package com.sagag.services.hazelcast.api;

import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface BrandPriorityCacheService extends CacheService {

  Map<String, CachedBrandPriorityDto> findCachedBrandPriority(List<String> gaids);

  @Override
  default String defName() {
    return HazelcastMaps.BRAND_PRIORITY_MAP.name();
  }

  @Override
  default String getCacheName() {
    return getCacheName(Locale.GERMAN);
  }
}
