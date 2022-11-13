package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.article.CrossReferenceSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.CrossReferenceArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class CrossReferenceSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private CrossReferenceSearchServiceImpl service;

  @Override
  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void shouldFilterSuccessfully() {
    // Given
    final CrossReferenceArticleSearchCriteria criteria = new CrossReferenceArticleSearchCriteria();
    criteria.setArtNr("fdb1788");
    criteria.setBrandId("62");
    final Pageable pageable = PageUtils.DEF_PAGE;
    // When
    ArticleFilteringResponse result = service.filter(criteria, pageable);
    // Then
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertNull(result.getAggregations());
  }
}
