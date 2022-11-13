package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.services.elasticsearch.api.CategoryService;
import com.sagag.services.elasticsearch.api.VehicleGenArtService;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.elasticsearch.domain.GenArt;
import com.sagag.services.elasticsearch.domain.category.GtCUPIInfo;
import com.sagag.services.hazelcast.api.CategoryCacheService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Cache service implementation class for category document.
 */
@Service
@Slf4j
public class CategoryCacheServiceImpl extends CacheDataProcessor implements CategoryCacheService {

  private static final String QUERY_GENARTS_GAID = "genarts[any].gaid";

  private static final String QUERY_GENARTS_CUPI = "genarts[any].cupis[any].cupi";

  private static final String QUERY_CATTREE_EXTERNAL_SERVICE = "externalService";

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private VehicleGenArtService vehicleGenArtService;

  @Override
  public IMap<String, CategoryDoc> getAllCacheCategories() {
    log.debug("Getting all categories from cache");
    return hazelcastInstance.getMap(getCacheName());
  }

  @Override
  public Collection<CategoryDoc> findCategoriesByGaids(List<String> gaids) {
    log.debug("Finding all categories from a list of gaids");
    final IMap<String, CategoryDoc> categories = getAllCacheCategories();
    return categories.values(Predicates.in(QUERY_GENARTS_GAID,
        gaids.toArray(new String[gaids.size()])));
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available categories from ES to Hazelcast instance");
    refreshCacheCategories(categoryService.getAll());
  }

  @Override
  public Collection<CategoryDoc> findCategoriesByVehicle(String vehicleId) {
    log.debug("Finding all categories from the given vehicle id = {}", vehicleId);
    if (StringUtils.isBlank(vehicleId)) {
      return Collections.emptyList();
    }
    final List<String> gaids = vehicleGenArtService.findGenericArticlesByVehicle(vehicleId);
    if (CollectionUtils.isEmpty(gaids)) {
      return Collections.emptyList();
    }
    return findCategoriesByGaids(gaids);
  }

  @Override
  public Page<CategoryDoc> searchCategoriesByLeafText(String leafText) {
    Assert.hasText(leafText, "The given search query must not be empty");
    return new PageImpl<>(categoryService.getCategoriesByLeaftxt(leafText));
  }

  @Override
  public IMap<String, CategoryDoc> refreshCacheCategories(List<CategoryDoc> categories) {
    hazelcastInstance.getMap(getCacheName()).evictAll();
    final IMap<String, CategoryDoc> categoryMap = getAllCacheCategories();
    categories.parallelStream().forEach(doc -> categoryMap.put(doc.getId(), doc));
    return categoryMap;
  }

  @Override
  public Collection<CategoryDoc> findCategoriesByCupis(List<String> cupis) {
    log.debug("Finding all categories from cupis");
    if (CollectionUtils.isEmpty(cupis)) {
      return Collections.emptyList();
    }
    final IMap<String, CategoryDoc> categories = getAllCacheCategories();
    return categories.values(Predicates.in(QUERY_GENARTS_CUPI,
        cupis.toArray(new String[cupis.size()])));
  }

  @Override
  public List<String> searchGenArtIdsByPartCodes(List<String> partCodes) {
    log.debug("Finding all categories from a list of partCodes");
    final IMap<String, CategoryDoc> categories = getAllCacheCategories();
    final Collection<CategoryDoc> selectedCategories =
        categories.values(Predicates.in(QUERY_GENARTS_CUPI,
            partCodes.toArray(new String[partCodes.size()])));
    return selectedCategories.stream()
        .flatMap(category -> category.getGenarts().stream())
        .filter(genartContainCupi(partCodes)).map(GenArt::getGaid)
        .collect(Collectors.toList());
  }

  private Predicate<GenArt> genartContainCupi(List<String> cupis) {
    return genart -> {
      List<String> cupiCodes = CollectionUtils.emptyIfNull(genart.getCupis()).stream()
          .map(GtCUPIInfo::getCupi).collect(Collectors.toList());
      return cupiCodes.stream().anyMatch(cupis::contains);
    };
  }

  @Override
  public Collection<CategoryDoc> findCategoriesByGaIdsAndExternalService(List<String> gaids,
      String externalService) {
    if (CollectionUtils.isEmpty(gaids) || StringUtils.isBlank(externalService)) {
      return Collections.emptyList();
    }
    final IMap<String, CategoryDoc> categories = getAllCacheCategories();
    return categories.values(Predicates.and(
        Predicates.in(QUERY_GENARTS_GAID, gaids.toArray(new String[gaids.size()])),
        Predicates.in(QUERY_CATTREE_EXTERNAL_SERVICE, externalService)));
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }
}
