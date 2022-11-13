package com.sagag.services.hazelcast.api;

import com.hazelcast.core.HazelcastInstance;
import com.sagag.services.common.contants.SagConstants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Common Cache service interfacing.
 */
public interface CacheService {

  /**
   * Returns the cache name by language.
   *
   * @return the cache name.
   */
  default String getCacheName(String userLang) {
    if (Locale.GERMAN.getLanguage().equalsIgnoreCase(userLang)) {
      return defName();
    }
    return buildCacheName(defName(), userLang);
  }

  default String getCacheName(Locale locale) {
    return getCacheName(locale.getLanguage());
  }

  default String getCacheName() {
    return getCacheName(LocaleContextHolder.getLocale());
  }

  static String buildCacheName(String mapName, Locale locale) {
    return buildCacheName(mapName, locale.getLanguage());
  }

  static String buildCacheName(String mapName, String userLang) {
    if (Locale.GERMAN.getLanguage().equalsIgnoreCase(userLang)) {
      return mapName;
    }
    return mapName + SagConstants.UNDERSCORE + StringUtils.upperCase(userLang);
  }

  /**
   * Returns the default cache map name.
   *
   * @return the default cache name.
   */
  String defName();

  /**
   * Checks whether there's data exists in cached for this cache map.
   *
   * @return <code>true</code> if already have records in cache. <code>false</code> otherwise.
   */
  default boolean exists() {
    return !hzInstance().getMap(getCacheName()).isEmpty();
  }

  /**
   *
   */
  HazelcastInstance hzInstance();
}
