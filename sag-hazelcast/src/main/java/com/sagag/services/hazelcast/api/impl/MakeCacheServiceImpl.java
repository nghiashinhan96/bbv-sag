package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.dto.MakeItem;
import com.sagag.services.elasticsearch.api.MakeSearchService;
import com.sagag.services.elasticsearch.domain.MakeDoc;
import com.sagag.services.elasticsearch.domain.MakeRef;
import com.sagag.services.hazelcast.api.MakeCacheService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Make cache service implementation class.
 */
@Service
@Slf4j
public class MakeCacheServiceImpl extends CacheDataProcessor implements MakeCacheService {

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Autowired
  private MakeSearchService makeService;

  @Override
  public List<MakeItem> getAllSortedMakes(String vehType, SupportedAffiliate affiliate) {
    final Optional<String> affliate = Optional.ofNullable(affiliate)
        .map(SupportedAffiliate::getEsShortName);
    return getAllMakes(vehType).stream()
        .map(ref -> buildMakeItem(ref, affliate))
      .sorted(topAffilateComparator().thenComparing(alphabetComparator()))
      .collect(Collectors.toList());
  }

  private static Comparator<MakeItem> alphabetComparator() {
    return (i1, i2) -> StringUtils.defaultString(i1.getMake())
        .compareToIgnoreCase(StringUtils.defaultString(i2.getMake()));
  }

  private static Comparator<MakeItem> topAffilateComparator() {
    return (i1, i2) -> BooleanUtils.compare(i2.isTop(),
        i1.isTop());
  }

  @SuppressWarnings("unchecked")
  private List<MakeRef> getAllMakes(String vehicleType) {
    log.debug("Getting all available Makes from cache");
    final IMap<String, MakeDoc> makeMap = hazelcastInstance.getMap(defName());
    if (makeMap.isEmpty()) {
      log.warn("No Makes in the cache");
      return Collections.emptyList();
    }
    List<Predicate<String, MakeDoc>> predicates = new ArrayList<>();
    if (!StringUtils.isBlank(vehicleType)) {
      predicates.add(Predicates.equal("refs[any].vehicleType", vehicleType));
    }
    return makeMap.values(Predicates.and(predicates.toArray(new Predicate[0])))
        .stream().flatMap(doc -> doc.getRefs().stream())
      .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private List<MakeRef> getMakesByVehicleClassAndSubClass(String vehicleClass,
      List<String> vehicleSubclass) {
    log.debug("Getting all available Makes from cache");
    final IMap<String, MakeDoc> makeMap = hazelcastInstance.getMap(defName());
    if (makeMap.isEmpty()) {
      log.warn("No Makes in the cache");
      return Collections.emptyList();
    }
    List<Predicate<String, MakeDoc>> predicates = new ArrayList<>();

    if (!StringUtils.isBlank(vehicleClass)) {
      String pattern = "(.*)(" + vehicleClass + ")(.*)";
      predicates.add(Predicates.regex("refs[any].vehicleClass", pattern));
    }

    if (CollectionUtils.isNotEmpty(vehicleSubclass)) {
      List<Predicate<String, MakeDoc>> predicatesForVehicleSubClass = new ArrayList<>();
      for (String subClass : vehicleSubclass) {
        String pattern = "(.*)(" + subClass + ")(.*)";
        predicatesForVehicleSubClass
            .add(Predicates.regex("refs[any].vehicleSubClass", pattern));
      }

      Predicate<String, MakeDoc> vehicleSubClassPredicatesOR =
          Predicates.or(predicatesForVehicleSubClass.toArray(new Predicate[0]));
      predicates.add(vehicleSubClassPredicatesOR);
    }
    return makeMap.values(Predicates.and(predicates.toArray(new Predicate[0]))).stream()
        .flatMap(doc -> doc.getRefs().stream()).collect(Collectors.toList());
  }

  private static MakeItem buildMakeItem(final MakeRef ref, final Optional<String> affilate) {
    return MakeItem.builder()
        .makeId(ref.getIdMake())
        .make(ref.getMake())
        .gtMake(ref.getGtmake())
        .gtVin(ref.isGtvin())
        .normauto(ref.isNormauto())
        .vehicleType(ref.getVehicleType())
        .top(affilate.map(af -> StringUtils.containsIgnoreCase(ref.getTop(), af)).orElse(true))
        .build();
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available makes from ES to Hazelcast instance");
    refreshCacheMakes(makeService.getAll());
  }

  @Override
  public IMap<String, MakeDoc> refreshCacheMakes(final List<MakeDoc> makes) {
    hazelcastInstance.getMap(defName()).evictAll();
    final IMap<String, MakeDoc> makeMap = hazelcastInstance.getMap(defName());
    makes.parallelStream().forEach(doc -> makeMap.put(doc.getId(), doc));
    return makeMap;
  }

  @Override
  public Optional<MakeItem> findMakeItemByCode(final String makeCode) {
    log.debug("Getting id_make from gtmake = {}", makeCode);
    if (StringUtils.isBlank(makeCode)) {
      return Optional.empty();
    }
    final IMap<String, MakeDoc> makeMap = hazelcastInstance.getMap(defName());
    if (makeMap.isEmpty()) {
      log.warn("No Makes in the cache");
      return Optional.empty();
    }

    return makeMap.values().stream()
        .flatMap(doc -> doc.getRefs().stream())
        .filter(ref -> StringUtils.equals(makeCode, ref.getGtmake()))
        .findFirst()
        .map(makeRef -> buildMakeItem(makeRef, Optional.empty()));
  }

  @Override
  public Map<String, String> findMakesByIds(List<String> makeIds) {
    if (CollectionUtils.isEmpty(makeIds)) {
      return Collections.emptyMap();
    }
    final IMap<String, MakeDoc> makeMap = hazelcastInstance.getMap(defName());
    return makeMap.values(Predicates.in("id", makeIds.stream().toArray(String[]::new))).stream()
      .flatMap(item -> item.getRefs().stream())
      .collect(Collectors.toMap(item -> String.valueOf(item.getIdMake()), MakeRef::getMake));
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

  @Override
  public Map<String, String> findMakeIdsByMakes(List<String> makes) {
    if (CollectionUtils.isEmpty(makes)) {
      return Collections.emptyMap();
    }
    final IMap<String, MakeDoc> makeMap = hazelcastInstance.getMap(defName());
    return makeMap.values().stream().flatMap(make -> make.getRefs().stream())
        .filter(ref -> makes.contains(ref.getMake()))
        .collect(Collectors.toMap(item -> String.valueOf(item.getIdMake()), MakeRef::getMake));

  }

  @Override
  public List<MakeItem> getMakesByVehicleClassAndSubClass(String vehClass, List<String> vehSubClass,
      SupportedAffiliate affiliate) {
    final Optional<String> affliate =
        Optional.ofNullable(affiliate).map(SupportedAffiliate::getEsShortName);
    return getMakesByVehicleClassAndSubClass(vehClass, vehSubClass).stream()
        .map(ref -> buildMakeItem(ref, affliate))
        .sorted(topAffilateComparator().thenComparing(alphabetComparator()))
        .collect(Collectors.toList());
  }
}
