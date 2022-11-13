package com.sagag.services.ivds.filter.articles.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.CrossReferenceArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.filter.CrossReferenceRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CrossReferenceArticleFilterImplTest {

  @InjectMocks
  private CrossReferenceArticleFilterImpl articleFilter;

  @Mock
  private IArticleSearchService<CrossReferenceArticleSearchCriteria, Page<ArticleDoc>> crossReferenceFiltering;

  @Test
  public void shouldGetFilterMode() {
    // When
    FilterMode result = articleFilter.mode();
    // Then
    Assert.assertEquals(FilterMode.CROSS_REFERENCE, result);
  }

  @Test
  public void shouldFilterArticlesSuccessfully() {
    // Given
    CrossReferenceRequest crossReferenceRequest = CrossReferenceRequest.builder()
        .articleNumber("fdb1788").brandId("62").build();
    final ArticleFilterRequest request = ArticleFilterRequest.builder()
        .crossReferenceRequest(crossReferenceRequest).build();
    final Pageable pageable = PageUtils.DEF_PAGE;

    ArticleFilteringResponse filteringResponse = new ArticleFilteringResponse();
    filteringResponse.setArticles(DataProvider.buildArticleDocs(pageable));

    when(crossReferenceFiltering.filter(any(), any())).thenReturn(filteringResponse);
    // When
    ArticleFilteringResponse result = articleFilter.filterArticles(request, pageable, new String[0], false);
    // Then
    Assert.assertNotNull(result);
    Assert.assertNull(result.getAggregations());
    Assert.assertEquals(2, result.getArticles().getTotalElements());
  }
}