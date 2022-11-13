package com.sagag.services.hazelcast.api;

import com.hazelcast.core.IMap;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.oates.dto.RecommendProductDto;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Interface to define services for generic article caching.
 */
public interface OatesAdditionalRecommendationsCacheService extends CacheService {

  @Override
  default String defName() {
    return HazelcastMaps.ADDITIONAL_RECOMMENDATIONS_MAP.name();
  }

  @Override
  default String getCacheName() {
    return getCacheName(Locale.GERMAN);
  }

  /**
   * Refreshes the cache with a list of additional recommendation.
   *
   * @return the {@link IMap}
   */
  IMap<String, RecommendProductDto> refreshCacheAdditionalRecommendation(
      List<RecommendProductDto> recommendProducts);

  /**
   * Returns additional recommendation by article id.
   *
   * @param articleId
   * @return the map of recommend product.
   */
  Map<String, RecommendProductDto> searchAdditionalRecommendationByArtId(String articleId);

  Map<String, RecommendProductDto> searchByNames(List<String> articleNames);

}
