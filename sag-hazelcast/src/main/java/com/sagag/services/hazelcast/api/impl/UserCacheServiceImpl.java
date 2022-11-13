package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import java.util.Objects;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation class for user cache service.
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {

  private static final String MAP_NAME = HazelcastMaps.SESSION_USER_MAP.name();

  @Autowired
  private HazelcastInstance cacheInstance;

  @Override
  @LogExecutionTime
  public UserInfo get(Long userId) {
    final IMap<Long, UserInfo> userMap = cacheInstance.getMap(MAP_NAME);
    if (MapUtils.isEmpty(userMap)) {
      return null;
    }
    return userMap.get(userId);
  }

  @Override
  public void put(UserInfo user) {
    final IMap<Long, UserInfo> userMap = cacheInstance.getMap(MAP_NAME);
    if (Objects.isNull(userMap)) {
      return;
    }
    userMap.put(user.getId(), user);
  }

  @Override
  public void remove(Long userId) {
    final IMap<Long, UserInfo> userMap = cacheInstance.getMap(MAP_NAME);
    if (MapUtils.isEmpty(userMap)) {
      return;
    }
    userMap.remove(userId);
  }

  @Override
  public UserInfo getBySid(String sid) {
    final IMap<Long, UserInfo> userMap = cacheInstance.getMap(MAP_NAME);
    return userMap.values(Predicates.in("externalUserSession.sid", sid))
        .stream().findFirst().orElse(null);
  }

}
