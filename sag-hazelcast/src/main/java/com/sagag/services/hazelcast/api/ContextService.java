package com.sagag.services.hazelcast.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.eshop.dto.ContextDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.hazelcast.domain.user.EshopContextType;

import java.util.Optional;

/**
 * Context Interface to define the API to the cache.
 */
public interface ContextService {

  /**
   * Returns Eshop context from user.
   *
   * @param key the cache user key
   * @return the eshop context
   */
  EshopContext getEshopContext(final String key);

  /**
   * Updates the application context.
   *
   * @param user the user who request to update
   * @param contextDto the context data
   * @param contextType the context type
   */
  void updateByContextType(final UserInfo user, final ContextDto contextDto,
      final EshopContextType contextType);

  /**
   * Clears application context.
   *
   * @param key the unique user key.
   */
  void clearEshopContext(final String key);

  void storeCachedArticlesResult(ArticleFilteringResponseDto tyreArticleFilteringResponse);

  /**
   * Returns the cached articles result by context key.
   *
   * @param contextKey the context key
   * @return the object of {@link ArticleFilteringResponseDto}
   */
  ArticleFilteringResponseDto getCachedArticlesResult(String contextKey);

  /**
   * Clears application context in all contexts.
   *
   * @param key the unique user key.
   * @param scope the scope
   * @param contextId the context id
   */
  void clearContext(final String key, String scope, String contextId);

  /**
   * Returns pickup branch from context
   *
   * @param user the user who request to update
   * @return pickup branch from context
   */
  String getPickupBranchId(final UserInfo user);

  /**
   * Returns Eshop Basket Context from user.
   *
   * @param key the cache user key
   * @return the Eshop Basket Context
   */
  EshopBasketContext getBasketContext(final String key);

  /**
   * Updates order condition to cache for user
   *
   * @param user the user who request to update
   * @param orderConditions order conditions
   * @return the object of {@link EshopContext}
   */
  EshopContext updateOrderConditions(final UserInfo user, final EshopBasketContext orderConditions);

  Optional<VehicleDto> getVehicleInContext(String userKey);
}
