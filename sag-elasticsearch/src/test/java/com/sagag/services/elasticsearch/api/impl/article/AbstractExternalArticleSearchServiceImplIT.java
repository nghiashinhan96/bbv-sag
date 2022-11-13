package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.api.impl.articles.ArticleLoopSearchService;
import com.sagag.services.elasticsearch.api.impl.articles.external.ExternalArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.KeywordExternalArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
public class AbstractExternalArticleSearchServiceImplIT {

  @Autowired
  private ExternalArticleSearchService service;

  @Autowired
  @Qualifier("freetextArticleSearchServiceImpl")
  private ArticleLoopSearchService freetextSearchService;

  private KeywordExternalArticleSearchCriteria buildCriteria(String text, Pageable pageable,
    boolean onAgg) {
    final KeywordArticleSearchCriteria kArtSearchCriteria = new KeywordArticleSearchCriteria();
    kArtSearchCriteria.setAffNameLocks(ArrayUtils.EMPTY_STRING_ARRAY);
    kArtSearchCriteria.setText(text);
    kArtSearchCriteria.setAggregated(onAgg);
    final KeywordExternalArticleSearchCriteria criteria =
      SagBeanUtils.map(kArtSearchCriteria, KeywordExternalArticleSearchCriteria.class);
    criteria.setSearchQueries(freetextSearchService.buildLoopQueries(kArtSearchCriteria, pageable));
    criteria.setDisableIgnoreGenArtMatch(true);
    criteria.setFromTextSearch(true);
    return criteria;
  }
  protected void testAndAssertSearchExtArticles(String text) {
    final Pageable pageable = PageUtils.DEF_PAGE;
    final KeywordExternalArticleSearchCriteria criteria = buildCriteria(text, pageable, false);
    final Page<ArticleDoc> articles = service.search(criteria, pageable);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(articles));
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  protected void testAndAssertFilterExtArticles(String text) {
    final Pageable pageable = PageUtils.DEF_PAGE;
    final KeywordExternalArticleSearchCriteria criteria = buildCriteria(text, pageable, true);
    final ArticleFilteringResponse response = service.filter(criteria, pageable);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));
    Assert.assertThat(response.hasContent(), Matchers.is(true));
  }
}
