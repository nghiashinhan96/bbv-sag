package com.sagag.services.ivds.freetext.impl;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.response.FreetextResponseDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class ArticleFreetextSearchServiceImplTest {

  @InjectMocks
  private ArticleFreetextSearchServiceImpl service;

  @Mock
  private IvdsArticleService articleService;

  @Test
  public void testSearchArticlesFreetext() {
    final FreetextSearchRequest request = DataProvider.buildArticleSearchFreetext("audi");
    final FreetextResponseDto response = new FreetextResponseDto();

    boolean isSupported = service.support(request.getSearchOptions());
    Assert.assertThat(isSupported, Matchers.is(true));

    final ArticleFilteringResponseDto filterResponse = new ArticleFilteringResponseDto();
    filterResponse.setArticles(new PageImpl<>(Arrays.asList(new ArticleDocDto())));

    Mockito.when(articleService.searchFreetext(Mockito.eq(request)))
    .thenReturn(filterResponse);
    service.search(request, response);

    Assert.assertThat(response.getArticles().hasContent(), Matchers.is(true));

    Mockito.verify(articleService, Mockito.times(1)).searchFreetext(request);
  }
}
