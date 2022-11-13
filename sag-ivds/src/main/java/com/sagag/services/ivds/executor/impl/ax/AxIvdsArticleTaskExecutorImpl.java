package com.sagag.services.ivds.executor.impl.ax;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.executor.ArticleSearchCriteria.ArticleSearchCriteriaBuilder;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.hazelcast.api.BranchCacheService;
import com.sagag.services.hazelcast.api.DeliveryProfileCacheService;
import com.sagag.services.hazelcast.api.ExternalVendorCacheService;
import com.sagag.services.hazelcast.api.NextWorkingDateCacheService;
import com.sagag.services.hazelcast.api.TourTimeCacheService;
import com.sagag.services.ivds.executor.impl.AbstractIvdsArticleTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The common component to update business data for search result in IVDS module.
 */
@Component
@AxProfile
public class AxIvdsArticleTaskExecutorImpl extends AbstractIvdsArticleTaskExecutor {

  @Autowired
  private NextWorkingDateCacheService nextWorkingDateCacheService;
  @Autowired
  private ExternalVendorCacheService externalVendorCacheService;
  @Autowired
  private DeliveryProfileCacheService deliveryProfileCacheService;
  @Autowired(required = false)
  private BranchCacheService branchCacheService;
  @Autowired
  private TourTimeCacheService tourTimeCacheService;

  @Override
  protected void loadAvailabilitiesInfo(ArticleSearchCriteriaBuilder builder, UserInfo user,
      String pickupBranchId) {
    builder.nextWorkingDate(nextWorkingDateCacheService.get(user, pickupBranchId));
    builder.externalVendors(externalVendorCacheService.findAll());
    builder.deliveryProfiles(deliveryProfileCacheService.findAll());
    if (branchCacheService != null) {
      builder.companyBranches(branchCacheService.getCachedBranches(user.getCompanyName()));
    }
    builder.customerTourTimes(tourTimeCacheService.searchTourTimesByCustNr(user.getCustNr()));
  }

}
