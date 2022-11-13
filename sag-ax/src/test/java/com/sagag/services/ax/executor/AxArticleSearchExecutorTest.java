package com.sagag.services.ax.executor;

import com.sagag.services.article.api.attachedarticle.AttachedArticleHandler;
import com.sagag.services.article.api.executor.ArticleErpExternalExecutorBuilders;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.ax.availability.filter.impl.AxAvailabilitiesFilterContext;
import com.sagag.services.ax.availability.stock.ArticleIsOnStockAvailabilitiesFilter;
import com.sagag.services.ax.callable.common.AxErpArticleFilterCallableCreatorImpl;
import com.sagag.services.ax.executor.helper.AxArticleSearchHelper;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.executor.ThreadManager;
import com.sagag.services.domain.article.ArticleDocDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class AxArticleSearchExecutorTest {

  private static final SupportedAffiliate AFFILIATE = SupportedAffiliate.DERENDINGER_AT;
  private static final String CUST_NR = "1100005";

  @InjectMocks
  private AxArticleSearchExecutor executor;

  @Mock
  private ThreadManager threadManager;
  @Mock
  private ErpCallableCreator erpArticleFilterCallableCreator;
  @Mock
  private ArticleIsOnStockAvailabilitiesFilter articleAvailabilitiesFilter;
  @Mock
  private List<ErpCallableCreator> erpAsyncCallableCreators;
  @Mock
  private AxErpArticleFilterCallableCreatorImpl callableCreatorImpl;
  @Mock
  private AxAvailabilitiesFilterContext availabilitiesFilterContext;
  @Mock
  private ArticleErpExternalExecutorBuilders artErpExternalExecutorBuilder;
  @Mock
  private AttachedArticleHandler attachedArticleHandler;
  @Mock
  private AxArticleSearchHelper axArticleSearchHelper;

  private ArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = ArticleSearchCriteria.builder().affiliate(AFFILIATE).custNr(CUST_NR).build();
  }

  @Test
  public void shouldReturnEmptyIfSendEmptyArticles() {
    final List<ArticleDocDto> articles = executor.execute(criteria);
    Assert.assertThat(articles.isEmpty(), Matchers.is(true));
  }

  @Test
  public void shouldReturnArticlesWithoutFilterAvailabilities() {
    // Given
    criteria.setArticles(Collections.singletonList(new ArticleDocDto()));
    // When
    final List<ArticleDocDto> articles = executor.execute(criteria);
    // Then
    Assert.assertThat(articles.isEmpty(), Matchers.is(false));
  }

  @Test
  public void shouldReturnArticlesWithFilterAvailabilities() {
    // Given
    criteria.setArticles(Collections.singletonList(new ArticleDocDto()));
    criteria.setUpdateAvailability(true);
    Mockito.when(availabilitiesFilterContext.doFilterAvailabilities(Mockito.any(), Mockito.any()))
        .thenReturn(criteria.getArticles());
    // When
    final List<ArticleDocDto> articles = executor.execute(criteria);
    // Then
    Assert.assertThat(articles.isEmpty(), Matchers.is(false));
    Mockito.verify(availabilitiesFilterContext, Mockito.times(1))
        .doFilterAvailabilities(Mockito.any(), Mockito.any());
  }

  @Test
  public void shouldReturnFilteredArticlesWithFilterAvailabilities() {
    // Given
    criteria.setArticles(Collections.singletonList(new ArticleDocDto()));
    criteria.setFilterArticleBefore(true);
    criteria.setUpdateAvailability(true);
    // When
    final List<ArticleDocDto> articles = executor.execute(criteria);
    // Then
    Assert.assertThat(articles.isEmpty(), Matchers.is(true));
    Mockito.verify(erpArticleFilterCallableCreator,
        Mockito.times(0)).create(Mockito.any(), Mockito.any());
    Mockito.verify(availabilitiesFilterContext, Mockito.times(0))
        .doFilterAvailabilities(Mockito.any(), Mockito.any());
  }

  @Test
  public void shouldReturnEmptyArticlesAfterErpArticleFiltering() {
    // Given
    criteria.setArticles(Collections.singletonList(new ArticleDocDto()));
    criteria.setFilterArticleBefore(true);
    criteria.setUpdateAvailability(true);
    List<ArticleDocDto> arts = Collections.singletonList(mock(ArticleDocDto.class));
    Mockito.when(axArticleSearchHelper.filterArticles(Mockito.any(), Mockito.any()))
        .thenReturn(arts);

    // When
    final List<ArticleDocDto> articles = executor.execute(criteria);

    // Then
    Assert.assertThat(articles.isEmpty(), Matchers.is(true));
    Mockito.verify(axArticleSearchHelper,
        Mockito.times(1)).filterArticles(Mockito.any(), Mockito.any());
    Mockito.verify(articleAvailabilitiesFilter, Mockito.times(0))
        .doFilterAvailabilities(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
            Mockito.any());
  }
}
