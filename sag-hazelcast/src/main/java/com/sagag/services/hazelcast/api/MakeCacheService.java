package com.sagag.services.hazelcast.api;

import com.hazelcast.core.IMap;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.dto.MakeItem;
import com.sagag.services.elasticsearch.domain.MakeDoc;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface to define services for make caching.
 */
public interface MakeCacheService extends CacheService {

  /**
   * Returns all makes from cache.
   *
   * @return all makes
   */
  List<MakeItem> getAllSortedMakes(String vehType, SupportedAffiliate affiliate);

  /**
   * Returns all makes from cache matching with vehicle class and sub-class
   *
   * @return all makes with conditions
   */
  List<MakeItem> getMakesByVehicleClassAndSubClass(String vehClass, List<String> vehSubClass,
      SupportedAffiliate affiliate);

  /**
   * Finds the make item from gtmotive make code (gt_umc).
   *
   * @param makeCode the make code from gtmotive
   * @return the make item in ES
   */
  Optional<MakeItem> findMakeItemByCode(final String makeCode);

  /**
   * Returns the found make docs by make ids.
   *
   * @param makeIds the list of make ids.
   * @return the list of {@link MakeDoc}
   */
  Map<String, String> findMakesByIds(final List<String> makeIds);

  @Override
  default String defName() {
    return HazelcastMaps.MAKE_MAP.name();
  }

  /**
   * Refreshes a list of make docs.
   *
   * @param makes the make docs from Elasticsearch
   * @return a map of makes
   */
  IMap<String, MakeDoc> refreshCacheMakes(final List<MakeDoc> makes);

  /**
   * Returns makeIds by makes
   *
   * @param makes
   * @return
   */
  Map<String, String> findMakeIdsByMakes(List<String> makes);
}
