package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.services.elasticsearch.api.GenArtSearchService;
import com.sagag.services.elasticsearch.domain.GenArtDoc;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.hazelcast.api.GenArtCacheService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Cache service implementation for generic article.
 */
@Service
@Slf4j
public class GenArtCacheServiceImpl extends CacheDataProcessor implements GenArtCacheService {

  private static final String KEY_GAID = "genArtTxts[any].gaid";

  @Autowired
  private HazelcastInstance hazelcastInstance;
  @Autowired
  private GenArtSearchService genArtService;

  @Override
  public Map<String, GenArtTxt> searchGenArtByIds(final List<String> gaIds) {
    return searchGenArtByIdsAndLanguage(gaIds, Optional.empty());
  }

  @Override
  public Map<String, GenArtTxt> searchGenArtByIdsAndLanguage(final List<String> gaIds,
      final Optional<Locale> localeOpt) {
    log.debug("Seaching the generic articles from a list of ids = {} and user language = {}", gaIds,
        localeOpt);
    Optional<String> languageCodeOpt = localeOpt.map(Locale::getLanguage);
    return searchGenArtByLanguage(gaIds, languageCodeOpt);
  }


  @Override
  public Map<String, GenArtTxt> searchGenArtByIdsAndLanguageCode(final List<String> gaIds,
      final Optional<String> languageCodeOpt) {
    log.debug("Seaching the generic articles from a list of ids = {} and user language = {}", gaIds,
        languageCodeOpt);
    return searchGenArtByLanguage(gaIds, languageCodeOpt);
  }

  private Map<String, GenArtTxt> searchGenArtByLanguage(final List<String> gaIds,
      Optional<String> languageCodeOpt) {
    final String cacheName = languageCodeOpt.map(this::getCacheName).orElseGet(this::getCacheName);
    final IMap<String, GenArtDoc> genArtsMap = hazelcastInstance.getMap(cacheName);
    final Map<String, GenArtTxt> genArts = new HashMap<>();
    genArtsMap.values(Predicates.in(KEY_GAID, gaIds.toArray(new String[gaIds.size()]))).stream()
        .flatMap(doc -> doc.getGenArtTxts().stream())
        .forEach(txt -> genArts.put(txt.getGaid(), txt));
    return genArts;
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available generic articles from ES to Hazelcast instance");
    refreshCacheGenArts(genArtService.getAll());
  }

  @Override
  public IMap<String, GenArtDoc> refreshCacheGenArts(List<GenArtDoc> genarts) {
    hazelcastInstance.getMap(getCacheName()).evictAll();
    final IMap<String, GenArtDoc> genArtMap = hazelcastInstance.getMap(getCacheName());
    genArtMap.addIndex(KEY_GAID, true);
    genarts.parallelStream().forEach(doc -> genArtMap.put(doc.getId(), doc));
    return genArtMap;
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

}
