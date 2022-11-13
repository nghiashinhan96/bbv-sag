package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.services.elasticsearch.api.CriteriaSearchService;
import com.sagag.services.elasticsearch.domain.CriteriaDoc;
import com.sagag.services.elasticsearch.domain.CriteriaTxt;
import com.sagag.services.hazelcast.api.CriteriaCacheService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cache service for criteria document.
 */
@Service
@Slf4j
public class CriteriaCacheServiceImpl extends CacheDataProcessor implements CriteriaCacheService {

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Autowired
  private CriteriaSearchService criteriaService;

  @Override
  public Map<String, CriteriaTxt> searchCriteriaByIds(final List<String> criteriaIds) {
    log.debug("Searching the criteria by id from cache");
    final IMap<String, CriteriaDoc> criteriaMap = hazelcastInstance.getMap(getCacheName());
    final Map<String, CriteriaTxt> criteria = new HashMap<>();
    criteriaMap.values(
            Predicates.in("crittxts[any].cid", criteriaIds.toArray(new String[criteriaIds.size()])))
        .stream().flatMap(doc -> doc.getCrittxts().stream())
        .forEach(txt -> criteria.put(txt.getCid(), txt));
    return criteria;
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available criteria from ES to Hazelcast instance");
    refreshCacheCriteria(criteriaService.getAll());
  }

  @Override
  public IMap<String, CriteriaDoc> refreshCacheCriteria(List<CriteriaDoc> criteriaDocs) {
    hazelcastInstance.getMap(getCacheName()).evictAll();
    final IMap<String, CriteriaDoc> criteriaMap = hazelcastInstance.getMap(getCacheName());
    criteriaDocs.parallelStream().forEach(doc -> criteriaMap.put(doc.getId(), doc));
    return criteriaMap;
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

}
