package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.ArticleVehiclesSearchService;
import com.sagag.services.elasticsearch.domain.article.ArticleVehicles;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class ArticleVehiclesSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private ArticleVehiclesSearchService articleVehiclesSearchService;

  @Test
  public void testSearchArticleVehiclesByArticleId() {
    String articleId = "1000301190";
    final List<ArticleVehicles> actual =
        articleVehiclesSearchService.searchArticleVehiclesByArtId(articleId);

    Assert.assertNotNull(actual);
    Assert.assertThat(actual.size(), Matchers.greaterThanOrEqualTo(0));
    if (CollectionUtils.isEmpty(actual)) {
      return;
    }

    List<ArticleVehicles> articleVehiclesDocs = actual.stream()
        .filter(x -> Objects.equals(x.getId(), "1000301190")).collect(Collectors.toList());
    Assert.assertThat(articleVehiclesDocs.size(), Matchers.greaterThanOrEqualTo(0));
    if (!CollectionUtils.isEmpty(articleVehiclesDocs)) {
      Assert.assertThat(actual, CoreMatchers.is(articleVehiclesDocs));
    }
  }

  @Test
  public void testSearchArticleVehiclesByArticleIdWithGerman() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    testSearchArticleVehiclesByArticleId();
  }

  @Test
  @Ignore("The AX have no FR index for article_vehicles")
  public void testSearchArticleVehiclesByArticleIdWithFrench() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    testSearchArticleVehiclesByArticleId();
  }

  @Test
  @Ignore("The AX have no IT index for article_vehicles")
  public void testSearchArticleVehiclesByArticleIdWithItalian() {
    LocaleContextHolder.setLocale(Locale.ITALIAN);
    testSearchArticleVehiclesByArticleId();
  }
}
