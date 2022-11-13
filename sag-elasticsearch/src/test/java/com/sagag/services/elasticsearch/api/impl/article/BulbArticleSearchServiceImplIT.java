package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.bulbs.BulbArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.common.BulbConstants;
import com.sagag.services.elasticsearch.criteria.article.BulbArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.BulbArticleResponse;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.utils.BulbDataTestUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
@Slf4j
public class BulbArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private BulbArticleSearchServiceImpl service;

  @Test
  public void testSearchBulbArticlesByCriteriaDefault() {
    final BulbArticleSearchCriteria criteria = BulbArticleSearchCriteria.builder().build();
    final BulbArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);

    assertBulbAggregationFound(response.getAggregations());
  }

  @Test
  public void testSearchBulbArticlesByCriteriaWithFullCriteria() {

    final BulbArticleSearchCriteria criteria = BulbArticleSearchCriteria.builder()
      .suppliers(BulbDataTestUtils.SUPPLIER_HELLA).voltages(BulbDataTestUtils.VOLTAGE_24)
      .watts(BulbDataTestUtils.WATT_10).codes(BulbDataTestUtils.CODE_K100W).build();
    final BulbArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);

    log.debug("aggregations result = {}", response.getAggregations());
    Optional.ofNullable(response.getAggregations())
    .filter(MapUtils::isNotEmpty)
    .ifPresent(aggs -> aggs.forEach((name, items) -> {
      assetAggsFirstValues(aggs, BulbConstants.SUPPLIER_MAP_NAME,
          Matchers.equalTo(BulbDataTestUtils.SUPPLIER_HELLA.get(0).toUpperCase()));

      assetAggsFirstValues(aggs, BulbConstants.VOLTAGE_MAP_NAME,
          Matchers.equalTo(BulbDataTestUtils.VOLTAGE_24.get(0).toUpperCase()));

      assetAggsFirstValues(aggs, BulbConstants.WATT_MAP_NAME,
          Matchers.equalTo(BulbDataTestUtils.WATT_10.get(0).toUpperCase()));

      assetAggsFirstValues(aggs, BulbConstants.CODE_MAP_NAME,
          Matchers.equalTo(BulbDataTestUtils.CODE_K100W.get(0).toUpperCase()));
    }));
  }

  private static void assetAggsFirstValues(Map<String, List<Object>> aggs, String name,
      Matcher<Object> matcher) {
    Optional.ofNullable(aggs.get(name)).filter(CollectionUtils::isNotEmpty)
    .map(items -> items.get(0)).ifPresent(item -> Assert.assertThat(item, matcher));
  }

  @Test
  public void testSearchBulbArticlesByCriteriaWithSupplier() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().suppliers(BulbDataTestUtils.SUPPLIER_HELLA).build();
    final BulbArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);

    assertBulbAggregationFound(response.getAggregations());
  }

  @Test
  public void testSearchBulbArticlesByCriteriaWithVoltage() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().voltages(BulbDataTestUtils.VOLTAGE_24).build();
    final BulbArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);

    assertBulbAggregationFound(response.getAggregations());
  }

  @Test
  public void testSearchBulbArticlesByCriteriaWithWatt() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().watts(BulbDataTestUtils.WATT_10).build();
    final BulbArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);

    assertBulbAggregationFound(response.getAggregations());
  }

  @Test
  public void testSearchBulbArticlesByCriteriaWithCode() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().codes(BulbDataTestUtils.CODE_F2).build();
    final BulbArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);

    assertBulbAggregationFound(response.getAggregations());
  }

  @Test
  public void testSearchBulbArticlesByFilterDefault() {
    final BulbArticleSearchCriteria criteria = BulbArticleSearchCriteria.builder().build();
    final ArticleFilteringResponse response = service.filter(criteria,
        BulbDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  @Test
  public void testSearchBulbArticlesByFilterWithFullCriteria() {
    final BulbArticleSearchCriteria criteria = BulbArticleSearchCriteria.builder()
      .suppliers(BulbDataTestUtils.SUPPLIER_HELLA).voltages(BulbDataTestUtils.VOLTAGE_24)
      .watts(BulbDataTestUtils.WATT_10).codes(BulbDataTestUtils.CODE_K100W).build();
    final ArticleFilteringResponse response = service.filter(criteria,
      BulbDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(0L));
  }

  @Test
  public void testSearchBulbArticlesByFilterWithSupplier() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().suppliers(BulbDataTestUtils.SUPPLIER_HELLA).build();
    final ArticleFilteringResponse response = service.filter(criteria,
      BulbDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  @Test
  public void testSearchBulbArticlesByFilterWithVoltage() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().voltages(BulbDataTestUtils.VOLTAGE_24).build();
    final ArticleFilteringResponse response = service.filter(criteria,
      BulbDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  @Test
  public void testSearchBulbArticlesByFilterWithWatt() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().watts(BulbDataTestUtils.WATT_10).build();
    final ArticleFilteringResponse response = service.filter(criteria,
      BulbDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  @Test
  public void testSearchBulbArticlesByFilterWithCode() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().codes(BulbDataTestUtils.CODE_H2).build();
    final ArticleFilteringResponse response = service.filter(criteria,
      BulbDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  @Test
  public void testSearchBulbArticlesByFilterWithCodes() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().codes(BulbDataTestUtils.CODES_H10W_K10W_R10W)
        .voltages(BulbDataTestUtils.VOLTAGE_24).watts(BulbDataTestUtils.WATT_10).build();
    final ArticleFilteringResponse response = service.filter(criteria,
      BulbDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
    Assert.assertThat(response.getArticles().hasContent(), Matchers.is(true));
  }

  @Test
  public void testSearchBulbArticlesByFilterWithCodesAndSuppliers() {
    final BulbArticleSearchCriteria criteria =
      BulbArticleSearchCriteria.builder().codes(BulbDataTestUtils.CODES_H10W_K10W_R10W)
        .voltages(BulbDataTestUtils.VOLTAGE_24).watts(BulbDataTestUtils.WATT_10).build();
    criteria.setSupplierRaws(BulbDataTestUtils.SUPPLIERS_HELLA_OSRAM);
    final ArticleFilteringResponse response = service.filter(criteria,
      BulbDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
    Assert.assertThat(response.getArticles().hasContent(), Matchers.is(true));
  }

  private void assertArticlesFound(ArticleFilteringResponse response) {
    Assert.assertNotNull(response.getAggregations());
    Assert.assertNotNull(response.getArticles());
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(0L));
    if (MapUtils.isEmpty(response.getAggregations())) {
      return;
    }
    Assert.assertThat(response.getAggregations().getOrDefault(ArticleField.SUPPLIER_RAW.name(),
        Collections.emptyList()).size(), Matchers.greaterThanOrEqualTo(0));
  }

  private void assertBulbAggregationFound(Map<String, List<Object>> aggregations) {
    log.debug("aggregations result = {}", aggregations);

    Assert.assertThat(aggregations, Matchers.notNullValue());
    Assert.assertThat(aggregations.get(BulbConstants.SUPPLIER_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(aggregations.get(BulbConstants.VOLTAGE_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(aggregations.get(BulbConstants.WATT_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(aggregations.get(BulbConstants.CODE_MAP_NAME).size(),
      Matchers.greaterThan(0));
  }
}
