package com.sagag.services.stakis.erp.api.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.StakisErpApplication;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StakisErpApplication.class)
@EshopIntegrationTest
@CzProfile
@Slf4j
public class StakisErpArticleExternalServiceImplIT {

  @Autowired
  private StakisErpArticleExternalServiceImpl service;

  @Test
  public void testApi() {
    final TmUserCredentials credentials = DataProvider.tmUserCredentials();
    credentials.setContextId(1);
    final List<ArticleDocDto> articles = DataProvider.buildArticles();

    final List<ArticleDocDto> updatedArticles = service.searchArticlePricesAndAvailabilities(
        credentials.getUsername(), credentials.getCustomerId(), credentials.getPassword(),
        credentials.getLang(), articles, DataProvider.DF_VAT_FOR_CZ,
        new AdditionalSearchCriteria());

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(updatedArticles));
  }

  @Test
  public void testApi_ArticleIdList() {
    final TmUserCredentials credentials = DataProvider.tmUserCredentials();
    credentials.setContextId(1);
    final List<ArticleDocDto> articles = DataProvider.buildArticles("10082108", "10436525");

    final List<ArticleDocDto> updatedArticles = service.searchArticlePricesAndAvailabilities(
        credentials.getUsername(), credentials.getCustomerId(), credentials.getPassword(),
        credentials.getLang(), articles, DataProvider.DF_VAT_FOR_CZ,
        new AdditionalSearchCriteria());

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(updatedArticles));
  }

  @Test
  public void testApi_ArticleIdList_WithAvailsDetails_DE() {
    testApi_ArticleIdList_WithAvailsDetails_ByLang(Locale.GERMAN.getLanguage());
  }

  @Test
  public void testApi_ArticleIdList_WithAvailsDetails_EN() {
    testApi_ArticleIdList_WithAvailsDetails_ByLang(Locale.ENGLISH.getLanguage());
  }

  @Test
  public void testApi_ArticleIdList_WithAvailsDetails_IT() {
    testApi_ArticleIdList_WithAvailsDetails_ByLang(Locale.ITALIAN.getLanguage());
  }

  @Test
  public void testApi_ArticleIdList_WithAvailsDetails_CS() {
    testApi_ArticleIdList_WithAvailsDetails_ByLang("cs");
  }

  private void testApi_ArticleIdList_WithAvailsDetails_ByLang(String lang) {
    final TmUserCredentials credentials = DataProvider.tmUserCredentials();
    credentials.setLang(lang);
    credentials.setContextId(1);
    final List<ArticleDocDto> articles = DataProvider.buildArticles("10082108", "10436525");

    final List<ArticleDocDto> updatedArticles = service.searchArticleAvailabilitiesDetails(
        credentials.getUsername(), credentials.getCustomerId(), credentials.getPassword(),
        credentials.getLang(), articles, DataProvider.DF_VAT_FOR_CZ, Optional.empty());

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(updatedArticles));
  }

  @Test
  public void testApi_DepotsitArticleIdList() {
    final TmUserCredentials credentials = DataProvider.tmUserCredentials();
    credentials.setContextId(1);
    final List<ArticleDocDto> articles = DataProvider.buildArticles("10403173");

    final List<ArticleDocDto> updatedArticles = service.searchArticlePricesAndAvailabilities(
        credentials.getUsername(), credentials.getCustomerId(), credentials.getPassword(),
        credentials.getLang(), articles, DataProvider.DF_VAT_FOR_CZ,
        new AdditionalSearchCriteria());

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(updatedArticles));
  }
}
