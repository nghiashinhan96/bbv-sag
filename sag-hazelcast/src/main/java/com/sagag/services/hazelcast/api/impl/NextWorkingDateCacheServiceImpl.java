package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.eshop.service.api.OpeningDaysService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.availability.backorder.BackOrderUtils;
import com.sagag.services.ax.utils.AxBranchUtils;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.hazelcast.api.NextWorkingDateCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation class for next working date caching service.
 */
@Service
public class NextWorkingDateCacheServiceImpl implements NextWorkingDateCacheService {

  private static final String MAP_NAME = HazelcastMaps.NEXT_WORKING_DATE_MAP.name();

  @Autowired
  private HazelcastInstance cacheInstance;

  @Autowired
  private OpeningDaysService openingDaysService;

  @Override
  public void clear(final UserInfo user) {
    final IMap<String, NextWorkingDates> nextWorkingDateMap = cacheInstance.getMap(MAP_NAME);

    if (MapUtils.isEmpty(nextWorkingDateMap)) {
      return;
    }

    final Set<String> keyBelongToUser = nextWorkingDateMap.keySet().stream()
        .filter(key -> key.startsWith(user.key())).collect(Collectors.toSet());

    if (keyBelongToUser.isEmpty()) {
      return;
    }
    keyBelongToUser.stream().forEach(nextWorkingDateMap::remove);
  }

  @Override
  @Transactional
  public void update(final UserInfo user, final String branchId) {
    final IMap<String, NextWorkingDates> nextWorkingDateMap = cacheInstance.getMap(MAP_NAME);
    if (Objects.isNull(nextWorkingDateMap)) {
      return;
    }
    nextWorkingDateMap.put(createNextWorkingDateKey(user.key(), branchId),
        getNextWorkingDate(user.getSupportedAffiliate(), branchId));
  }

  @Override
  @Transactional
  public NextWorkingDates get(final UserInfo user, final String branchId) {
    final IMap<String, NextWorkingDates> nextWorkingDateMap = cacheInstance.getMap(MAP_NAME);

    final String userKey = user.key();
    final String key = createNextWorkingDateKey(userKey, branchId);

    if (MapUtils.isEmpty(nextWorkingDateMap)
        || Objects.isNull(nextWorkingDateMap.get(createNextWorkingDateKey(userKey, branchId)))) {
      nextWorkingDateMap.put(key, getNextWorkingDate(user.getSupportedAffiliate(), branchId));
    }
    return nextWorkingDateMap.get(key);
  }

  private NextWorkingDates getNextWorkingDate(final SupportedAffiliate affiliate, final String pickupBranchId) {
    final Date currentDate = Calendar.getInstance().getTime();
    final int backOrderDays = BackOrderUtils.getBackOrderDays(affiliate);
    return NextWorkingDates.builder()
        .noBackorderDate(openingDaysService.getDateLaterFromToday(affiliate, pickupBranchId, 0).orElse(currentDate))
        .backorderDate(openingDaysService.getDateLaterFromToday(affiliate, pickupBranchId, backOrderDays)
            .orElse(currentDate))
        .build();
  }

  /**
   * Build the key for map with pattern: userKey_branchId
   *
   * @param userKey
   * @param branchId
   * @return
   */
  private String createNextWorkingDateKey(final String userKey, final String branchId) {
    return StringUtils.join(Arrays.asList(userKey, AxBranchUtils.getDefaultBranchIfNull(branchId)),
        SagConstants.UNDERSCORE);
  }

}
