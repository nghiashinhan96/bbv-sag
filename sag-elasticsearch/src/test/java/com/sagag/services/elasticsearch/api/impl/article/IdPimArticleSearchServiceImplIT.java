package com.sagag.services.elasticsearch.api.impl.article;

import static org.hamcrest.core.Is.is;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.article.IdPimArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.utils.AggregationUtils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class IdPimArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private IdPimArticleSearchServiceImpl service;

  private static final String GAID_STRING = "3280";

  @Override
  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void testGetSupplierGaIdAggsByArticleNumberWithEmpty() {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(
        StringUtils.EMPTY, LOCKS_DCH);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(response.getAggregations().isEmpty());
  }

  @Test
  @Ignore("Data should be changed")
  public void testGetSupplierGaIdAggsByArticleIdsagsys() {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria("1000791309", LOCKS_DCH);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(AggregationUtils.anyMatch(response.getAggregations(), GAID_STRING));
  }

  @Test
  public void testGetSupplierGaIdAggsByArticleIdsagsysWithSpace() {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(StringUtils.SPACE, LOCKS_DCH);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(response.getAggregations().isEmpty());
  }

  @Test
  public void testGetSupplierGaIdAggsByArticleIdsagsysWithEmpty() {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS_DCH);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(response.getAggregations().isEmpty());
  }

  @Test
  public void testGetSupplierGaIdAggsBySearchFreetextWithSpace() {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(StringUtils.SPACE, LOCKS_DCH);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(response.getAggregations().isEmpty());
  }

  @Test
  public void testSearchArticlesByIdsagsysSupplierOrGaidNotFoundWithInvalidGaid() {
    final String idSagsys = "1000301977";
    List<String> gaIds = Arrays.asList("911");
    List<String> suppliers = Arrays.asList("SAINT GOBA");
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(idSagsys, LOCKS_DCH);
    criteria.setGaIds(gaIds);
    criteria.setSupplierRaws(suppliers);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    final Page<ArticleDoc> articles = response.getArticles();
    Assert.assertThat(articles.getNumberOfElements(), is(0));
  }

  @Test
  public void testSearchArticlesByIdsagsysSupplierOrGaidNotFoundWithInvalidSupplier() {
    final String idSagsys = "1000301977";
    List<String> gaIds = Arrays.asList("1191");
    List<String> suppliers = Arrays.asList("champa");
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(idSagsys, LOCKS_DCH);
    criteria.setGaIds(gaIds);
    criteria.setSupplierRaws(suppliers);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    final Page<ArticleDoc> articles = response.getArticles();
    Assert.assertThat(articles.getNumberOfElements(), is(0));
  }

  @Test
  public void testSearchArticlesByIdsagsysSupplierOrGaidNotFoundWithEmpty() {

    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS_DCH);
    criteria.setGaIds(Collections.emptyList());
    criteria.setSupplierRaws(Collections.emptyList());
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    final Page<ArticleDoc> articles = response.getArticles();
    Assert.assertThat(articles.getNumberOfElements(), is(0));
  }


  @Test
  public void testSearchArticlesByIdsagsysSupplierOrGaid() {
    final String idSagsys = "1000301977";
    List<String> gaIds = Arrays.asList("1191");
    List<String> suppliers = Arrays.asList("SAINT GOBA");
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(idSagsys, LOCKS_DCH);
    criteria.setGaIds(gaIds);
    criteria.setSupplierRaws(suppliers);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);

    final Page<ArticleDoc> articles = response.getArticles();

    boolean allMatch = articles.getContent().parallelStream()
      .allMatch(t -> criteria.getGaIds().contains(t.getGaId())
        && criteria.getSupplierRaws().contains(t.getSupplier())
        && Objects.equals(t.getIdSagsys(), idSagsys));

    Assert.assertNotNull(articles);
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSearchArticlesByIdsagsysSupplierOrGaidWithoutGaid() {
    final String idSagsys = "1000301977";
    List<String> suppliers = Arrays.asList("SAINT GOBA");

    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(idSagsys, LOCKS_DCH);
    criteria.setSupplierRaws(suppliers);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);

    final Page<ArticleDoc> articles = response.getArticles();

    boolean allMatch = articles.getContent().parallelStream()
      .allMatch(t -> criteria.getSupplierRaws().contains(t.getSupplier())
        && Objects.equals(t.getIdSagsys(), idSagsys));

    Assert.assertNotNull(articles);
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSearchArticlesByIdsagsysSupplierOrGaidWithoutSupplier() {
    final String idSagsys = "1000301977";
    List<String> gaIds = Arrays.asList("1191");
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(idSagsys, LOCKS_DCH);
    criteria.setGaIds(gaIds);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);

    final Page<ArticleDoc> articles = response.getArticles();

    boolean allMatch = articles.getContent().parallelStream()
      .allMatch(t -> criteria.getGaIds().contains(t.getGaId())
        && Objects.equals(t.getIdSagsys(), idSagsys));

    Assert.assertNotNull(articles);
    Assert.assertTrue(allMatch);
  }

  @Test
  @Ignore("Data should be changed")
  public void testGetSupplierGaIdAggsByIdSagsysAndFilter() {
    String idSagsys = "1000791309";
    String supplier = "HC PARTS";

    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(idSagsys, LOCKS_DCH);
    criteria.setSupplierRaws(Arrays.asList(supplier));
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);

    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(AggregationUtils.anyMatch(response.getAggregations(), GAID_STRING));
  }

  @Test
  public void testSearchArticlesByIdsagsysSupplierOrGaidNotFoundWithInvalidIdsasys() {
    final String idSagsys = "101020877";
    List<String> gaIds = Arrays.asList("1191");
    List<String> suppliers = Arrays.asList("SAINT GOBA");
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(idSagsys, LOCKS_DCH);
    criteria.setGaIds(gaIds);
    criteria.setSupplierRaws(suppliers);
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);

    final Page<ArticleDoc> articles = response.getArticles();

    Assert.assertThat(articles.getNumberOfElements(), is(0));
  }
}
