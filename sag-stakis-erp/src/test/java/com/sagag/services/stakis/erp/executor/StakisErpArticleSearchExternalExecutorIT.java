package com.sagag.services.stakis.erp.executor;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
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
public class StakisErpArticleSearchExternalExecutorIT {

  @Autowired
  private ArticleSearchExternalExecutor executor;

  @Test
  public void testExecutor() {
    final TmUserCredentials credentials = DataProvider.tmUserCredentials();
    final List<ArticleDocDto> articles = DataProvider.buildArticles();
    ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
        .extCustomerId(credentials.getCustomerId())
        .extLanguage(credentials.getLang())
        .extUsername(credentials.getUsername())
        .extSecurityToken(credentials.getPassword())
        .articles(articles)
        .build();
    List<ArticleDocDto> updatedArticles = executor.execute(criteria);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(updatedArticles));
  }
}
