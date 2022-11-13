package com.sagag.services.hazelcast.api.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.api.CriteriaSearchService;
import com.sagag.services.elasticsearch.domain.CriteriaTxt;
import com.sagag.services.hazelcast.api.CriteriaCacheService;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * Integration tests for Criteria cache service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
public class CriteriaCacheServiceImplIT {

  @Autowired
  private CriteriaCacheService criteriaCacheService;

  @Autowired
  private CriteriaSearchService criteriaSearchService;

  /**
   * Initializes the criteria data into cache.
   */
  @Before
  public void initCache() {
    if (!criteriaCacheService.exists()) {
      LocaleContextHolder.setLocale(Locale.GERMAN);
      criteriaCacheService.refreshCacheCriteria(criteriaSearchService.getTop10Criteria());
      LocaleContextHolder.setLocale(Locale.FRENCH);
      criteriaCacheService.refreshCacheCriteria(criteriaSearchService.getTop10Criteria());
    }
  }

  @Test
  public void testSearchCriteriaById() throws IOException {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    final String criteriaId = "1098";
    final Map<String, CriteriaTxt> criteria =
        criteriaCacheService.searchCriteriaByIds(Arrays.asList(criteriaId));
    Assert.assertThat(criteria.values(), Matchers.notNullValue());
  }

  @Test
  public void testSearchCriteriaByIdNotFound() throws IOException {
    final String criteriaId = "1099NF";
    final Map<String, CriteriaTxt> criteria =
        criteriaCacheService.searchCriteriaByIds(Arrays.asList(criteriaId));
    assertThat(true, is(criteria.isEmpty()));
  }

  @Test
  public void shouldGetFrenchCriteria() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    final Map<String, CriteriaTxt> criteria =
        criteriaCacheService.searchCriteriaByIds(Arrays.asList("1065"));
    Assert.assertThat(criteria.values(), Matchers.notNullValue());

  }
}
