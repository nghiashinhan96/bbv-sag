package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation class for Coupon service.
 */
@Service
public class CouponCacheServiceImpl implements CouponCacheService {

  private static final String MAP_NAME = HazelcastMaps.COUPON_USE_LOG.name();

  @Autowired
  HazelcastInstance hazelcast;

  @Override
  public void add(CouponUseLog couponUseLog, String key) {
    final IMap<String, CouponUseLog> couponUseLogCache = hazelcast.getMap(MAP_NAME);
    couponUseLogCache.put(key + MAP_NAME, couponUseLog);
  }

  @Override
  public void clearCache(String key) {
    hazelcast.getMap(MAP_NAME).evict(key + MAP_NAME);
  }

  @Override
  public CouponUseLog getCouponUseLog(String key) {
    final IMap<String, CouponUseLog> couponUseLogCache = hazelcast.getMap(MAP_NAME);
    CouponUseLog couponUseLog = couponUseLogCache.get(key + MAP_NAME);
    return couponUseLog == null ? new CouponUseLog() : couponUseLog;
  }
}
