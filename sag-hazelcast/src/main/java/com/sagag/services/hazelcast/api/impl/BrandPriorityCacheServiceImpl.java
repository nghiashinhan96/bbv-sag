package com.sagag.services.hazelcast.api.impl;

import com.google.common.collect.Sets;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.elasticsearch.api.BrandPriorityService;
import com.sagag.services.elasticsearch.domain.brand_sorting.BrandPriorityDoc;
import com.sagag.services.hazelcast.api.BrandPriorityCacheService;
import com.sagag.services.hazelcast.builder.BrandPriorityBuilder;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BrandPriorityCacheServiceImpl extends CacheDataProcessor
  implements BrandPriorityCacheService {

  @Autowired
  private BrandPriorityService brandPriorityService;

  @Autowired
  private BrandPriorityBuilder brandPriorityBuilder;

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

  @Override
  public Map<String, CachedBrandPriorityDto> findCachedBrandPriority(List<String> gaids) {
    if (CollectionUtils.isEmpty(gaids)) {
      return Collections.emptyMap();
    }
    final IMap<String, CachedBrandPriorityDto> brandPriorities =
        hzInstance().getMap(getCacheName());
    return brandPriorities.getAll(Sets.newHashSet(gaids));
  }

  @Override
  public void refreshCacheAll() {
    hzInstance().getMap(getCacheName()).evictAll();
    final List<BrandPriorityDoc> priorities = brandPriorityService.getAll();
    final Map<String, CachedBrandPriorityDto> brandPrioritiesMap = priorities.stream()
        .map(brandPriorityBuilder)
        .collect(Collectors.toMap(CachedBrandPriorityDto::getGaid, Function.identity()));
    hzInstance().getMap(getCacheName()).putAll(brandPrioritiesMap);
  }

}
