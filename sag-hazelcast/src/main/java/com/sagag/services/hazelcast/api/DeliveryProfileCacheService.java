package com.sagag.services.hazelcast.api;

import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import java.util.List;

public interface DeliveryProfileCacheService extends CacheService {

  List<DeliveryProfileDto> findAll();

  @Override
  default String defName() {
    return HazelcastMaps.DELIVERY_PROFILE_MAP.name();
  }
}
