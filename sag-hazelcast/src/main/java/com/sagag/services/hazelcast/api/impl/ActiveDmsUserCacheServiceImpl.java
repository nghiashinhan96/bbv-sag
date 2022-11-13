package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.hazelcast.api.ActiveDmsUserCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.hazelcast.model.ActiveUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation class for dms user cache service.
 */
@Service
public class ActiveDmsUserCacheServiceImpl implements ActiveDmsUserCacheService {

  @Autowired
  private HazelcastInstance cacheInstance;

  @Override
  public ActiveUser get(final Long userId) {
    final IMap<Long, ActiveUser> userMap = cacheInstance
        .getMap(HazelcastMaps.ACTIVE_DMS_USER.name());
    return userMap.get(userId);
  }

  @Override
  public void put(final ActiveUser user) {
    final IMap<Long, ActiveUser> userMap = cacheInstance
        .getMap(HazelcastMaps.ACTIVE_DMS_USER.name());
    userMap.put(user.getId(), user);
  }

  @Override
  public void remove(final Long userId) {
    final IMap<Long, ActiveUser> userMap = cacheInstance
        .getMap(HazelcastMaps.ACTIVE_DMS_USER.name());
    userMap.remove(userId);
  }

  @Override
  public boolean contains(final Long userId) {
    final IMap<Long, ActiveUser> userMap = cacheInstance
        .getMap(HazelcastMaps.ACTIVE_DMS_USER.name());
    return userMap.containsKey(userId);
  }

}
