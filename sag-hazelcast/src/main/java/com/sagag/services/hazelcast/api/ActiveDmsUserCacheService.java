package com.sagag.services.hazelcast.api;

import com.sagag.services.hazelcast.model.ActiveUser;

/**
 * Interface to define APIs for active user caching.
 */
public interface ActiveDmsUserCacheService {

  /**
   * Returns cached active user info from id.
   *
   */
  ActiveUser get(Long userId);

  /**
   * Puts the active user to cache.
   */
  void put(ActiveUser user);

  /**
   * Removes the active user from cache.
   */
  void remove(Long userId);

  /**
   * Checks if the user is already active.
   */
  boolean contains(Long userId);
}
