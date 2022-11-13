package com.sagag.services.hazelcast.globalsetting;

import com.sagag.services.hazelcast.api.EshopGlobalSettingCacheService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class GtmotiveGlobalSettingPredicates {

  private static final String VIN_SEARCH_FOR_C4S_SETTING_TYPE = "GTMOTIVE";

  private static final String VIN_SEARCH_FOR_C4S_SETTING_CODE = "vin_search_for_c4s";

  @Autowired
  private EshopGlobalSettingCacheService connectGlobalSettingService;

  public <T> Predicate<T> andPredicates(List<Predicate<T>> predicates) {
    Predicate<T> firstPredicate = i -> connectGlobalSettingService.isAllowedToUse(
        VIN_SEARCH_FOR_C4S_SETTING_TYPE, VIN_SEARCH_FOR_C4S_SETTING_CODE);
    if (CollectionUtils.isEmpty(predicates)) {
      return firstPredicate;
    }

    for (Predicate<T> predicate : predicates) {
      firstPredicate = firstPredicate.and(predicate);
    }
    return firstPredicate;
  }

}
