package com.sagag.services.hazelcast.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.dto.ModelItem;
import com.sagag.services.elasticsearch.api.ModelSearchService;
import com.sagag.services.hazelcast.api.ModelCacheService;
import com.sagag.services.hazelcast.app.HazelcastApplication;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Integration test class for Model cache service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
public class ModelCacheServiceImplIT {

  private static final int FR_MAKE_ID_64 = 64;

  private static final String MODEL_ID = "14";

  @Autowired
  private ModelCacheService modelCacheService;

  @Autowired
  private ModelSearchService modelSearchService;

  /**
   * Initializes the models data into cache.
   */
  @Before
  public void initCache() {
    if (!modelCacheService.exists()) {
      LocaleContextHolder.setLocale(Locale.GERMAN);
      modelCacheService.refreshCacheModels(
          modelSearchService.getAllModels(PageUtils.DEF_PAGE).getContent());
      LocaleContextHolder.setLocale(Locale.FRENCH);
      modelCacheService.refreshCacheModels(
          modelSearchService.getAllModels(PageUtils.DEF_PAGE).getContent());
    }
  }

  @Test
  public void testGetModelById() {
    final Optional<ModelItem> modelOpt = modelCacheService.getModelById(MODEL_ID);
    Assert.assertTrue(modelOpt.isPresent());
  }

  public void testGetModelByIdNotFound() {
    final Optional<ModelItem> modelOpt = modelCacheService.getModelById("1122");
    Assert.assertFalse(modelOpt.isPresent());
  }

  @Test
  public void testGetAllSortedModelsByMake() {
    final List<ModelItem> models = modelCacheService.getAllSortedModelsByMake(FR_MAKE_ID_64,
        StringUtils.EMPTY, Collections.emptyList(), StringUtils.EMPTY);
    Assert.assertThat(models, Matchers.notNullValue());
  }

  @Test
  public void shouldGetFrenchModels() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    final List<ModelItem> models = modelCacheService.getAllSortedModelsByMake(FR_MAKE_ID_64,
        StringUtils.EMPTY, Collections.emptyList(), StringUtils.EMPTY);
    Assert.assertThat(models, Matchers.notNullValue());
  }
}
