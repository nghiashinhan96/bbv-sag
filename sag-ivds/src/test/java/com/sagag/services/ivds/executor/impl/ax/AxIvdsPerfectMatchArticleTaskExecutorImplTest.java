package com.sagag.services.ivds.executor.impl.ax;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.availability.comparator.AvailabilityArticleComparator;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
public class AxIvdsPerfectMatchArticleTaskExecutorImplTest {

  @InjectMocks
  private AxIvdsPerfectMatchArticleTaskExecutorImpl executor;

  @Mock
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Mock
  private AvailabilityArticleComparator articleAvailabilityComparator;

  @Test
  public void givenSucceedPerfectMatchTaskExecutor() {
    testExecuteTask(DataProvider.buildArticles(PageUtils.DEF_PAGE), PageUtils.DEF_PAGE);
  }

  @Test
  public void givenSucceedPerfectMatchTaskExecutorWithSortedByStock() {
    List<ArticleDocDto> articles = new ArrayList<>();
    final ArticleDocDto articleWithNullStock = new ArticleDocDto();
    articleWithNullStock.setArtid(UUID.randomUUID().toString());
    articleWithNullStock.setIdSagsys(articleWithNullStock.getArtid());

    final ArticleDocDto articleWithStock1 = new ArticleDocDto();
    articleWithStock1.setArtid(UUID.randomUUID().toString());
    articleWithStock1.setIdSagsys(articleWithStock1.getArtid());
    articleWithStock1.setStock(ArticleStock.builder().stock(18d).build());

    final ArticleDocDto articleWithStock2 = new ArticleDocDto();
    articleWithStock2.setArtid(UUID.randomUUID().toString());
    articleWithStock2.setIdSagsys(articleWithStock2.getArtid());
    articleWithStock2.setStock(ArticleStock.builder().stock(10d).build());
    articles.add(articleWithNullStock);
    articles.add(articleWithStock1);
    articles.add(articleWithStock2);
    PageImpl<ArticleDocDto> articlesPage =
        new PageImpl<>(articles, PageUtils.DEF_PAGE, articles.size());
    final UserInfo user = DataProvider.buildUserInfo();

    mockRelatedServices(articlesPage, user);

    final List<ArticleDocDto> updatedArticles =
        executor.executePerfectMatchTask(user, articlesPage, PageUtils.DEF_PAGE, Optional.empty());

    Assert.assertThat(updatedArticles, Matchers.notNullValue());
    Assert.assertThat(updatedArticles.get(0).getStock().getStockAmount(), Matchers.is(18d));
    Assert.assertThat(updatedArticles.get(1).getStock().getStockAmount(), Matchers.is(10d));
    Assert.assertThat(updatedArticles.get(2).getStock(), Matchers.nullValue());
  }

  @Test
  public void givenSucceedPerfectMatchTaskExecutorWithEmptyPage() {
    testExecuteTask(Page.empty(), PageUtils.DEF_PAGE);
  }

  private void testExecuteTask(Page<ArticleDocDto> articles,
      Pageable pageable) {

    final UserInfo user = DataProvider.buildUserInfo();

    mockRelatedServices(articles, user);

    final List<ArticleDocDto> updatedArticles =
        executor.executePerfectMatchTask(user, articles, pageable, Optional.empty());

    Assert.assertThat(updatedArticles, Matchers.notNullValue());

    Mockito.verify(ivdsArticleTaskExecutors, Mockito.times(articles.hasContent() ? 1 : 0))
    .executeTaskAvailabilitiesOnlyWithoutVehicle(Mockito.eq(user), Mockito.any(), Mockito.any());

    Mockito.verify(articleAvailabilityComparator, Mockito.times(articles.hasContent() ? 1 : 0))
    .sortPerfectMatchArticles(Mockito.any());
  }

  private void mockRelatedServices(Page<ArticleDocDto> articles, final UserInfo user) {
    Mockito.when(ivdsArticleTaskExecutors.executeTaskStockOnlyWithoutVehicle(Mockito.eq(user),
        Mockito.any(), Mockito.any())).thenReturn(articles);

    Mockito.when(articleAvailabilityComparator.sortPerfectMatchArticles(Mockito.any()))
    .thenReturn(articles.getContent());
  }
}
