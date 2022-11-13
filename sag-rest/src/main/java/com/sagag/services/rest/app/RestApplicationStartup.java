package com.sagag.services.rest.app;

import com.sagag.services.hazelcast.provider.CacheDataProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Application ready event listener job class.
 */
@Component
@PropertySource("classpath:version.properties")
public class RestApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

  @Autowired
  private CacheDataProvider cacheDataProvider;

  @Autowired
  private Environment environment;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    if (!environment.acceptsProfiles("test")) {
      cacheDataProvider.refreshCacheData();
    }
  }

}
