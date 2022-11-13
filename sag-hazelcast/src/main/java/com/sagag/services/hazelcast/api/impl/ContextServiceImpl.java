package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.utils.AxBranchUtils;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.eshop.dto.ContextDto;
import com.sagag.services.domain.eshop.dto.EshopBasketDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.api.VinOrderCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.hazelcast.domain.user.EshopContextType;
import com.sagag.services.hazelcast.user_context.IContextMapper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation class for Context service.
 */
@Service
public class ContextServiceImpl implements ContextService {

  private static final String SEARCH_RESULT_MAP_NAME = HazelcastMaps.SEARCH_RESULT_MAP.name();

  private static final String CONTEXT_CACHE_MAP_NAME = HazelcastMaps.ESHOP_CONTEXT_CACHE.name();

  private static final String CONTEXT_MAP_NAME = HazelcastMaps.CONTEXT_MAP.name();

  private static final String SEARCH_RESULT_SCOPE = "SEARCH_RESULT";

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Autowired
  private CouponCacheService couponCacheService;

  @Autowired
  private VinOrderCacheService vinOrderCacheService;

  @Autowired
  private List<IContextMapper> contextMappers;

  @Override
  public EshopContext getEshopContext(final String key) {
    final IMap<String, EshopContext> eshopContextCache = hazelcastInstance.getMap(CONTEXT_MAP_NAME);
    EshopContext context = eshopContextCache.get(key + CONTEXT_CACHE_MAP_NAME);
    context = context != null ? context : new EshopContext();
    return context;
  }

  @Override
  public void updateByContextType(final UserInfo user, final ContextDto contextDto,
      final EshopContextType contextType) {
    final String key = user.key();
    final IMap<String, EshopContext> eshopContextCache = hazelcastInstance.getMap(CONTEXT_MAP_NAME);
    final EshopContext context =
        eshopContextCache.getOrDefault(key + CONTEXT_CACHE_MAP_NAME, new EshopContext());
    ListUtils.emptyIfNull(contextMappers).stream()
    .filter(ctxMapper -> ctxMapper.type() == contextType)
    .forEach(ctxMapper -> ctxMapper.map(user, context, contextDto));

    eshopContextCache.put(key + CONTEXT_CACHE_MAP_NAME, context);
  }

  @Override
  public void clearEshopContext(final String key) {
    // clear coupon cached
    couponCacheService.clearCache(key);
    vinOrderCacheService.clearSearchCount(key);
  }

  @Override
  public void storeCachedArticlesResult(final ArticleFilteringResponseDto cachedArticlesResult) {
    if (cachedArticlesResult == null) {
      return;
    }
    final String contextId = cachedArticlesResult.getContextKey() + SEARCH_RESULT_MAP_NAME;
    final IMap<String, EshopContext> eshopContextCache = hazelcastInstance.getMap(CONTEXT_MAP_NAME);
    EshopContext context = eshopContextCache.get(contextId);
    context = context != null ? context : new EshopContext();
    context.setCachedArticlesResult(cachedArticlesResult);
    eshopContextCache.put(contextId, context);
  }

  @Override
  public ArticleFilteringResponseDto getCachedArticlesResult(String contextKey) {
    final IMap<String, EshopContext> eshopContextCache = hazelcastInstance.getMap(CONTEXT_MAP_NAME);
    final String contextId = contextKey + SEARCH_RESULT_MAP_NAME;
    EshopContext context = eshopContextCache.get(contextId);
    context = context != null ? context : new EshopContext();
    return context.getCachedArticlesResult();
  }

  @Override
  public void clearContext(final String key, String scope, String contextKey) {
    if (SEARCH_RESULT_SCOPE.equalsIgnoreCase(scope)) {
      hazelcastInstance.getMap(CONTEXT_MAP_NAME).remove(contextKey + SEARCH_RESULT_MAP_NAME);
    } else {
      clearEshopContext(key);
    }
  }

  @Override
  public String getPickupBranchId(final UserInfo user) {
    return AxBranchUtils
        .getDefaultBranchIfNull(getPickupBranchId(user.key(), user.getDefaultBranchId()));
  }

  @Override
  public EshopContext updateOrderConditions(final UserInfo user,
      final EshopBasketContext orderConditions) {
    updateByContextType(
        user, ContextDto.builder()
            .eshopBasketDto(SagBeanUtils.map(orderConditions, EshopBasketDto.class)).build(),
        EshopContextType.BASKET_CONTEXT);
    return EshopContext.builder().eshopBasketContext(orderConditions).build();
  }

  @Override
  public EshopBasketContext getBasketContext(String key) {
    return getEshopContext(key).getEshopBasketContext();
  }

  private String getPickupBranchId(final String userKey, final String defaultPickupBranchId) {
    final EshopBasketContext basketContext = getBasketContext(userKey);
    if (Objects.isNull(basketContext)) {
      return defaultPickupBranchId;
    }
    final CustomerBranch pickupBranch = basketContext.getPickupBranch();
    if (Objects.isNull(pickupBranch)) {
      return defaultPickupBranchId;
    }
    return pickupBranch.getBranchId();
  }

  @Override
  @LogExecutionTime
  public Optional<VehicleDto> getVehicleInContext(String userKey) {
    final List<VehicleDto> vehs = getEshopContext(userKey).getSelectedVehicleDocs();
    if (CollectionUtils.isEmpty(vehs)) {
      return Optional.empty();
    }
    return vehs.stream().findFirst();
  }
}
