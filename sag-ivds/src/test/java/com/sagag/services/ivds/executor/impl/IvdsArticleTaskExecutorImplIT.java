package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.converter.article.ArticleConverter;
import com.sagag.services.ivds.domain.ArticleExternalRequestOption;
import com.sagag.services.ivds.executor.impl.ax.AxIvdsArticleTaskExecutorImpl;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

/**
 * IT to verify {@link AxIvdsArticleTaskExecutorImpl}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
@Slf4j
public class IvdsArticleTaskExecutorImplIT {

  @Autowired
  private AxIvdsArticleTaskExecutorImpl executor;

  @Autowired
  private ArticleSearchService articleSearchService;

  @Autowired
  @Qualifier("makeCacheServiceImpl")
  private CacheDataProcessor makeCacheDataProcessor;

  @Autowired
  private ArticleConverter articleConverter;

  @Before
  public void setUp() {
    makeCacheDataProcessor.refreshCacheAll();
  }

  @Test
  public void shouldGroupingOENumbers() {
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria("DDF1218C", ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDocDto> articles =
      articleSearchService.searchArticlesByNumber(criteria, PageUtils.DEF_PAGE)
      .map(articleConverter);
    ArticleExternalRequestOption request = ArticleExternalRequestOption.builder().build();
    final List<ArticleDocDto> result =
      executor.executeTask(user(), articles.getContent(), Optional.empty(), request);

    log.debug("Result = {}", SagJSONUtil.convertObjectToPrettyJson(result));
  }

  private UserInfo user() {
    final UserInfo user = new UserInfo();
    user.setId(1l);
    user.setCompanyName(SupportedAffiliate.DERENDINGER_AT.getCompanyName());
    return user;
  }

}
