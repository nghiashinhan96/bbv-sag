package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.hazelcast.api.VinOrderCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation class for Vin Order cache service.
 */
@Service
public class VinOrderCacheServiceImpl implements VinOrderCacheService {

  private static final String MAP_NAME = HazelcastMaps.VIN_SEARCH_COUNT.name();

  @Autowired
  private HazelcastInstance hazelcast;

  @Override
  public void increaseSearchCount(final String key) {
    final IMap<String, Integer> searchCountCache = hazelcast.getMap(MAP_NAME);
    int searchCount;
    if (searchCountCache.get(key + MAP_NAME) != null) {
      searchCount = searchCountCache.get(key + MAP_NAME) + 1;
    } else {
      searchCount = 1;
    }
    searchCountCache.put(key + MAP_NAME, searchCount);
  }

  @Override
  public void clearSearchCount(String key) {
    hazelcast.getMap(MAP_NAME).evict(key + MAP_NAME);
  }

  @Override
  public Integer getSearchCount(String key) {
    final IMap<String, Integer> searchCountCache = hazelcast.getMap(MAP_NAME);
    return searchCountCache.get(key + MAP_NAME);
  }

}
