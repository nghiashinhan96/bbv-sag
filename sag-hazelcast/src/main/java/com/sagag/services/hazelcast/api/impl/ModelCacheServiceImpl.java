package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.sagag.services.domain.eshop.dto.ModelItem;
import com.sagag.services.elasticsearch.api.ModelSearchService;
import com.sagag.services.elasticsearch.domain.MakeDoc;
import com.sagag.services.elasticsearch.domain.ModelDoc;
import com.sagag.services.elasticsearch.domain.ModelRef;
import com.sagag.services.elasticsearch.query.vehicles.VehicleQueryUtils;
import com.sagag.services.hazelcast.api.ModelCacheService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Cache service for models document.
 */
@Service
@Slf4j
public class ModelCacheServiceImpl extends CacheDataProcessor implements ModelCacheService {

  @Autowired
  private ModelSearchService modelService;

  @Autowired
  private HazelcastInstance hazelcastInstance;
  
  @Override
  public Optional<ModelItem> getModelById(String modelId) {
    final IMap<String, ModelDoc> modelMap = hazelcastInstance.getMap(getCacheName());
        
    return Optional.ofNullable(modelMap.get(modelId))
        .map(ModelDoc::getRefs)
        .orElse(Collections.emptyList()).stream().findFirst()
        .map(ref -> ModelItem.builder()
                .modelId(ref.getIdModel())
                .model(ref.getModel())
                .modelDateBegin(ref.getModelDateBegin())
                .modelDateEnd(ref.getModelDateEnd())
                .sort(ref.getSort())
                .idMake(ref.getIdMake())
                .modelSeries(ref.getModelSeries())
                .modelGen(ref.getModelGen())
                .build());
  }

  @Override
  public List<ModelItem> getAllSortedModelsByMake(final Integer makeId, String vehClass,
      List<String> vehSubClass, final String yearFrom) {
    return getAllModels(makeId, vehClass, vehSubClass, yearFrom).stream()
        .map(ref -> ModelItem.builder()
            .modelId(ref.getIdModel())
            .model(ref.getModel())
            .modelDateBegin(ref.getModelDateBegin())
            .modelDateEnd(ref.getModelDateEnd())
            .sort(ref.getSort())
            .modelSeries(ref.getModelSeries())
            .modelGen(ref.getModelGen())
            .build())
        .sorted((item, compareItem) -> {
          return StringUtils.defaultString(item.getModel())
              .compareToIgnoreCase(StringUtils.defaultString(compareItem.getModel()));
        }).collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private List<ModelRef> getAllModels(final Integer makeId, final String vehClass,
      List<String> vehicleSubClass, final String yearFrom) {
    log.debug("Getting all sorted models from selected make");
    final IMap<String, ModelDoc> modelMap = hazelcastInstance.getMap(getCacheName());
    if (modelMap.isEmpty()) {
      log.warn("No Models in the cache");
      return Collections.emptyList();
    }
    List<Predicate<String, ModelDoc>> predicates = new ArrayList<>();
    if (Objects.nonNull(makeId)) {
      predicates.add(Predicates.equal("refs[any].idMake", makeId));
    }

    predicates.add(Predicates.equal("refs[any].vehicleClass", vehClass));

    if (CollectionUtils.isNotEmpty(vehicleSubClass)) {
      List<Predicate<String, MakeDoc>> predicatesForVehicleSubClass = new ArrayList<>();
      for (String subClass : vehicleSubClass) {
        String pattern = "(.*)(" + subClass + ")(.*)";
        predicatesForVehicleSubClass
            .add(Predicates.regex("refs[any].vehicleSubClass", pattern));
      }

      Predicate<String, ModelDoc> vehicleSubClassPredicatesOR =
          Predicates.or(predicatesForVehicleSubClass.toArray(new Predicate[0]));
      predicates.add(vehicleSubClassPredicatesOR);
    }

    Integer yearMonthTill = null;
    if (!StringUtils.isBlank(yearFrom)) {
      Integer yearMonthFrom = VehicleQueryUtils.getFormattedBuiltYearAndDecMonth(yearFrom);
      predicates.add(Predicates.lessEqual("refs[any].modelDateBegin", yearMonthFrom));

      yearMonthTill = VehicleQueryUtils.getFormattedBuiltYearAndJanMonth(yearFrom);
    }
    final List<ModelRef> modelRefs = modelMap
        .values(Predicates.and(predicates.toArray(new Predicate[0]))).stream()
        .flatMap(doc -> doc.getRefs().stream()).collect(Collectors.toList());
    if (yearMonthTill == null) {
      return modelRefs;
    }
    final int yearMonthTillVal = yearMonthTill.intValue();
    return modelRefs.stream()
        .filter(modelRef -> NumberUtils.compare(modelRef.getModelDateEnd(),
            NumberUtils.INTEGER_ZERO) == 0 || NumberUtils.compare(modelRef.getModelDateEnd(),
                yearMonthTillVal) >= 0).collect(Collectors.toList());
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available models from ES to Hazelcast instance");
    refreshCacheModels(modelService.getAll());
  }

  @Override
  public IMap<String, ModelDoc> refreshCacheModels(List<ModelDoc> models) {
    hazelcastInstance.getMap(getCacheName()).evictAll();
    final IMap<String, ModelDoc> modelMap = hazelcastInstance.getMap(getCacheName());
    models
        .parallelStream()
        .forEach(doc -> modelMap.put(doc.getId(), doc));
    return modelMap;
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }
}
