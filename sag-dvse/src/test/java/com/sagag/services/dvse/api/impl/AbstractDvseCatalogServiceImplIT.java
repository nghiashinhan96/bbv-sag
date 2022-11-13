package com.sagag.services.dvse.api.impl;

import java.util.Objects;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.eshop.repo.api.DeliveryTypesRepository;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.hazelcast.domain.user.EshopContext;

public abstract class AbstractDvseCatalogServiceImplIT {

  private static final String CONTEXT_CACHE_MAP_NAME = HazelcastMaps.ESHOP_CONTEXT_CACHE.name();

  private static final String CONTEXT_MAP_NAME = HazelcastMaps.CONTEXT_MAP.name();

  private static final int TOUR_DELIVERY = 2;

  @Autowired
  protected UserCacheService userCacheService;

  @Autowired
  protected CustomerExternalService custExtService;

  @Autowired
  private DeliveryTypesRepository deliveryTypeRepo;

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Before
  public abstract void init();

  protected void initializeUserAppContext(final String userKey) {
    final EshopContext userContext = getUserAppContext(userKey);
    // initialize delivery type in cache so that the dvse service will take to get the article
    // information. The delivery type should not be null for any user session context.
    final DeliveryTypeDto deliveryType = SagBeanUtils
        .map(deliveryTypeRepo.findById(TOUR_DELIVERY).orElse(null), DeliveryTypeDto.class);
    userContext.getEshopBasketContext().setDeliveryType(deliveryType);
    updateUserAppContext(userKey, userContext);
  }

  private EshopContext getUserAppContext(final String userKey) {
    final IMap<String, EshopContext> contextCache = hazelcastInstance.getMap(CONTEXT_MAP_NAME);
    final EshopContext context = contextCache.get(userKey + CONTEXT_CACHE_MAP_NAME);
    if (Objects.isNull(context)) {
      return new EshopContext();
    }
    return context;
  }

  private void updateUserAppContext(final String userKey, final EshopContext context) {
    final IMap<String, EshopContext> contextCache = hazelcastInstance.getMap(CONTEXT_MAP_NAME);
    contextCache.put(userKey + CONTEXT_CACHE_MAP_NAME, context);
  }

}
