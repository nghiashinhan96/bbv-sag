package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.hazelcast.api.DmsExportCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DmsExportCacheServiceImpl implements DmsExportCacheService {

  private static final String MAP_NAME = HazelcastMaps.DMS_EXPORT.name();

  @Autowired
  private HazelcastInstance hazelcast;

  @Override
  public void add(String fileContent, String userKey) {
    final IMap<String, String> dmsExportCache = hazelcast.getMap(MAP_NAME);
    dmsExportCache.put(userKey, fileContent);
  }

  @Override
  public String getFileContent(String userKey) {
    final IMap<String, String> dmsExportCache = hazelcast.getMap(MAP_NAME);
    return dmsExportCache.get(userKey);
  }

  @Override
  public void clearCache(String userKey) {
    hazelcast.getMap(MAP_NAME).evict(userKey);
  }

}
