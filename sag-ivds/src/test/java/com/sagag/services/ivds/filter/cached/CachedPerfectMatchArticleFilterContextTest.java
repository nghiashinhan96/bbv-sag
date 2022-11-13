package com.sagag.services.ivds.filter.cached;

import static org.mockito.Mockito.mock;

import com.google.common.collect.Maps;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.filter.aggregation.impl.GenArtAndSubAggregationResultBuilderImpl;
import com.sagag.services.ivds.filter.articles.ArticleFilterFactory;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.impl.CachedPerfectMatchArticleFilterContext;
import com.sagag.services.ivds.filter.optimizer.PerfectMatchArticleSearchOptimizer;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class CachedPerfectMatchArticleFilterContextTest {

  @InjectMocks
  private CachedPerfectMatchArticleFilterContext cachedPerfectMatchArticleFilterContext;

  @Mock
  private PerfectMatchArticleSearchOptimizer perfectMatchArtSearchOptimizer;

  @Mock
  private ArticleFilterFactory articleFilterFactory;

  @Mock
  private GenArtAndSubAggregationResultBuilderImpl genArtAndSubAggResultBuilder;

  @Mock
  private ContextService contextService;

  @Mock
  protected IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Test
  public void testInitContext() {
    Assert.assertThat(cachedPerfectMatchArticleFilterContext, Matchers.notNullValue());
  }

  @Test
  public void testSupportMode() {
    Assert.assertThat(cachedPerfectMatchArticleFilterContext.supportMode(FilterMode.BAR_CODE),
        Matchers.is(false));

    Assert.assertThat(cachedPerfectMatchArticleFilterContext.supportMode(FilterMode.TYRES_SEARCH),
        Matchers.is(false));


    Assert.assertThat(cachedPerfectMatchArticleFilterContext.supportMode(FilterMode.ID_SAGSYS),
        Matchers.is(false));

    Assert.assertThat(cachedPerfectMatchArticleFilterContext.supportMode(FilterMode.ARTICLE_NUMBER),
        Matchers.is(true));

    Assert.assertThat(cachedPerfectMatchArticleFilterContext.supportMode(FilterMode.FREE_TEXT),
        Matchers.is(true));
  }

  @Test
  public void givenDisabledPerfectMatchArticlesWithFreetextAUDIAndFirstPage() {
    testDisabledPerfectMatchArticlesWithFreetextAUDI(PageUtils.DEF_PAGE, StringUtils.EMPTY);
  }

  @Test
  public void givenDisabledPerfectMatchArticlesWithFreetextAUDIAndSecondPage() {
    final Pageable pageable = PageUtils.defaultPageable(1, SagConstants.DEFAULT_PAGE_NUMBER);
    testDisabledPerfectMatchArticlesWithFreetextAUDI(pageable, RandomStringUtils.randomNumeric(10));
  }

  private void testDisabledPerfectMatchArticlesWithFreetextAUDI(Pageable pageable,
      String contextKey) {
    testCachedPerfectMatchArticlesWithFreetext("audi", pageable, contextKey, true);
  }

  @Test
  public void givenSucceedPerfectMatchArticlesWithFreetextOC90AndFirstPage() {
    testSucceedPerfectMatchArticlesWithFreetextOC90(PageUtils.DEF_PAGE, StringUtils.EMPTY, false);
  }

  @Test
  public void givenSucceedPerfectMatchArticlesWithFreetextOC90AndFirstPageAndNotEmptyContextKey() {
    testSucceedPerfectMatchArticlesWithFreetextOC90(PageUtils.DEF_PAGE,
        RandomStringUtils.randomNumeric(10), true);
  }

  @Test
  public void givenSucceedPerfectMatchArticlesWithFreetextOC90AndSecondPage() {
    final Pageable pageable = PageUtils.defaultPageable(1, SagConstants.DEFAULT_PAGE_NUMBER);
    testSucceedPerfectMatchArticlesWithFreetextOC90(pageable,
        RandomStringUtils.randomNumeric(10), false);
  }

  @Test
  public void givenSucceedPerfectMatchArticlesWithFreetextOC90AndSecondPageAndEmptyContextKey() {
    final Pageable pageable = PageUtils.defaultPageable(1, SagConstants.DEFAULT_PAGE_NUMBER);
    testSucceedPerfectMatchArticlesWithFreetextOC90(pageable, StringUtils.EMPTY, true);
  }

  private void testSucceedPerfectMatchArticlesWithFreetextOC90(Pageable pageable,
      String contextKey, boolean disabledPerfectMatch) {
    testCachedPerfectMatchArticlesWithFreetext("oc90", pageable, contextKey, disabledPerfectMatch);
  }

  private void testCachedPerfectMatchArticlesWithFreetext(String searchStr, Pageable pageable,
      String contextKey, boolean disabledPerfectMatch) {
    final UserInfo user = DataProvider.buildUserInfo();
    final FilterMode filterMode = FilterMode.FREE_TEXT;
    final Optional<AdditionalSearchCriteria> additional = Optional.empty();
    final ArticleFilterRequest request =
        DataProvider.buildArticleFilterRequest(searchStr, filterMode);
    request.setContextKey(contextKey);

    final Page<ArticleDocDto> articles =
        disabledPerfectMatch ? Page.empty(pageable) : DataProvider.buildArticles(pageable);
    final Map<String, List<SagBucket>> aggregations = Maps.newHashMap();

    final FilteredArticleAndAggregationResponse mockedResponse =
        FilteredArticleAndAggregationResponse.empty(pageable);

    mockedResponse.setArticles(articles);
    mockedResponse.setAggregations(aggregations);

    Mockito.when(perfectMatchArtSearchOptimizer.filterAllOptimized(Mockito.any(), Mockito.any()))
        .thenReturn(mockedResponse);

    Mockito.when(perfectMatchArtSearchOptimizer
        .filterOptimized(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
            Mockito.any()))
        .thenReturn(mockedResponse);

    Mockito.when(contextService.getCachedArticlesResult(Mockito.anyString()))
        .thenReturn(DataProvider.buildMockedCachedResponse(articles));

    Mockito.when(ivdsArticleTaskExecutors.executeTaskStockOnlyWithoutVehicle(Mockito.any(),
            Mockito.any(), Mockito.any()))
        .thenReturn(new PageImpl<>(Collections.singletonList(mock(ArticleDocDto.class))));

    // When
    final ArticleFilteringResponseDto response = cachedPerfectMatchArticleFilterContext
        .execute(user, filterMode, request, pageable, additional);

    // Then
    Assert.assertThat(response, Matchers.notNullValue());

    Mockito.verify(perfectMatchArtSearchOptimizer, Mockito.times(1))
        .filterOptimized(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
            Mockito.any());

    Mockito.verify(contextService, Mockito.atLeast(disabledPerfectMatch ? 0 : 1))
        .getCachedArticlesResult(Mockito.anyString());
  }

}
