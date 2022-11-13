package com.sagag.services.hazelcast.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;

/**
 * Interface to define APIs for next working date caching.
 */
public interface NextWorkingDateCacheService {

  /**
   * Returns the next working date from cache belongs to user.
   *
   * @param user the user info
   * @param branchId
   * @return the the next working dates belongs to user
   */
  NextWorkingDates get(final UserInfo user, String branchId);

  /**
   * Updates the next working date with the same hour to cache.
   *
   * @param user the user info
   * @param branchId branchId uses to get next working date
   */
  void update(final UserInfo user, final String branchId);

  /**
   * Removes the next working date from cache belongs to user.
   *
   * @param user the user info
   */
  void clear(final UserInfo user);
}
