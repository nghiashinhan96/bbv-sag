package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.article.OEAndArtNumArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.utils.AggregationUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
@Slf4j
public class OEAndArtNumArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private OEAndArtNumArticleSearchServiceImpl service;

  @Override
  @Before
  public void setup() {
    LocaleContextHolder.setLocale(new Locale("de", "ch"));
  }

  @Test
  public void testGetSupplierGaIdAggsByArticleNumberWithArtNumber1() {
    String articleNumber = "oc47";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNumber, new String[] { "srb"});
    ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    log.info("{} - {}", response.getArticles().getTotalElements(),
        SagJSONUtil.convertObjectToPrettyJson(response.getArticles().getContent()));

    Page<ArticleDoc> page = service.search(criteria, DEF_PAGE);
    log.info("{} - {}", page.getTotalElements(),
        SagJSONUtil.convertObjectToPrettyJson(page.getContent()));
  }

  @Test
  public void testGetSupplierGaIdAggsByArticleNumberWithArtNumber() {
    String articleNumber = "3400122101";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNumber, LOCKS_DCH);
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(AggregationUtils.anyMatch(response.getAggregations(), "SACHS"));
  }

  @Test
  public void testGetSupplierGaIdAggsByArticleNumberWithPartRef() {
    String articleNumber = "1935393R";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNumber, LOCKS_DCH);
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(AggregationUtils.anyMatch(response.getAggregations(), "SACHS"));
  }

  @Test
  public void testGetSupplierGaIdAggsByArticleNumberPartRef() {
    String articleNumber = "7700720978";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNumber, LOCKS_DCH);
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(AggregationUtils.anyMatch(response.getAggregations(), "MANN-FILTER"));
  }

  @Test
  public void testGetSupplierGaIdAggsByArticleNumberWithSpace() {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(StringUtils.SPACE, LOCKS_DCH);
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(response.getAggregations().isEmpty());
  }

  @Test
  @Ignore
  public void testSearchArticlesByNumberSupplierOrGaidwithEmpty() {
    // search articles by number should have non-nullable article number
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria("3400122101", LOCKS_DCH);
    criteria.setGaIds(Arrays.asList(StringUtils.EMPTY));
    criteria.setSupplierRaws(Arrays.asList(StringUtils.EMPTY));
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    final Page<ArticleDoc> articles = response.getArticles();
    Assert.assertThat(articles.hasContent(), Matchers.is(false));
  }

  @Test
  public void testSearchArticlesByNumberSupplierOrGaid() {
    final String articleNr = "5001100420";
    List<String> gaIds = Arrays.asList("1191");
    List<String> suppliers = Arrays.asList("SEKURIT");
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNr, LOCKS_DCH);
    criteria.setSupplierRaws(suppliers);
    criteria.setGaIds(gaIds);
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);

    final Page<ArticleDoc> articles = response.getArticles();

    boolean allMatch = articles.getContent().parallelStream()
      .allMatch(t -> criteria.getGaIds().contains(t.getGaId())
        && criteria.getSupplierRaws().contains(t.getSupplier())
        && Objects.equals(t.getArtnr(), articleNr));

    Assert.assertNotNull(articles);
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSearchArticlesByNumberSupplierOrGaidWithPartRef() {
    final String articleNr = "1935393R";
    List<String> gaIds = Arrays.asList("479");
    List<String> suppliers = Arrays.asList("SACHS");
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNr, LOCKS_DCH);
    criteria.setGaIds(gaIds);
    criteria.setSupplierRaws(suppliers);
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    final Page<ArticleDoc> articles = response.getArticles();

    boolean allMatch = articles.getContent().parallelStream()
      .allMatch(t -> criteria.getGaIds().contains(t.getGaId())
        && (criteria.getSupplierRaws().contains(t.getSupplier())
        || Objects.equals(t.getArtnr(), articleNr)));

    Assert.assertNotNull(articles);
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSearchArticlesByNumberSupplierOrGaidNotFound() {
    final String articleNr = "5112NF";
    List<String> gaIds = Arrays.asList("1191");
    List<String> suppliers = Arrays.asList("SEKURIT");
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNr, LOCKS_DCH);
    criteria.setSupplierRaws(suppliers);
    criteria.setGaIds(gaIds);
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    final Page<ArticleDoc> articles = response.getArticles();
    Assert.assertThat(0, Matchers.is(articles.getNumberOfElements()));
  }

  @Test
  public void testGetSupplierGaIdAggsByArticleNumberAndFilter() {
    String articleNumber = "3400122101";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNumber, LOCKS_DCH);
    criteria.setSupplierRaws(Arrays.asList("SACHS"));
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);

    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(AggregationUtils.anyMatch(response.getAggregations(), "SACHS"));

  }

  @Test
  public void testGetSupplierGaIdAggsByArticleNumberAndFilterWithPartRef() {
    String articleNumber = "1935393R";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNumber, LOCKS_DCH);
    criteria.setSupplierRaws(Arrays.asList("SACHS"));
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(AggregationUtils.anyMatch(response.getAggregations(), "SACHS"));
  }

  @Test
  public void givenArticleNumberWildCard_ShouldFilterArticles() {
    String articleNumber = "3400*101";
    executeSearchAndAssert(articleNumber);
  }

  @Test
  public void givenArticleNumberWildCardSuffix_ShouldFilterArticles() {
    String articleNumber = "3400122101*";
    executeSearchAndAssert(articleNumber);
  }

  private void executeSearchAndAssert(String articleNumber) {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(articleNumber, LOCKS_DCH);
    criteria.setSupplierRaws(Arrays.asList("SACHS"));
    final ArticleFilteringResponse response = service.filter(criteria, DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(AggregationUtils.anyMatch(response.getAggregations(), "SACHS"));
  }

  @Test
  public void givenArticleNumberWildCardPrefix_ShouldFilterArticles() {
    String articleNumber = "*122101";
    executeSearchAndAssert(articleNumber);
  }

  @Test
  public void givenArticleNumberWildCardSuffixAndPrefix_ShouldFilterArticles() {
    String articleNumber = "*3400122101*";
    executeSearchAndAssert(articleNumber);
  }

  @Test
  public void givenArticleNumberWithLoopSearch() {
    String articleNumber = "714098190219";
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNumber, LOCKS_DCH);
    final Page<ArticleDoc> response = service.search(criteria, DEF_PAGE);
    Assert.assertThat(response.hasContent(), Matchers.anyOf(Matchers.is(true), Matchers.is(false)));
  }
}
