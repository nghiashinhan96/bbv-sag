package com.sagag.services.hazelcast.api.impl;

import com.sagag.eshop.service.api.VatRateService;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.hazelcast.api.VatRateCacheService;
import com.sagag.services.hazelcast.app.HazelcastApplication;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Integration test class for vat rate cache service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
public class VatRateCacheServiceImplIT {

  @Autowired
  private VatRateCacheService vatRateCacheService;

  @Autowired
  private VatRateService vatRateService;

  /**
   * Initializes the vat rate data into cache.
   */
  @Before
  public void initCache() {
      vatRateCacheService.refreshAllCacheVatRate(vatRateService.getAll());
  }

  @Test
  public void testGetCacheVatRateByArticleId() {
    final String atrId = "1000054556";
    LocaleContextHolder.setLocale(Locale.GERMAN);
    Assert.assertThat(vatRateCacheService.getCacheVatRateByArticleId(atrId).isPresent(),
        Matchers.is(true));
  }

  @Test
  public void testGetCacheVatRateByArticleIds() {
    List<String> atrIds = new ArrayList<>();
    atrIds.add("1000054556");
    Assert.assertThat(vatRateCacheService.getCacheVatRateByArticleIds(atrIds).size()
        , Matchers.greaterThan(0));
  }

  @Test
  public void testGetAllCacheCategories() {
    Assert.assertThat(vatRateCacheService.getAllCacheVatRate().size(),
        Matchers.greaterThanOrEqualTo(2));
  }
}
