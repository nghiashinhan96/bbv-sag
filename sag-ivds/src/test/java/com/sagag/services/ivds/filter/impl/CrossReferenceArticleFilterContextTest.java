package com.sagag.services.ivds.filter.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.hazelcast.api.BrandPriorityCacheService;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.converter.article.ArticleConverter;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.filter.ArticleFilterContext;
import com.sagag.services.ivds.filter.articles.ArticleFilterFactory;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.impl.CrossReferenceArticleFilterImpl;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import java.util.Collections;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CrossReferenceArticleFilterContextTest {

  @InjectMocks
  private CrossReferenceArticleFilterContext filterContext;

  @Mock
  private ArticleFilterContext articleFilterContext;
  @Mock
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;
  @Mock
  private ArticleFilterFactory articleFilterFactory;
  @Mock
  private SupportedAffiliateRepository supportedAffiliateRepo;
  @Mock
  private CrossReferenceArticleFilterImpl crossReferenceArticleFilter;
  @Mock
  private ArticleConverter articleConverter;
  @Mock
  private BrandPriorityCacheService brandPriorityCacheService;

  @Test
  public void shouldCheckSupportMode() {
    // Given
    final FilterMode filterMode = FilterMode.CROSS_REFERENCE;
    // When
    boolean result = filterContext.supportMode(filterMode);
    // Then
    Assert.assertTrue(result);
  }

  @Test
  public void shouldExecuteFilterSuccessfully() {
    // Given
    final UserInfo user = DataProvider.buildUserInfo();
    final FilterMode filterMode = FilterMode.ACCESSORY_LIST;
    final ArticleFilterRequest request = ArticleFilterRequest.builder().build();
    final Pageable pageable = PageUtils.DEF_PAGE;
    final Optional<AdditionalSearchCriteria> searchCriteria = Optional.of(mock(AdditionalSearchCriteria.class));

    ArticleFilteringResponse filteringResponse = new ArticleFilteringResponse();
    filteringResponse.setArticles(DataProvider.buildArticleDocs(pageable));

    when(articleFilterFactory.getArticleFilter(filterMode)).thenReturn(crossReferenceArticleFilter);
    when(crossReferenceArticleFilter.filterArticles(any(),any(),any(),anyBoolean())).thenReturn(filteringResponse);
    when(ivdsArticleTaskExecutors.executeTaskStockOnlyWithoutVehicle(any(), any(), any())).thenReturn(Page.empty());
    when(articleFilterContext.sortArticlesBrandPriority(any(), any())).thenReturn(Collections.emptyList());
    // When
    ArticleFilteringResponseDto result = filterContext.execute(user, filterMode, request, pageable, searchCriteria);
    // Then
    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.getArticles().getTotalElements());
  }
}