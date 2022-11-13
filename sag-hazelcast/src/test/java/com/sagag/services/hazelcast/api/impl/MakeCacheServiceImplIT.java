package com.sagag.services.hazelcast.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.dto.MakeItem;
import com.sagag.services.elasticsearch.api.MakeSearchService;
import com.sagag.services.hazelcast.api.MakeCacheService;
import com.sagag.services.hazelcast.app.HazelcastApplication;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

/**
 * Integration test class for make cache service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
public class MakeCacheServiceImplIT {

  @Autowired
  private MakeCacheService makeCacheService;

  @Autowired
  private MakeSearchService makeSearchService;

  /**
   * Initializes the makes data into cache.
   */
  @Before
  public void initCache() {
    if (!makeCacheService.exists()) {
      makeCacheService.refreshCacheMakes(makeSearchService.getTop10Makes());
    }
  }

  @Test
  public void testGetAllSortedMakes() {
    final List<MakeItem> makes = makeCacheService.getAllSortedMakes(StringUtils.EMPTY,
        SupportedAffiliate.DERENDINGER_AT);
    Assert.assertThat(makes.size(), Matchers.greaterThan(0));
  }

  @Test
  public void testFindMakeIdFromGtmotiveMakeCode() {
    final MakeItem makeItem = makeCacheService.findMakeItemByCode("SB1").get();
    Assert.assertThat("99", Is.is(String.valueOf(makeItem.getMakeId())));
  }

  @Test
  public void testGTVinSortedMakes() {
    final List<MakeItem> makes = makeCacheService.getAllSortedMakes(StringUtils.EMPTY,
        SupportedAffiliate.DERENDINGER_AT);
    Assert.assertNotNull(makes);
    final Optional<MakeItem> disabledVinMake =
        makes.stream().filter(make -> "DAIHATSU".equals(make.getMake())).findFirst();
    Assert.assertThat(disabledVinMake.isPresent(), Matchers.is(true));
    Assert.assertThat(disabledVinMake.get().isGtVin(), Matchers.is(false));

    final Optional<MakeItem> enabledVinMake =
        makes.stream().filter(make -> "OPEL".equals(make.getMake())).findFirst();
    Assert.assertThat(enabledVinMake.isPresent(), Matchers.is(true));
    Assert.assertThat(enabledVinMake.get().isGtVin(), Matchers.is(true));
  }

}
