package com.sagag.services.hazelcast.api;

import com.sagag.services.domain.sag.haynespro.LabourTimeJobDto;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheJobDto;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheSmartCartDto;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheVehicleDto;

import java.util.List;
import java.util.Optional;

public interface HaynesProCacheService extends CacheService {

  /**
   * Saves HaynesPro Smart Cart from HaynesPro callback response.
   *
   */
  void saveHpSmartCart(String key, HaynesProCacheSmartCartDto hpCart);

  /**
   * Returns HaynesPro Smart Cart from user key.
   *
   */
  Optional<HaynesProCacheSmartCartDto> getHpSmartCart(String key);

  /**
   * Clears HaynesPro Smart Cart by user key.
   *
   */
  void clearSmartCart(String key);

  /**
   * Returns labour times by user key.
   *
   */
  List<LabourTimeJobDto> getLabourTimes(String key, String vehicleId, double vatRate);

  /**
   * Adds labour times by user key.
   *
   */
  void addLabourTimes(String key, String vehicleId, List<HaynesProCacheJobDto> hpJobs);

  /**
   * Saves labour times by user key.
   *
   */
  void saveLabourTimes(String key, HaynesProCacheVehicleDto vehicle, final String vehId,
      List<HaynesProCacheJobDto> hpJobs);

  /**
   * Removes labour time by user key.
   *
   */
  void removeLabourTime(String key, String vehicleId, String awNumber);

  /**
   * Clears labour times by user key.
   *
   */
  void clearLabourTimes(String key);

  @Override
  default String defName() {
    return HazelcastMaps.HAYNESPRO_MAP.name();
  }
}
