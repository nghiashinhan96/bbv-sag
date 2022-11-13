package com.sagag.services.hazelcast.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.CategoryService;
import com.sagag.services.hazelcast.api.CategoryCacheService;
import com.sagag.services.hazelcast.app.HazelcastApplication;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Locale;

/**
 * Integration test class for Category cache service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
public class CategoryCacheServiceImplIT {

  @Autowired
  private CategoryCacheService catCacheService;

  @Autowired
  private CategoryService categoryService;

  /**
   * Initializes the categories data into cache.
   */
  @Before
  public void initCache() {
    if (!catCacheService.exists()) { // to avoid run multiple times
      LocaleContextHolder.setLocale(Locale.GERMAN);
      catCacheService.refreshCacheCategories(
          categoryService.getAllCategories(PageUtils.DEF_PAGE).getContent());
      LocaleContextHolder.setLocale(Locale.FRENCH);
      catCacheService.refreshCacheCategories(
          categoryService.getAllCategories(PageUtils.DEF_PAGE).getContent());
    }
  }

  @Test
  @Ignore("Data is huge for running")
  public void testGetAllCacheCategories() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    Assert.assertThat(catCacheService.getAllCacheCategories().size(),
        Matchers.greaterThanOrEqualTo(10));
  }

  @Test
  @Ignore("ES Data is updated")
  public void testFindCategoriesByGaids() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    Assert.assertThat("has categories by gaid",
        catCacheService.findCategoriesByGaids(
            Arrays.asList("2110082", "1164", "322", "455", "572", "757", "2048")).isEmpty(),
        Matchers.is(false));
  }

}
