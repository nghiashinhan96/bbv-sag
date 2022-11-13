package com.sagag.services.hazelcast.api;

import com.sagag.eshop.service.dto.UserInfo;

/**
 * Interface to define APIs for current user caching.
 */
public interface UserCacheService {

  /**
   * Returns cached user info from id.
   * 
   * @param userId the user id
   * @return the current user logging info
   */
  UserInfo get(final Long userId);

  /**
   * Puts the session user to cache.
   * 
   * @param user the session user
   */
  void put(final UserInfo user);

  /**
   * Removes the session user from cache.
   * 
   * @param userId the user id
   */
  void remove(final Long userId);
  
  UserInfo getBySid(final String sid);
  
}
