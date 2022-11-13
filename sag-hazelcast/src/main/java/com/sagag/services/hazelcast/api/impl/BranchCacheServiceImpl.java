package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.article.api.BranchExternalService;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.hazelcast.api.BranchCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementation class for branch cache service.
 */
@Service
@Slf4j
@SbProfile
public class BranchCacheServiceImpl implements BranchCacheService {

  @Autowired
  private HazelcastInstance cacheInstance;

  @Autowired
  @Qualifier("axServiceImpl")
  private BranchExternalService branchExternalService;

  @Override
  public List<CustomerBranch> getCachedBranches(String companyName) {
    Assert.notNull(companyName, "The given companyName must not be null");

    final List<CustomerBranch> branches = get(companyName);
    if (branches != null) {
      log.debug("Return cached branches {}", branches);
      return branches;
    }

    final List<CustomerBranch> updatedBranches = branchExternalService.getBranches(companyName);

    put(companyName, updatedBranches);
    return get(companyName);
  }

  @Override
  public void clearCache(String companyName) {

    final IMap<String, List<CustomerBranch>> branchesMap = getBranchHazelcastInstanceMap();
    if (Objects.isNull(branchesMap)) {
      return;
    }
    branchesMap.remove(companyName);
  }

  private List<CustomerBranch> get(String companyName) {
    final IMap<String, List<CustomerBranch>> branchesMap = getBranchHazelcastInstanceMap();
    if (MapUtils.isEmpty(branchesMap)) {
      return null;
    }
    return branchesMap.get(companyName);
  }

  private void put(String companyName, List<CustomerBranch> customerInfo) {
    Asserts.notNull(companyName, "companyName must be not null");
    final IMap<String, List<CustomerBranch>> branchesMap = getBranchHazelcastInstanceMap();
    if (Objects.isNull(branchesMap)) {
      return;
    }
    branchesMap.set(companyName, customerInfo);
  }

  private IMap<String, List<CustomerBranch>> getBranchHazelcastInstanceMap() {
    return cacheInstance.getMap(HazelcastMaps.SESSION_BRANCH_MAP.name());
  }

}
