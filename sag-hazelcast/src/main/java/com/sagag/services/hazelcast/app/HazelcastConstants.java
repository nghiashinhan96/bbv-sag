package com.sagag.services.hazelcast.app;

import com.sagag.services.common.cache.CacheConstants;

import lombok.experimental.UtilityClass;

/**
 * Class to define the cache constants.
 */
@UtilityClass
public class HazelcastConstants {

  /** time to live for cache map is 8 hours. */
  public static final int TTL_8_HOURS = CacheConstants.TTL_8_HOURS;

  /** time to live for cache map is forever. */
  public static final int TTL_FOREVER = CacheConstants.TTL_FOREVER;

  /** time to live for cache map is 1 hour. */
  public static final int TTL_1_HOUR = CacheConstants.TTL_1_HOUR;

  /** time to live for cache map is 4 hour. */
  public static final int TTL_4_HOURS = CacheConstants.TTL_4_HOURS;
}
