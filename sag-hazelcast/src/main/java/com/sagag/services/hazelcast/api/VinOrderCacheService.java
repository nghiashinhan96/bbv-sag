package com.sagag.services.hazelcast.api;


/**
 * Interface for VIN order cache service.
 */
public interface VinOrderCacheService {

  /**
   * Increases the search count.
   * 
   * @param key the cache user unique key
   */
  void increaseSearchCount(final String key);

  /**
   * Clears the search count for a specific key.
   * 
   * @param key the key to clear
   */
  void clearSearchCount(String key);

  /**
   * Returns the VIN search counts.
   * 
   * @param key the cache key to get
   * @return the count number
   */
  Integer getSearchCount(String key);
}
