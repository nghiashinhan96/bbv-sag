package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.article.GenArtIdAndReferenceArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.ReferenceAndArtNumSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class GenArtIdAndReferenceArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private GenArtIdAndReferenceArticleSearchServiceImpl service;

  private ReferenceAndArtNumSearchCriteria criteria;

  @Override
  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    criteria = SagBeanUtils.map(new KeywordArticleSearchCriteria("714098190219", ArrayUtils.EMPTY_STRING_ARRAY),
      ReferenceAndArtNumSearchCriteria.class);
    criteria.setArticleNrs(Arrays.asList("95 38 07-71", "714098190219"));
    Map<String, List<String>> refAndGaMap = new HashMap<>();
    refAndGaMap.put("1511", Arrays.asList("98190219", "8XU144429011", "11994"));
  }

  @Test
  public void testSearchFunction() {
    Page<ArticleDoc> result = service.search(criteria, Pageable.unpaged());
    Assert.assertThat(result.hasContent(), Matchers.is(false));
  }

  @Test
  public void testFilterFunction() {
    ArticleFilteringResponse result = service.filter(criteria, Pageable.unpaged());
    Assert.assertThat(result.hasContent(), Matchers.is(false));
  }
}
