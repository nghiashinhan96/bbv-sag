package com.sagag.services.ivds.api.impl;

import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.api.impl.GenArtCacheServiceImpl;
import com.sagag.services.ivds.api.IvdsArticleInVehicleContextService;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.request.CategorySearchRequest;
import com.sagag.services.ivds.response.ArticleListSearchResponseDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@ChEshopIntegrationTest
@Slf4j
public class IvdsArticleInVehicleContextServiceImplIT extends BaseSearchServiceImplIT {

  @Autowired
  private IvdsArticleInVehicleContextService ivdsArticleService;

  @Autowired
  private GenArtCacheServiceImpl genArtCacheService;

  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    genArtCacheService.refreshCacheAll();
  }

  @Test
  public void testSearchArticlesByVehIdAndGaIds()
      throws ServiceException {
    final String vehId = StringUtils.EMPTY;
    final List<String> gaIds = Arrays.asList("1");

    final Page<ArticleDocDto> articles = executeSearchArticlesByVehIdAndGaIds(vehId, gaIds);
    Assert.assertThat(articles.hasContent(), Matchers.is(false));
  }

  @Test
  public void testSearchArticlesByVehIdAndGaIdsV15507M15345And686()
      throws ServiceException {
    final String vehId = "V15507M15345";
    final List<String> gaIds = Arrays.asList("686");

    final Page<ArticleDocDto> articles = executeSearchArticlesByVehIdAndGaIds(vehId, gaIds);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  @Test
  public void testSearchArticlesByVehIdAndGaIdsV28024M2578And686()
    throws ServiceException {
    final String vehId = "V28024M2578";
    final List<String> gaIds = Arrays.asList("686");

    final Page<ArticleDocDto> articles = executeSearchArticlesByVehIdAndGaIds(vehId, gaIds);

    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  private Page<ArticleDocDto> executeSearchArticlesByVehIdAndGaIds(String vehId, List<String> gaIds)
    throws ServiceException {
    CategorySearchRequest categorySearchRequest =
      CategorySearchRequest.builder().vehIds(Arrays.asList(vehId)).genArtIds(gaIds)
      .usingVersion2(true).build();
    ArticleListSearchResponseDto result =
      ivdsArticleService.searchArticlesInVehicleContext(user, categorySearchRequest);
    Page<ArticleDocDto> articles = result.getArticles();
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(articles));
    return articles;
  }
}
