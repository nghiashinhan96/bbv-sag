package com.sagag.services.hazelcast.api;

import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import java.util.List;

public interface TourTimeCacheService extends CacheService {

  @Override
  default String defName() {
    return HazelcastMaps.TOUR_TIME_MAP.name();
  }

  /**
   * Returns tour time list by customer nr.
   *
   */
  List<TourTimeDto> searchTourTimesByCustNr(String custNr);

}
