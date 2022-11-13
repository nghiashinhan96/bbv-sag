package com.sagag.services.hazelcast.provider.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.hazelcast.api.GenArtCacheService;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;
import com.sagag.services.hazelcast.app.HazelcastApplication;
import com.sagag.services.hazelcast.provider.CacheDataProvider;

import lombok.extern.slf4j.Slf4j;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
@Slf4j
public class CacheDataProviderImplIT {

  @Autowired
  private CacheDataProvider cacheDataProvider;

  @Autowired
  @Qualifier("genArtCacheServiceImpl")
  private CacheDataProcessor genArtCacheDataProcessor;

  @Autowired
  private GenArtCacheService genArtService;

  @Test
  public void shouldRefreshCacheData() {
    cacheDataProvider.refreshCacheData();
  }

  @Test
  @Ignore
  public void shouldRefreshAlllGenArtData() {
    genArtCacheDataProcessor.refreshCacheAll();
    Map<String, GenArtTxt> genArtMap = genArtService.searchGenArtByIds(Arrays.asList("2110082"));
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(genArtMap));
  }

}
