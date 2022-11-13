package com.sagag.services.common.cache;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CacheConstants {

  /** time to live for cache map is 4 hours = 14400s. */
  public static final int TTL_4_HOURS = 14400;

  /** time to live for cache map is 8 hours = 28800s. */
  public static final int TTL_8_HOURS = 28800;

  /** time to live for cache map is forever = 0s. */
  public static final int TTL_FOREVER = 0;

  /** time to live for cache map is 1 hour = 3600s. */
  public static final int TTL_1_HOUR = 3600;

}
