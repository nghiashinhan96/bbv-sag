package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.hazelcast.api.OatesAdditionalRecommendationsCacheService;
import com.sagag.services.oates.api.OatesAdditionalRecommendationsService;
import com.sagag.services.oates.dto.RecommendProductDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Cache service implementation for generic article.
 */
@Service
@Slf4j
public class OatesAdditionalRecommendationsCacheServiceImpl extends CacheDataProcessor
    implements OatesAdditionalRecommendationsCacheService {

  private static final String KEY_URI = "resources[any].uri";
  private static final String KEY_NAME = "name";

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Autowired
  private OatesAdditionalRecommendationsService oatesAddRecommendationsService;

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available recommend products to Hazelcast instance");
    refreshCacheAdditionalRecommendation(
      oatesAddRecommendationsService.getAllRecommendProducts());
  }

  @Override
  public IMap<String, RecommendProductDto> refreshCacheAdditionalRecommendation(
      List<RecommendProductDto> recommendProducts) {
    hazelcastInstance.getMap(getCacheName()).evictAll();
    final IMap<String, RecommendProductDto> recommendProductMap =
        hazelcastInstance.getMap(getCacheName());
    recommendProductMap.addIndex(KEY_URI, true);
    recommendProducts.parallelStream().forEach(doc -> recommendProductMap.put(doc.getId(), doc));
    return recommendProductMap;
  }

  @Override
  public Map<String, RecommendProductDto> searchAdditionalRecommendationByArtId(
    final String articleId) {
    log.debug("Searching the recommend product from an article id = {} ", articleId);
    return searchRecommendProduct(articleId);
  }

  @Override
  public Map<String, RecommendProductDto> searchByNames(final List<String> articleNames) {
    return searchRecommendProductByName(articleNames);
  }

  private Map<String, RecommendProductDto> searchRecommendProduct(final String articleId) {
    final IMap<String, RecommendProductDto> recommendProductCacheMap =
        hazelcastInstance.getMap(getCacheName());
    final Map<String, RecommendProductDto> recommendProduct = new HashMap<>();
    recommendProductCacheMap
        .values(Predicates.ilike(KEY_URI,
            StringUtils.join(SagConstants.LIKE_CHAR, articleId, SagConstants.LIKE_CHAR)))
        .stream().forEach(doc -> recommendProduct.put(doc.getName(), doc));
    return recommendProduct;
  }

  private Map<String, RecommendProductDto> searchRecommendProductByName(
      final List<String> articleNames) {
    final IMap<String, RecommendProductDto> recommendProductCacheMap =
        hazelcastInstance.getMap(getCacheName());
    final Map<String, RecommendProductDto> recommendProduct = new HashMap<>();
    recommendProductCacheMap
        .values(Predicates.in(KEY_NAME, articleNames.toArray(new String[articleNames.size()])))
        .stream().forEach(doc -> recommendProduct.put(doc.getName(), doc));
    return recommendProduct;
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

}
