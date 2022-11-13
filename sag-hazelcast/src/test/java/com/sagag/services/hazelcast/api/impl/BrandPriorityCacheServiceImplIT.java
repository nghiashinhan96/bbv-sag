package com.sagag.services.hazelcast.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.hazelcast.app.HazelcastApplication;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
@Slf4j
public class BrandPriorityCacheServiceImplIT {

  @Autowired
  private BrandPriorityCacheServiceImpl service;

  @Before
  public void setup() {
    service.refreshCacheAll();
  }

  @Test
  public void test() {
    Map<String, CachedBrandPriorityDto> result =
        service.findCachedBrandPriority(Arrays.asList("402"));
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(result));
    Assert.assertThat(result.isEmpty(), Matchers.is(false));
  }

}
