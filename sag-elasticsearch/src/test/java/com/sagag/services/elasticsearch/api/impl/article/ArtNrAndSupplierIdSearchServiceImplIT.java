package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.article.ArtNrAndSupplierIdSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class ArtNrAndSupplierIdSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private ArtNrAndSupplierIdSearchServiceImpl service;

  @Override
  @Before
  public void setup() {
    LocaleContextHolder.setLocale(new Locale("de", "ch"));
  }

  @Test
  public void testFilterArtByKeywordAndSupplierId() {
    String text = "030.793 Dirko";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(text, LOCKS_DCH);
    criteria.setIdDlnr("10");
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertTrue(response.getArticles().getContent().size() > 0);
    Assert.assertTrue(response.getArticles().stream()
        .filter(art -> StringUtils.equals(art.getArtid(), "1001900930")).findAny().isPresent());
  }


  @Test
  public void testFilterArtByKeywordAndSupplierId_notFound() {
    String text = "030.793 Dirko";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(text, LOCKS_DCH);
    criteria.setIdDlnr("12");
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertEquals(0, response.getArticles().getContent().size());
  }

  @Test
  public void testSearchArtByKeywordAndSupplierId() {
    String text = "030.793 Dirko";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(text, LOCKS_DCH);
    criteria.setIdDlnr("10");
    final Page<ArticleDoc> response = service.search(criteria, DEF_PAGE);
    Assert.assertTrue(response.getContent().size() > 0);
    Assert.assertTrue(response.getContent().stream()
        .filter(art -> StringUtils.equals(art.getArtid(), "1001900930")).findAny().isPresent());
  }

  @Test
  public void testSearchArtByKeywordAndSupplierId_notFound() {
    String text = "030.793 Dirko";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(text, LOCKS_DCH);
    criteria.setIdDlnr("13");
    final Page<ArticleDoc> response = service.search(criteria, DEF_PAGE);
    Assert.assertEquals(0, response.getContent().size());
  }

}
