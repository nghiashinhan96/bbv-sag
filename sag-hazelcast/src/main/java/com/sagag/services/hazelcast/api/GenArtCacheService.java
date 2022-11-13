package com.sagag.services.hazelcast.api;

import com.hazelcast.core.IMap;
import com.sagag.services.elasticsearch.domain.GenArtDoc;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.hazelcast.app.HazelcastMaps;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Interface to define services for generic article caching.
 */
public interface GenArtCacheService extends CacheService {

  /**
   * Returns the map of {@link GenArtTxt} from its ids.
   *
   * @param gaIds the searching generic article ids
   * @return a map of {@link GenArtTxt}. Returns empty map if no generic articles found.
   */
  Map<String, GenArtTxt> searchGenArtByIds(List<String> gaIds);

  /**
   * Returns the map of {@link GenArtTxt} from its ids and language optional.
   *
   * @param gaIds the searching generic article ids
   * @param langOpt the optional user language
   * @return a map of {@link GenArtTxt}. Returns empty map if no generic articles found.
   */
  Map<String, GenArtTxt> searchGenArtByIdsAndLanguage(List<String> gaIds,
      Optional<Locale> localeOpt);

  @Override
  default String defName() {
    return HazelcastMaps.GEN_ART_MAP.name();
  }

  /**
   * Refreshes the cache with a list of generic articles.
   *
   * @return the {@link IMap}
   */
  IMap<String, GenArtDoc> refreshCacheGenArts(List<GenArtDoc> genarts);

  /**
   * Returns the map of {@link GenArtTxt} from its ids and language code optional.
   *
   * @param gaIds the searching generic article ids
   * @param languageCodeOpt the optional user language code
   * @return a map of {@link GenArtTxt}. Returns empty map if no generic articles found.
   */
  Map<String, GenArtTxt> searchGenArtByIdsAndLanguageCode(List<String> gaIds,
      Optional<String> languageCodeOpt);
}
