package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.CriteriaSearchService;
import com.sagag.services.elasticsearch.domain.CriteriaDoc;
import java.util.List;
import java.util.Locale;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests for Criteria Elasticsearch service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class CriteriaSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private CriteriaSearchService criteriaSearchService;

  @Test
  public void shouldGetTop10Criteria() {
    List<CriteriaDoc> allCriteria = criteriaSearchService.getTop10Criteria();
    Assert.assertThat(allCriteria.size(), Matchers.equalTo(10));
  }

  @Test
  public void testGetAllCriteriaWithGerman() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    shouldGetTop10Criteria();
  }

  @Test
  public void testGetAllCriteriaWithFrench() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    shouldGetTop10Criteria();
  }

  @Test
  public void testGetAllCriteriaWithItalian() {
    LocaleContextHolder.setLocale(Locale.ITALIAN);
    shouldGetTop10Criteria();
  }

  @Test
  public void testGetAllCriteriaWithEnglish() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    shouldGetTop10Criteria();
  }

}
