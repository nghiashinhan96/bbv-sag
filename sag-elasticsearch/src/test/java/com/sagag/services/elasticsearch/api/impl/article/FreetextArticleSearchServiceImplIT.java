package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.article.FreetextArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.enums.ArticleField;

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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
@Slf4j
public class FreetextArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private FreetextArticleSearchServiceImpl service;

  @Override
  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void testGetGaIdAggsBySearchFreetext() {
    String freeText = "audi";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(freeText, LOCKS_DCH);
    final ArticleFilteringResponse response = service.filter(criteria, PageUtils.DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
  }

  @Test
  @Ignore("We don't provide supplier aggregation anymore")
  public void testGetSupplierGaIdAggsBySearchFreetextWithSupplier() {
    String freeText = "zahnriemen";
    final String supplier = "BOSCH";
    List<String> suppliers = Arrays.asList(supplier);
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(freeText, LOCKS_DCH);
    criteria.setSupplierRaws(suppliers);
    final ArticleFilteringResponse response = service.filter(criteria, PageUtils.DEF_PAGE);
    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertNotNull(response.getAggregations());
    final List<Object> bucketKeys = response.getAggregations().values().stream()
      .flatMap(items -> items.stream()).map(item -> item.getKey())
      .collect(Collectors.toList());

    Assert.assertThat(bucketKeys.stream().anyMatch(key -> Objects.equals(supplier, key)),
      Matchers.is(true));
  }

  @Test
  public void testGetSupplierGaIdAggsBySearchFreetextWithEmpty() {
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(
        StringUtils.EMPTY, LOCKS_DCH);
    final ArticleFilteringResponse response = service.filter(criteria, PageUtils.DEF_PAGE);
    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(response.getAggregations().isEmpty());
  }

  @Test
  public void testSearchArticlesByFreeTextWithPnrn() {
    final String freeText = "51606SS0902";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(freeText, LOCKS_DCH);
    final ArticleFilteringResponse response = service.filter(criteria, PageUtils.DEF_PAGE);
    final Page<ArticleDoc> articles = response.getArticles();
    Assert.assertNotNull(articles);
  }

  @Test
  @Ignore("We don't provide supplier aggregation anymore")
  public void testSearchArticlesByFreeTextWithSupplierOrGaIdFilter() {
    final String freeText = "Weg-Nut-Stoßdämpfer";
    List<String> gaIds = Arrays.asList("854");
    List<String> suppliers = Arrays.asList("SACHS");
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(freeText, LOCKS_DCH);
    criteria.setGaIds(gaIds);
    criteria.setSupplierRaws(suppliers);
    final ArticleFilteringResponse response = service.filter(criteria, PageUtils.DEF_PAGE);
    final Page<ArticleDoc> articles = response.getArticles();

    boolean allMatch = articles.getContent().parallelStream()
      .allMatch(t -> gaIds.contains(t.getGaId()) && suppliers.contains(t.getSupplier()));

    Assert.assertNotNull(articles);
    Assert.assertTrue(allMatch);
  }

  @Test
  @Ignore("Need prod_alldata index")
  public void testGetSupplierGaIdAggsByFreeTextSupplierGaId() {
    final String freeText = "Weg-Nut-Stoßdämpfer";
    final String gaid = "150";
    final String supplier = "SACHS";
    List<String> gaIds = Arrays.asList(gaid);
    List<String> suppliers = Arrays.asList(supplier);

    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(freeText, LOCKS_DCH);
    criteria.setGaIds(gaIds);
    criteria.setSupplierRaws(suppliers);
    final ArticleFilteringResponse response = service.filter(criteria, PageUtils.DEF_PAGE);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response.getArticles().getContent()));
    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertNotNull(response.getAggregations());
  }

  @Test
  @Ignore("We don't provide supplier aggregation anymore")
  public void testGetSupplierGaIdAggsByFreeTextSupplierGaIdWithGaid() {
    final String freeText = "Weg-Nut-Stoßdämpfer";
    List<String> gaIds = Arrays.asList("854");

    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(freeText, LOCKS_DCH);
    criteria.setGaIds(gaIds);
    final ArticleFilteringResponse response = service.filter(criteria, PageUtils.DEF_PAGE);

    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(response.getAggregations().get(ArticleField.GA_ID.name()).parallelStream()
      .anyMatch(p -> criteria.getGaIds().contains(p.getKey())));
  }

  @Test
  @Ignore
  public void testGetSupplierGaIdAggsByFreeTextSupplierGaIdWithSupplier() {
    final String freeText = "Weg-Nut-Stoßdämpfer";
    List<String> suppliers = Arrays.asList("SACHS");

    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(freeText, LOCKS_DCH);
    criteria.setSupplierRaws(suppliers);
    final ArticleFilteringResponse response = service.filter(criteria, PageUtils.DEF_PAGE);

    Assert.assertNotNull(response.getAggregations());
    Assert.assertTrue(response.getAggregations().get(ArticleField.SUPPLIER_RAW.name())
      .parallelStream().anyMatch(p -> criteria.getSupplierRaws().contains(p.getKey())));
  }

  @Test
  public void testGetSupplierGaIdAggsByFreeTextSupplierGaIdWithEmpty() {
    final String freeText = "Weg-Nut-Stoßdämpfer";
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(freeText, LOCKS_DCH);
    final ArticleFilteringResponse response = service.filter(criteria, PageUtils.DEF_PAGE);

    Assert.assertNotNull(response.getAggregations());
    Assert.assertThat(response.getAggregations().getOrDefault(ArticleField.SUPPLIER.name(),
      Collections.emptyList()).size(),
      Matchers.greaterThanOrEqualTo(0));
    Assert.assertThat(response.getAggregations().getOrDefault(ArticleField.GA_ID.name(),
      Collections.emptyList()).size(),
      Matchers.greaterThanOrEqualTo(0));
  }

}
