package com.sagag.services.ivds.oates;

import com.sagag.services.hazelcast.api.CacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.oates.dto.OatesApplicationDto;

import java.util.List;

public interface OatesCacheService extends CacheService {

  /**
   * Saves the OATES applications in cache by userKey.
   *
   * @param userKey
   * @param applications
   */
  void saveOatesApplications(String userKey, List<OatesApplicationDto> applications);

  /**
   * Returns the list of OATES applications by userKey.
   *
   * @param userKey
   * @return the list of <code>OatesApplicationDto</code>
   */
  List<OatesApplicationDto> getOatesApplicationsByUserKey(String userKey);

  @Override
  default String defName() {
    return HazelcastMaps.OATES_VEHICLE_MAP.name();
  }

}
