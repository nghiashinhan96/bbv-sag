package com.sagag.services.oauth2.config;

import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;

@Configuration
@Slf4j
public class OAuth2ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

  private final CacheDataProcessor eshopClientCacheDataProcessor;

  @Autowired
  public OAuth2ApplicationStartup(
    @Qualifier("eshopClientCacheServiceImpl") final CacheDataProcessor cacheDataProcessor) {
    this.eshopClientCacheDataProcessor = cacheDataProcessor;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    log.debug("Caching all eshop client into hazelcast when application startup");
    try {
      eshopClientCacheDataProcessor.refreshCacheAll();
    } catch(DataAccessException ex) {
      log.warn("Can not refresh cache with exception", ex);
    }
  }

}
