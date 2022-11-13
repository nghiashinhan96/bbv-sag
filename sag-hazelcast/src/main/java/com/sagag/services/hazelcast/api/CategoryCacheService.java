package com.sagag.services.hazelcast.api;

import com.hazelcast.core.IMap;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

/**
 * Interface to define services for category caching.
 */
public interface CategoryCacheService extends CacheService {

  /**
   * Returns a map containing all categories.
   *
   * @return all categories
   */
  IMap<String, CategoryDoc> getAllCacheCategories();

  /**
   * Returns all categories belonging to the generic articles.
   *
   * @param gaids the list of generic articles ids
   * @return a collection of categories
   */
  Collection<CategoryDoc> findCategoriesByGaids(final List<String> gaids);

  /**
   * Returns all categories belonging to the vehicle.
   *
   * @param vehicleId the vehicle id
   * @return a collection of categories
   */
  Collection<CategoryDoc> findCategoriesByVehicle(String vehicleId);

  /**
   * Returns the searched categories by leaf-text.
   *
   * @param leafText
   * @return a page of categories
   */
  Page<CategoryDoc> searchCategoriesByLeafText(String leafText);

  /**
   * Returns a map containing all refesh categories.
   *
   * @param categories categories to refresh
   * @return all cache refresh categories
   */
  IMap<String, CategoryDoc> refreshCacheCategories(final List<CategoryDoc> categories);

  /**
   * Returns categories by cupis.
   * @param cupis the cupi codes
   * @return a collection of categories
   */
  Collection<CategoryDoc> findCategoriesByCupis(List<String> cupis);

  /**
   * Returns the generic article id list by part code list.
   *
   * @param partCodes the list of part codes
   * @return the list of generic article id
   */
  List<String> searchGenArtIdsByPartCodes(List<String> partCodes);

  /**
   * Returns the collection of categories by list of gaids and external services.
   *
   * @param gaids
   * @param externalService
   * @return the collection of categories
   */
  Collection<CategoryDoc> findCategoriesByGaIdsAndExternalService(List<String> gaids,
      String externalService);

  @Override
  default String defName() {
    return HazelcastMaps.CATEGORY_MAP.name();
  }
}
