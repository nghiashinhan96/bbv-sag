package com.sagag.services.hazelcast.api;


import com.sagag.services.domain.sag.external.Courier;

import java.util.List;

/**
 * Latest couriers infos caching expired in 1 hour and after user session ends.
 */
public interface CourierCacheService {

  /**
   * Gets cached couriers.
   *
   * @param companyName the company name
   * @return list of { @link Courier }
   */
  List<Courier> getCachedCouriers(String companyName);

  /**
   * Clears cache for specific companyName.
   *
   * @param companyName the companyName
   */
  void clearCache(String companyName);

}
