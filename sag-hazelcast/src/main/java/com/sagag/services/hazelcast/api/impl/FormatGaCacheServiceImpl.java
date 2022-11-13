package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.services.elasticsearch.api.FormatGaSearchService;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaElement;
import com.sagag.services.hazelcast.api.FormatGaCacheService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cache service implementation class for Format Generic Article.
 */
@Service
@Slf4j
public class FormatGaCacheServiceImpl extends CacheDataProcessor implements FormatGaCacheService {

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Autowired
  private FormatGaSearchService formatGaSearchService;

  @Override
  public Map<String, FormatGaDoc> searchFormatGaByGaIds(List<String> gaIds) {
    log.debug("Searching the generic format_ga from a list of gaids");
    final IMap<String, FormatGaDoc> genArtsMap = hazelcastInstance.getMap(getCacheName());
    final Map<String, FormatGaDoc> formatGas = new HashMap<>();
    genArtsMap.values(Predicates.in("genarts[any].gaid", gaIds.toArray(new String[gaIds.size()])))
        .stream()
        .forEach(doc -> formatGas.put(String.valueOf(doc.getGenarts().get(0).getGaid()), doc));
    return formatGas;
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available generic format ga from ES to Hazelcast instance");
    refreshCacheFormatGas(formatGaSearchService.getAll());
  }

  @Override
  public IMap<String, FormatGaDoc> refreshCacheFormatGas(List<FormatGaDoc> formatGas) {
    hazelcastInstance.getMap(getCacheName()).evictAll();
    final IMap<String, FormatGaDoc> formatGaMap = hazelcastInstance.getMap(getCacheName());
    formatGas.parallelStream().forEach(doc -> {
      sortFormatGaElements(doc.getElements());
      formatGaMap.put(String.valueOf(doc.getGenarts().get(0).getGaid()), doc);
    });
    return formatGaMap;
  }

  private void sortFormatGaElements(List<FormatGaElement> elements) {
    Collections.sort(elements, (ele1, ele2) -> {
      int result = sortFormatGaElementByField(ele1.getSort(), ele2.getSort());
      if (Integer.valueOf(0).equals(result)) {
        return sortFormatGaElementByField(ele1.getOrder(), ele2.getOrder());
      }
      return result;
    });
  }

  private int sortFormatGaElementByField(final String field1, final String field2) {
    Integer obj1 = null;
    if (!StringUtils.isEmpty(field1)) {
      obj1 = Integer.valueOf(field1);
    }
    Integer obj2 = null;
    if (!StringUtils.isEmpty(field2)) {
      obj2 = Integer.valueOf(field2);
    }
    if (obj1 == null || obj2 == null) {
      return -1;
    }
    return obj1.compareTo(obj2);
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

}
