package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.FormatGaSearchService;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Integration test class for Elasticsearch service of Format Generic Article.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@ChEshopIntegrationTest
public class FormatGaSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private FormatGaSearchService formatGaSearchService;

  @Before
  public void init() {
    Locale currentLocale =
        Arrays.asList(new Locale("de", "ch"), new Locale("de", "fr"), new Locale("de", "it"))
            .get(new Random().nextInt(3));
    LocaleContextHolder.setLocale(currentLocale);
  }

  @Test
  public void shouldGetTop10FormatGa() {
    List<FormatGaDoc> actualResult = formatGaSearchService.getAllFormatGa(PageUtils.DEF_PAGE)
        .getContent();
    Assert.assertNotNull(actualResult);
    Assert.assertThat(actualResult.size(), Matchers.greaterThanOrEqualTo(0));
  }
}
