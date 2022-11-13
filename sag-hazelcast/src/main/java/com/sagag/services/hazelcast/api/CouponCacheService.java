package com.sagag.services.hazelcast.api;

import com.sagag.eshop.repo.entity.CouponUseLog;

/**
 * Interface to define APIs for coupon service.
 */
public interface CouponCacheService {

  /**
   * Add coupon usage to cache.
   * 
   * @param couponUseLog the coupon use to add
   * @param key the cache key to add the coupon use
   */
  void add(CouponUseLog couponUseLog, String key);

  /**
   * Returns coupon use log from key.
   * 
   * @param key the cache key to get the coupon usage
   * @return the {@link CouponUseLog}
   */
  CouponUseLog getCouponUseLog(String key);

  /**
   * Clears the coupon cache.
   * 
   * @param key the key to clear
   */
  void clearCache(String key);
}
