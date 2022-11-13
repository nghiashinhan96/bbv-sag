package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.GenArtSearchService;
import com.sagag.services.elasticsearch.domain.GenArtDoc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Integration test service for Generic article Elasticsearch service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class GenArtSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private GenArtSearchService genArtService;

  @Test
  public void shouldGetTop10GenArts() {
    Page<GenArtDoc> allGenArts = genArtService.getAllGenArts(PageUtils.DEF_PAGE);
    Assert.assertNotNull(allGenArts.stream().map(GenArtDoc::getId).collect(Collectors.toList()));
    Assert.assertNotNull(allGenArts);
  }

  @Test
  public void testGetAllGenArtsWithGerman() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    shouldGetTop10GenArts();
  }

  @Test
  public void testGetAllGenArtsWithFrench() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    shouldGetTop10GenArts();
  }

  @Test
  public void testGetAllGenArtsWithItalian() {
    LocaleContextHolder.setLocale(Locale.ITALIAN);
    shouldGetTop10GenArts();
  }

  @Test
  public void testGetAllGenArtsWithEnglish() {
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    shouldGetTop10GenArts();
  }
}
