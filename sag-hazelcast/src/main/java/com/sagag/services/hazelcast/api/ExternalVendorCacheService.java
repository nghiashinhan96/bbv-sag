package com.sagag.services.hazelcast.api;

import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import java.util.List;

public interface ExternalVendorCacheService extends CacheService {

  List<ExternalVendorDto> findAll();

  @Override
  default String defName() {
    return HazelcastMaps.EXTERNAL_VENDOR_MAP.name();
  }
}
