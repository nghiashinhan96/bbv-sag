package com.sagag.services.ivds.filter.cached;

import com.sagag.services.ivds.filter.articles.ArticleFilterFactory;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.impl.NonCachedArticlePartListFilterContext;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class NonCachedArticlePartListFilterContextTest {

  @InjectMocks
  private NonCachedArticlePartListFilterContext nonCachedArticlePartListFilterContext;

  @Mock
  private ArticleFilterFactory articleFilterFactory;

  @Test
  public void testInitContext() {
    Assert.assertThat(nonCachedArticlePartListFilterContext, Matchers.notNullValue());
  }

  @Test
  public void testSupportMode() {
    Assert.assertThat(nonCachedArticlePartListFilterContext.supportMode(FilterMode.BAR_CODE),
        Matchers.is(false));

    Assert.assertThat(nonCachedArticlePartListFilterContext.supportMode(FilterMode.TYRES_SEARCH),
        Matchers.is(false));

    Assert.assertThat(nonCachedArticlePartListFilterContext.supportMode(FilterMode.ID_SAGSYS),
        Matchers.is(false));

    Assert.assertThat(nonCachedArticlePartListFilterContext.supportMode(FilterMode.ARTICLE_NUMBER),
        Matchers.is(false));

    Assert.assertThat(nonCachedArticlePartListFilterContext.supportMode(FilterMode.FREE_TEXT),
        Matchers.is(false));

    Assert.assertThat(nonCachedArticlePartListFilterContext.supportMode(FilterMode.PART_LIST),
        Matchers.is(true));
  }
}
