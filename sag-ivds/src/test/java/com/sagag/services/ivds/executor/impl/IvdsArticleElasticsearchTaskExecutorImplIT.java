package com.sagag.services.ivds.executor.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticlePartDto;
import com.sagag.services.elasticsearch.enums.ArticlePartType;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.utils.IvdsDataTestUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
public class IvdsArticleElasticsearchTaskExecutorImplIT {

  private static final String ARTICLE_FDB1398_JSON_NAME = "article_fdb1398.json";

  @Autowired
  private IvdsArticleElasticsearchTaskExecutorImpl executor;

  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void givenAdditionalParts_shouldsetPccs() throws IOException {

    ArticleDocDto article = IvdsDataTestUtils.article(ARTICLE_FDB1398_JSON_NAME);
    final List<ArticlePartDto> parts = new ArrayList<>();
    parts.add(ArticlePartDto.builder().brandid("62").pnrn("23130").ptype(ArticlePartType.PCC.name())
        .build());
    parts.add(ArticlePartDto.builder().brandid("62").pnrn("23131").ptype(ArticlePartType.PCC.name())
        .build());
    parts.add(ArticlePartDto.builder().brandid("62").pnrn("23187").ptype(ArticlePartType.PCC.name())
        .build());
    article.setParts(parts);
    List<ArticleDocDto> articles = new ArrayList<>();
    articles.add(article);
    Assert.assertFalse(article.hasPnrnPccs());

    executor.executeTask(articles, Optional.empty(), SupportedAffiliate.DERENDINGER_AT);

    Assert.assertTrue(articles.get(0).hasPnrnPccs());
  }

  @Ignore("Init make cache")
  @Test
  public void givenAdditionalParts_shouldsetOes() throws IOException {

    ArticleDocDto article = IvdsDataTestUtils.article(ARTICLE_FDB1398_JSON_NAME);
    final List<ArticlePartDto> parts = new ArrayList<>();
    parts.add(ArticlePartDto.builder().brandid("104").pnrn("1K0698151J")
        .ptype(ArticlePartType.OEM.name()).build());
    parts.add(ArticlePartDto.builder().brandid("104").pnrn("8Z0698151A")
        .ptype(ArticlePartType.OEM.name()).build());
    parts.add(ArticlePartDto.builder().brandid("104").pnrn("1J0698151D")
        .ptype(ArticlePartType.OEM.name()).build());
    article.setParts(parts);
    List<ArticleDocDto> articles = new ArrayList<>();
    articles.add(article);
    Assert.assertFalse(article.hasOeNumbers());

    executor.executeTask(articles, Optional.empty(), SupportedAffiliate.DERENDINGER_AT);

    Assert.assertTrue(articles.get(0).hasOeNumbers());
  }

  @Ignore("Init make cache")
  @Test
  public void givenAdditionalParts_shouldsetIams() throws IOException {

    ArticleDocDto article = IvdsDataTestUtils.article(ARTICLE_FDB1398_JSON_NAME);
    final List<ArticlePartDto> parts = new ArrayList<>();
    parts.add(ArticlePartDto.builder().brandid("624").pnrn("GBP1283AF")
        .ptype(ArticlePartType.IAM.name()).build());
    parts.add(ArticlePartDto.builder().brandid("2676").pnrn("DBP391398")
        .ptype(ArticlePartType.IAM.name()).build());
    parts.add(ArticlePartDto.builder().brandid("617").pnrn("MDB2040")
        .ptype(ArticlePartType.IAM.name()).build());
    article.setParts(parts);
    List<ArticleDocDto> articles = new ArrayList<>();
    articles.add(article);
    Assert.assertFalse(article.hasIamNumbers());
    executor.executeTask(articles, Optional.empty(), SupportedAffiliate.DERENDINGER_AT);

    Assert.assertTrue(articles.get(0).hasIamNumbers());
  }

  @Test
  public void givenAdditionalParts_shouldsetEans() throws IOException {

    ArticleDocDto article = IvdsDataTestUtils.article(ARTICLE_FDB1398_JSON_NAME);
    final List<ArticlePartDto> parts = new ArrayList<>();
    parts.add(ArticlePartDto.builder().brandid("62").pnrn("5016687199178").ptype(ArticlePartType.EAN.name()).build());
    parts.add(ArticlePartDto.builder().brandid("62").pnrn("5888888865016687199178").ptype(ArticlePartType.EAN.name()).build());
    parts.add(ArticlePartDto.builder().brandid("617").pnrn("MDB2040").ptype(ArticlePartType.EAN.name()).build());
    article.setParts(parts);
    List<ArticleDocDto> articles = new ArrayList<>();
    articles.add(article);
    Assert.assertFalse(article.hasPnrnEANs());

    executor.executeTask(articles, Optional.empty(), SupportedAffiliate.DERENDINGER_AT);

    Assert.assertTrue(articles.get(0).hasPnrnEANs());
  }
}
