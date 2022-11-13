package com.sagag.services.hazelcast.provider;

public interface CacheDataProvider {

  /**
   * Refreshes all initial necessary cache data for Connect Shop.
   *
   */
  void refreshCacheData();

}
