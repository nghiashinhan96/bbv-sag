package com.sagag.services.rest.scheduled;

import com.sagag.services.common.schedule.ScheduledTask;
import com.sagag.services.hazelcast.provider.CacheDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CachedSyncedScheduledTask implements ScheduledTask {

  @Autowired
  private CacheDataProvider cacheDataProvider;

  @Override
  @Scheduled(cron = "${hours.refresh_cache}")
  public void executeTask() {
    log.debug("Refreshing cached data by scheduled");
    cacheDataProvider.refreshCacheData();
    log.debug("Finished refresh cached data by scheduled");
  }
}
