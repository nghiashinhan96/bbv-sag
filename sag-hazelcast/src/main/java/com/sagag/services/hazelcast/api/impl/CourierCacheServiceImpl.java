package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.article.api.CourierExternalService;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.sag.external.Courier;
import com.sagag.services.hazelcast.api.CourierCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.MapUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * Implementation class for couier cache service.
 */
@Service
@SbProfile
@Slf4j
public class CourierCacheServiceImpl implements CourierCacheService {

  @Autowired
  private HazelcastInstance cacheInstance;

  @Qualifier("axServiceImpl")
  @Autowired
  private CourierExternalService courierExternalService;

  @Override
  public List<Courier> getCachedCouriers(String companyName) {
    Assert.notNull(companyName, "The given companyName must not be null");

    final List<Courier> courieres = get(companyName);
    if (courieres != null) {
      log.debug("Return cached courieres {}", courieres);
      return courieres;
    }

    final List<Courier> updatedCouriers = courierExternalService.getCouriers(companyName);
    put(companyName, updatedCouriers);

    return get(companyName);
  }

  @Override
  public void clearCache(String companyName) {

    final IMap<String, List<Courier>> courieresMap = getCourierHazelcastInstanceMap();
    if (Objects.isNull(courieresMap)) {
      return;
    }
    courieresMap.remove(companyName);
  }

  private List<Courier> get(String companyName) {
    final IMap<String, List<Courier>> courieresMap = getCourierHazelcastInstanceMap();
    if (MapUtils.isEmpty(courieresMap)) {
      return null;
    }
    return courieresMap.get(companyName);
  }

  private void put(String companyName, List<Courier> couriers) {
    Asserts.notNull(companyName, "companyName must be not null");
    final IMap<String, List<Courier>> courieresMap = getCourierHazelcastInstanceMap();
    if (Objects.isNull(courieresMap)) {
      return;
    }
    courieresMap.set(companyName, couriers);
  }

  private IMap<String, List<Courier>> getCourierHazelcastInstanceMap() {
    return cacheInstance.getMap(HazelcastMaps.SESSION_COURIER_MAP.name());
  }

}
