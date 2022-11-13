package com.sagag.services.hazelcast.api;

import com.hazelcast.core.IMap;
import com.sagag.services.domain.eshop.dto.ModelItem;
import com.sagag.services.elasticsearch.domain.ModelDoc;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import java.util.List;
import java.util.Optional;

/**
 * Interface to define services for model caching.
 */
public interface ModelCacheService extends CacheService {

  /**
   * Returns all sorted models from make id.
   *
   * @param makeId the make id that the models belong.
   * @param vehClass vehicle class
   * @return vehicle sub-class
   * @param yearFrom the year from of vehicle
   * @return a list of model
   */
  List<ModelItem> getAllSortedModelsByMake(Integer makeId, String vehClass,
      List<String> vehSubClass, String yearFrom);

  /**
   * Returns model from model Id
   *
   * @param makeId the make id that the models belong.
   * @param yearFrom the year from of vehicle
   * @return a optional of model
   */
  Optional<ModelItem> getModelById(String modelId);

  @Override
  default String defName() {
    return HazelcastMaps.MODEL_MAP.name();
  }

  /**
   * Refreshes models in cache.
   *
   * @return the map of models
   */
  IMap<String, ModelDoc> refreshCacheModels(List<ModelDoc> models);
}
