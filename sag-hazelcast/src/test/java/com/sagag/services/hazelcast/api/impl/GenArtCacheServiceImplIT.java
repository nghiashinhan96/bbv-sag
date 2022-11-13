package com.sagag.services.hazelcast.api.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.GenArtSearchService;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.hazelcast.api.GenArtCacheService;
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
 * Integration test class for Cache service of Generic article.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
public class GenArtCacheServiceImplIT {

  @Autowired
  private GenArtCacheService genArtCacheService;

  @Autowired
  private GenArtSearchService genArtSearchService;

  /**
   * Initializes the generic articles data into cache.
   */
  @Before
  public void initCache() {
    if (!genArtCacheService.exists()) {
      LocaleContextHolder.setLocale(Locale.GERMAN);
      genArtCacheService.refreshCacheGenArts(
          genArtSearchService.getAllGenArts(PageUtils.DEF_PAGE).getContent());
      LocaleContextHolder.setLocale(Locale.FRENCH);
      genArtCacheService.refreshCacheGenArts(
          genArtSearchService.getAllGenArts(PageUtils.DEF_PAGE).getContent());
    }
  }

  @Test
  public void testSearchGenArtById() throws IOException {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    final String gaId = "10";
    final Map<String, GenArtTxt> genArtTxts =
        genArtCacheService.searchGenArtByIds(Arrays.asList(gaId));
    Assert.assertThat(genArtTxts, Matchers.notNullValue());
  }

  @Test
  public void testSearchGenArtByIdNotFound() throws IOException {
    final String gaId = "3229NF";
    final Map<String, GenArtTxt> genArtTxts =
        genArtCacheService.searchGenArtByIds(Arrays.asList(gaId));
    assertThat(true, is(genArtTxts.isEmpty()));
  }

  @Test
  public void shouldGetFrenchGenArts() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    final Map<String, GenArtTxt> genArtTxts =
        genArtCacheService.searchGenArtByIds(Arrays.asList("3014"));
    Assert.assertThat(genArtTxts, Matchers.notNullValue());
  }
}
