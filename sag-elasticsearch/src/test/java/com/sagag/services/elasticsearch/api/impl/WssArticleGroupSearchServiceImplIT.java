package com.sagag.services.elasticsearch.api.impl;

import static org.junit.Assert.assertThat;

import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.WssArticleGroupSearchService;
import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchCriteria;
import com.sagag.services.elasticsearch.criteria.wss.WssArticlegroupSearchTermCriteria;
import com.sagag.services.elasticsearch.domain.wss.WssArticleGroupDoc;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Locale;

/**
 * Integration test class for Elasticsearch WSS Article group service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@ChEshopIntegrationTest
public class WssArticleGroupSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private WssArticleGroupSearchService wssArticleSearchService;

  @Override
  @Before
  public void setup() {
    LocaleContextHolder.setLocale(new Locale("de", "ch"));
  }

  @Test
  public void shouldGetTop10WssArticleGroups() {
    List<WssArticleGroupDoc> top10WssArticleGroups = wssArticleSearchService.getTop10ArticleGroup();
    assertThat(top10WssArticleGroups.size(), Matchers.greaterThanOrEqualTo(0));
  }

  @Test
  public void testSearchWssArticleGroup_WithExactCriteria() {
    final PageRequest pageable = PageUtils.defaultPageable(10);
    final WssArticlegroupSearchTermCriteria searchTerm =
        WssArticlegroupSearchTermCriteria.builder().articleGroup("1-11").articleGroupDesc("Scheibenbremssätze").build();
    final WssArticleGroupSearchCriteria criteria =
        WssArticleGroupSearchCriteria.builder().searchTerm(searchTerm).build();
    final Page<WssArticleGroupDoc> articleGroups =
        wssArticleSearchService.searchWssArticleGroupByCriteria(criteria, pageable).getArticleGroups();

    Assert.assertThat(articleGroups, Matchers.notNullValue());
    Assert.assertThat(articleGroups.getTotalElements(), Is.is(1L));
  }

  @Test
  public void testSearchWssArticleGroup_WithWildCardCriteria() {
    final PageRequest pageable = PageUtils.MAX_PAGE;
    final WssArticlegroupSearchTermCriteria searchTerm =
        WssArticlegroupSearchTermCriteria.builder().articleGroup("1-11*").build();
    final WssArticleGroupSearchCriteria criteria =
        WssArticleGroupSearchCriteria.builder().searchTerm(searchTerm).build();
    final Page<WssArticleGroupDoc> articleGroups =
        wssArticleSearchService.searchWssArticleGroupByCriteria(criteria, pageable).getArticleGroups();

    Assert.assertThat(articleGroups, Matchers.notNullValue());
    Assert.assertThat(articleGroups.getTotalElements(), Matchers.greaterThanOrEqualTo(0l));
  }
}
