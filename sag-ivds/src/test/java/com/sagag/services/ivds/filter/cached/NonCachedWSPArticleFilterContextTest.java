package com.sagag.services.ivds.filter.cached;

import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.ivds.filter.aggregation.impl.GenArtAndSubAggregationResultBuilderImpl;
import com.sagag.services.ivds.filter.articles.ArticleFilterFactory;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.impl.NonCachedWSPArticleFilterContext;
import com.sagag.services.ivds.filter.optimizer.PerfectMatchArticleSearchOptimizer;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class NonCachedWSPArticleFilterContextTest {

  @InjectMocks
  private NonCachedWSPArticleFilterContext nonCachedWSPArticleFilterContext;

  @Mock
  private PerfectMatchArticleSearchOptimizer perfectMatchArtSearchOptimizer;

  @Mock
  private ArticleFilterFactory articleFilterFactory;

  @Mock
  private GenArtAndSubAggregationResultBuilderImpl genArtAndSubAggResultBuilder;

  @Mock
  private ContextService contextService;

  @Test
  public void testInitContext() {
    Assert.assertThat(nonCachedWSPArticleFilterContext, Matchers.notNullValue());
  }

  @Test
  public void testSupportMode() {
    Assert.assertThat(nonCachedWSPArticleFilterContext.supportMode(FilterMode.BAR_CODE),
        Matchers.is(false));

    Assert.assertThat(nonCachedWSPArticleFilterContext.supportMode(FilterMode.TYRES_SEARCH),
        Matchers.is(false));

    Assert.assertThat(nonCachedWSPArticleFilterContext.supportMode(FilterMode.ID_SAGSYS),
        Matchers.is(false));

    Assert.assertThat(nonCachedWSPArticleFilterContext.supportMode(FilterMode.ARTICLE_NUMBER),
        Matchers.is(false));

    Assert.assertThat(nonCachedWSPArticleFilterContext.supportMode(FilterMode.FREE_TEXT),
        Matchers.is(false));

    Assert.assertThat(nonCachedWSPArticleFilterContext.supportMode(FilterMode.WSP_SEARCH),
        Matchers.is(true));
  }

}
