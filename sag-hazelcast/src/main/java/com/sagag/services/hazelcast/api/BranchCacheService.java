package com.sagag.services.hazelcast.api;


import com.sagag.services.domain.sag.external.CustomerBranch;

import java.util.List;

/**
 * Latest branch infos caching expired in 1 hour and after user session ends.
 */
public interface BranchCacheService {

  /**
   * Gets cached branches.
   *
   * @param companyName the company name
   * @return list of { @link CustomerBranch }
   */
  List<CustomerBranch> getCachedBranches(String companyName);

  /**
   * Clears cache for specific companyName.
   *
   * @param companyName the companyName
   */
  void clearCache(String companyName);

}
