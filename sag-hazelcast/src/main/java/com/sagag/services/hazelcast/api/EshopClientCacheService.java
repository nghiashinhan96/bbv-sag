package com.sagag.services.hazelcast.api;

import com.sagag.eshop.service.dto.client.EshopClientDto;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import java.util.Optional;

public interface EshopClientCacheService extends CacheService {

  /**
   * Returns client info from client id.
   *
   * @param clientId
   * @return the optional of <code>ClientDetailCacheData</code>
   */
  Optional<EshopClientDto> findByClientId(String clientId);

  @Override
  default String defName() {
    return HazelcastMaps.ESHOP_CLIENT_DETAILS.name();
  }

  @Override
  default boolean exists() {
    return false;
  }
}
