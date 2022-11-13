package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.ModelSearchService;
import com.sagag.services.elasticsearch.domain.ModelDoc;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

/**
 * Integration tests for Model Elasticsearch service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class ModelSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private ModelSearchService modelSearchService;

  @Test
  public void shouldGetTop10Models() {
    Page<ModelDoc> allModels = modelSearchService.getAllModels(PageUtils.DEF_PAGE);
    Assert.assertNotNull(allModels);
    Assert.assertThat(allModels.getContent().size(), Matchers.equalTo(10));
  }

  @Test
  public void testGetAllModelsWithGerman() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    shouldGetTop10Models();
  }

  @Test
  public void testGetAllModelsWithFrench() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    shouldGetTop10Models();
  }

  @Test
  public void testGetAllModelsWithItalian() {
    LocaleContextHolder.setLocale(Locale.ITALIAN);
    shouldGetTop10Models();
  }

}
