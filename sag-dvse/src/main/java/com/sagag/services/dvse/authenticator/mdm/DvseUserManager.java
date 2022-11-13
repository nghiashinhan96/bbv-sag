package com.sagag.services.dvse.authenticator.mdm;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.hazelcast.app.HazelcastMaps;

@Component
public class DvseUserManager {

  private final IMap<String, ConnectUser> dvseUserMap;

  public DvseUserManager(final HazelcastInstance hazelcastInstance) {
    this.dvseUserMap = hazelcastInstance.getMap(HazelcastMaps.DVSE_USER_MAP.name());
  }

  public void add(String customerId, String username,
      String externalSessionId, ConnectUser user) {
    this.dvseUserMap.putIfAbsent(StringUtils.join(customerId, username, externalSessionId), user);
  }

  public Optional<ConnectUser> findConnectUser(String customerId, String username,
      String externalSessionId) {
    return Optional.ofNullable(this.dvseUserMap.get(
        StringUtils.join(customerId, username, externalSessionId)));
  }

}
