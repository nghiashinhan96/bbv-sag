package com.sagag.services.ivds.filter.optimizer;

import com.google.common.collect.Maps;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.executor.impl.ax.AxIvdsPerfectMatchArticleTaskExecutorImpl;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@RunWith(SpringRunner.class)
public class PerfectMatchArticleSearchOptimizerTest {

  @InjectMocks
  private PerfectMatchArticleSearchOptimizer articleSearchOptimizer;

  @Mock
  private AxIvdsPerfectMatchArticleTaskExecutorImpl ivdsPerfectMatchArtTaskExecutor;

  @Mock
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Test
  public void testInit() {
    Assert.assertThat(articleSearchOptimizer, Matchers.notNullValue());
  }

  @Test
  public void givenSucceedFilterAllOptimizedWhenFreetextOC90() {
    executeTestFilterAllOptimized("oc90", DataProvider.buildArticles(PageUtils.DEF_PAGE));
  }

  @Test
  public void givenEmptyPageFilterAllOptimizedWhenFreetext() {
    executeTestFilterAllOptimized("oRc9R0R", Page.empty());
  }

  @Test
  public void givenSucceedFilterAllOptimizedAndChangedPageableWhenFreetextOC() {
    executeTestFilterAllOptimized("oc", DataProvider.buildArticles(PageUtils.DEF_PAGE));
  }

  @Test
  public void givenDisabledFilterAllOptimizedWhenFreetext() {
    executeTestFilterAllOptimized("o9c0*", DataProvider.buildArticles(PageUtils.DEF_PAGE));
  }

  private void executeTestFilterAllOptimized(String searchStr,
      Page<ArticleDocDto> expectedArticles) {

    final UserInfo user = DataProvider.buildUserInfo();
    final ArticleFilterRequest request = ArticleFilterRequest.builder()
        .keyword(searchStr)
        .build();
    BiFunction<ArticleFilterRequest, Pageable, FilteredArticleAndAggregationResponse> filterFunc =
        buildFilterFunction(expectedArticles);

    Mockito.when(ivdsPerfectMatchArtTaskExecutor
        .executeGetPriceTask(Mockito.eq(user), Mockito.eq(expectedArticles.getContent()),
            Mockito.eq(expectedArticles), Mockito.any(), Mockito.any()))
    .thenReturn(expectedArticles.getContent());

    Mockito.when(ivdsPerfectMatchArtTaskExecutor
        .executePerfectMatchTask(Mockito.eq(user), Mockito.eq(expectedArticles), Mockito.any(),
            Mockito.any()))
    .thenReturn(expectedArticles.getContent());

    final FilteredArticleAndAggregationResponse response =
        articleSearchOptimizer.filterAllOptimized(filterFunc, request);

    Assert.assertThat(response, Matchers.notNullValue());
  }

  private static BiFunction<ArticleFilterRequest, Pageable,
    FilteredArticleAndAggregationResponse> buildFilterFunction(
        Page<ArticleDocDto> expectedArticles) {
    return (r, p) -> {
      FilteredArticleAndAggregationResponse res = new FilteredArticleAndAggregationResponse();
      res.setArticles(expectedArticles);
      res.setAggregations(Maps.newHashMap());
      return res;
    };
  }

  @Test
  public void givenSucceedFilterOptimizedWhenHasAllOptimizedArticlesWithoutERP() {
    final Page<ArticleDocDto> articles = DataProvider.buildArticles(PageUtils.DEF_PAGE);
    executeTestFilterOptimized(articles.getContent(), articles, false);
  }

  @Test
  public void givenSucceedFilterOptimizedWhenHasAllOptimizedArticles() {
    final Page<ArticleDocDto> articles = DataProvider.buildArticles(PageUtils.DEF_PAGE);
    executeTestFilterOptimized(articles.getContent(), articles, true);
  }

  private void executeTestFilterOptimized(List<ArticleDocDto> allOptimizedArticles,
      Page<ArticleDocDto> expectedArticles, boolean fullRequest) {
    // Given
    final String keyWork = "oc90";
    final UserInfo user = DataProvider.buildUserInfo();
    final ArticleFilterRequest request = ArticleFilterRequest.builder()
        .keyword(keyWork).fullRequest(fullRequest).build();
    BiFunction<ArticleFilterRequest, Pageable, FilteredArticleAndAggregationResponse> filterFunc =
        buildFilterFunction(expectedArticles);

    Mockito.when(ivdsPerfectMatchArtTaskExecutor
            .executePerfectMatchTask(Mockito.eq(user), Mockito.eq(expectedArticles), Mockito.any(),
                Mockito.any()))
        .thenReturn(expectedArticles.getContent());

    // When
    final FilteredArticleAndAggregationResponse response =
        articleSearchOptimizer
            .filterOptimized(user, allOptimizedArticles, filterFunc, request, PageUtils.DEF_PAGE,
                Optional.empty());

    // Then
    Assert.assertThat(response, Matchers.notNullValue());
    Mockito.verify(ivdsPerfectMatchArtTaskExecutor).executePerfectMatchTask(Mockito.eq(user),
        Mockito.eq(expectedArticles), Mockito.any(), Mockito.any());
  }

}
