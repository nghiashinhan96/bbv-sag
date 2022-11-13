package com.sagag.services.elasticsearch.api.impl.article;

import static com.sagag.services.elasticsearch.utils.AssertESUtils.assertExistAndEqualsOnlyOneValue;
import static com.sagag.services.elasticsearch.utils.AssertESUtils.getTotalElements;
import static com.sagag.services.elasticsearch.utils.BatteryDataTestUtils.DF_PAGE_REQUEST;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.batteries.BatteryArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.common.BatteryConstants;
import com.sagag.services.elasticsearch.criteria.article.BatteryArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.BatteryArticleResponse;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.utils.AssertESUtils;
import com.sagag.services.elasticsearch.utils.BatteryDataTestUtils;
import com.sagag.services.elasticsearch.utils.EsLogUtils;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
@Slf4j
public class BatteryArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private BatteryArticleSearchServiceImpl service;

  @Test
  public void testSearchAllBatteryArticlesByCriteria() {
    BatteryArticleResponse response = service.search(BatteryArticleSearchCriteria.builder().build(),
        DF_PAGE_REQUEST);

    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertThat(response.getArticles().hasContent(), Matchers.equalTo(true));
    Assert.assertThat(response.hasAggregations(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBatteryArticlesByCriteriaWithVoltageAndLength() {
    BatteryArticleSearchCriteria criteria =
      BatteryArticleSearchCriteria.builder().voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
        .lengths(BatteryDataTestUtils.LENGTH_513).build();
    BatteryArticleResponse response = service.search(criteria, DF_PAGE_REQUEST);

    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());
    Assert.assertThat(response.getArticles().hasContent(), Matchers.equalTo(true));
    Assert.assertThat(response.hasAggregations(), Matchers.equalTo(true));

    AssertESUtils.assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.VOLTAGE_MAP_NAME),
      BatteryDataTestUtils.DEFAULT_VOLTAGE.get(0));
  }

  @Test
  public void testSearchBatteryArticlesByCriteriaWithDefaultVoltageAndAmpereHour() {
    BatteryArticleSearchCriteria criteria =
      BatteryArticleSearchCriteria.builder().voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
        .ampereHours(BatteryDataTestUtils.AMPERE_HOUR_120)
        .build();
    BatteryArticleResponse response = service.search(criteria, DF_PAGE_REQUEST);

    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertThat(response.getArticles().hasContent(), Matchers.equalTo(true));
    Assert.assertThat(response.hasAggregations(), Matchers.equalTo(true));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.VOLTAGE_MAP_NAME),
      BatteryDataTestUtils.DEFAULT_VOLTAGE.get(0));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.AMPERE_HOUR_MAP_NAME),
      BatteryDataTestUtils.AMPERE_HOUR_120.get(0));
  }

  @Test
  public void testSearchBatteryArticlesByCriteriaWithDefaultVoltageAndAmpereHourAndDimension() {
    BatteryArticleSearchCriteria criteria =
      BatteryArticleSearchCriteria.builder().voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
        .ampereHours(BatteryDataTestUtils.AMPERE_HOUR_120)
        .heights(BatteryDataTestUtils.HEIGHT_223)
        .widths(BatteryDataTestUtils.WIDTH_189)
        .lengths(BatteryDataTestUtils.LENGTH_513)
        .build();
    BatteryArticleResponse response = service.search(criteria, DF_PAGE_REQUEST);

    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertThat(response.getArticles().hasContent(), Matchers.equalTo(true));
    Assert.assertThat(response.hasAggregations(), Matchers.equalTo(true));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.VOLTAGE_MAP_NAME),
      BatteryDataTestUtils.DEFAULT_VOLTAGE.get(0));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.AMPERE_HOUR_MAP_NAME),
      BatteryDataTestUtils.AMPERE_HOUR_120.get(0));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.LENGTH_MAP_NAME),
      BatteryDataTestUtils.LENGTH_513.get(0));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.HEIGHT_MAP_NAME),
      BatteryDataTestUtils.HEIGHT_223.get(0));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.WIDTH_MAP_NAME),
      BatteryDataTestUtils.WIDTH_189.get(0));
  }

  @Test
  public void testSearchBatteryArticlesByCriteriaNotFoundWithDimension() {
    BatteryArticleSearchCriteria criteria = BatteryArticleSearchCriteria.builder()
      .voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
      .ampereHours(BatteryDataTestUtils.AMPERE_HOUR_1).heights(BatteryDataTestUtils.HEIGHT_215)
      .widths(BatteryDataTestUtils.WIDTH_175).lengths(BatteryDataTestUtils.LENGTH_100).build();
    BatteryArticleResponse response = service.search(criteria, DF_PAGE_REQUEST);

    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertThat(response.getArticles().hasContent(), Matchers.equalTo(false));
  }

  @Test
  public void testSearchBatteryArticlesByCriteriaWithInterconnectionAndPole() {
    BatteryArticleSearchCriteria criteria =
      BatteryArticleSearchCriteria.builder().voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
        .interconnections(BatteryDataTestUtils.INTERCONNECTION_1)
        .typeOfPoles(BatteryDataTestUtils.POLE_3).build();
    BatteryArticleResponse response = service.search(criteria, DF_PAGE_REQUEST);

    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertThat(response.getArticles().hasContent(), Matchers.equalTo(true));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.VOLTAGE_MAP_NAME),
      BatteryDataTestUtils.DEFAULT_VOLTAGE.get(0));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.INTERCONNECTION_MAP_NAME),
      BatteryDataTestUtils.INTERCONNECTION_1.get(0));

    assertExistAndEqualsOnlyOneValue(response.getAggregations().get(BatteryConstants.POLE_MAP_NAME),
      BatteryDataTestUtils.POLE_3.get(0).toUpperCase());
  }

  @Test
  public void testSearchBatteryArticlesByCriteriaWithInterconnection() {
    BatteryArticleSearchCriteria criteria =
      BatteryArticleSearchCriteria.builder().voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
        .interconnections(BatteryDataTestUtils.INTERCONNECTION_1).build();
    BatteryArticleResponse response = service.search(criteria, DF_PAGE_REQUEST);

    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertThat(response.getArticles().hasContent(), Matchers.equalTo(true));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.VOLTAGE_MAP_NAME),
      BatteryDataTestUtils.DEFAULT_VOLTAGE.get(0));

    assertExistAndEqualsOnlyOneValue(
      response.getAggregations().get(BatteryConstants.INTERCONNECTION_MAP_NAME),
      BatteryDataTestUtils.INTERCONNECTION_1.get(0));

  }

  @Test
  public void testSearchBatteryArticlesByCriteria() {

    BatteryArticleSearchCriteria criteria =
      BatteryArticleSearchCriteria.builder().voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
        .interconnections(BatteryDataTestUtils.INTERCONNECTION_1).build();
    ArticleFilteringResponse response = service.filter(criteria,
      BatteryDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertNotNull(response.getAggregations());

    Assert.assertThat(response.getAggregations().get(ArticleField.SUPPLIER_RAW.name()),
      Matchers.not(Matchers.empty()));
    Assert.assertThat(response.getAggregations().get(BatteryConstants.AMPERE_HOUR_MAP_NAME),
      Matchers.not(Matchers.empty()));
  }

  @Test
  @Ignore
  public void testSearchBatteryArticlesByCriteriaWithAmpereHour() {
    BatteryArticleSearchCriteria criteria = BatteryArticleSearchCriteria.builder()
      .voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
      //.interconnections(BatteryDataTestUtils.INTERCONNECTION_1)
      .ampereHours(Arrays.asList("44"))
      .build();

    ArticleFilteringResponse response = service.filter(criteria,
      BatteryDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    Assert.assertNotNull(response.getAggregations());
    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertThat(response.getAggregations().get(ArticleField.SUPPLIER_RAW.name()),
      Matchers.not(Matchers.empty()));
    Assert.assertThat(response.getAggregations().get(BatteryConstants.AMPERE_HOUR_MAP_NAME).size(),
      Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  @Ignore
  public void testSearchBatteryArticlesByCriteriaWithSupplier() {

    BatteryArticleSearchCriteria criteria =
      BatteryArticleSearchCriteria.builder().voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
        .build();

    // Set Filter
    criteria.setSupplierRaws(BatteryDataTestUtils.SUPPLIER_BOSCH);
    ArticleFilteringResponse response = service.filter(criteria,
      BatteryDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    Assert.assertNotNull(response.getAggregations());
    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());
    Assert.assertThat(response.getAggregations().get(BatteryConstants.AMPERE_HOUR_MAP_NAME),
      Matchers.not(Matchers.empty()));
    Assert.assertThat(response.getAggregations().get(ArticleField.SUPPLIER_RAW.name()).size(),
      Matchers.equalTo(1));
  }

  @Test
  public void testSearchBatteryArticlesByCriteriaWithSupplierAndAmpereHour() {

    BatteryArticleSearchCriteria criteria = BatteryArticleSearchCriteria.builder()
      .voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE).ampereHours(Arrays.asList("100"))
      .interconnections(BatteryDataTestUtils.INTERCONNECTION_1)
      .build();

    // Set Filter
    criteria.setSupplierRaws(BatteryDataTestUtils.SUPPLIER_EXIDE_BATTERIES);
    ArticleFilteringResponse response = service.filter(criteria,
      BatteryDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    Assert.assertNotNull(response.getAggregations());
    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());
    Assert.assertThat(response.getAggregations().get(BatteryConstants.AMPERE_HOUR_MAP_NAME).size(),
      Matchers.greaterThanOrEqualTo(0));
    Assert.assertThat(response.getAggregations().get(ArticleField.SUPPLIER_RAW.name()).size(),
      Matchers.greaterThanOrEqualTo(0));
  }

  @Test
  public void testSearchBatteryArticlesByCriteria_WithWithoutStartStop() {

    BatteryArticleSearchCriteria criteria =
      BatteryArticleSearchCriteria.builder().withoutStartStop(true).build();
    ArticleFilteringResponse response = service.filter(criteria,
      BatteryDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    Assert.assertNotNull(response.getAggregations());

    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertThat(response.getAggregations().get(ArticleField.SUPPLIER_RAW.name()),
      Matchers.not(Matchers.empty()));
    Assert.assertThat(response.getAggregations().get(BatteryConstants.AMPERE_HOUR_MAP_NAME),
      Matchers.not(Matchers.empty()));
  }

  @Test
  public void testSearchBatteryArticlesByCriteriaWithAmpereHours() {

    BatteryArticleSearchCriteria criteria = BatteryArticleSearchCriteria.builder()
      .voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
      .ampereHours(Arrays.asList("102", "105"))
      .build();

    // Set Filter
    criteria.setSupplierRaws(BatteryDataTestUtils.SUPPLIER_BOSCH);
    ArticleFilteringResponse response = service.filter(criteria,
      BatteryDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    Assert.assertNotNull(response.getAggregations());
    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());
    Assert.assertThat(response.getAggregations().get(BatteryConstants.AMPERE_HOUR_MAP_NAME).size(),
      Matchers.greaterThanOrEqualTo(0));
  }

  @Test
  public void testSearchBatteryArticlesByCriteriaWithSuppliers() {
    BatteryArticleSearchCriteria criteria = BatteryArticleSearchCriteria.builder()
      .voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
      .ampereHours(Arrays.asList("120"))
      .build();

    // Set Filter
    criteria.setSupplierRaws(BatteryDataTestUtils.SUPPLIER_EXIDE_BATTERIES);
    ArticleFilteringResponse response = service.filter(criteria,
      BatteryDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    Assert.assertNotNull(response.getAggregations());
    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());
    Assert.assertThat(getTotalElements(response), Matchers.greaterThanOrEqualTo(1l));
  }

  @Test
  @Ignore("Testcase is out to date")
  public void testSearchBatteryArticlesByCriteriaWithAmpereHoursAndSuppliers() {

    BatteryArticleSearchCriteria criteria =
      BatteryArticleSearchCriteria.builder().voltages(BatteryDataTestUtils.DEFAULT_VOLTAGE)
        .ampereHours(Arrays.asList("120", "155", "180"))
        .build();

    // Set Filter
    criteria.setSupplierRaws(BatteryDataTestUtils.SUPPLIER_BOSCH_VARTA);
    ArticleFilteringResponse response = service.filter(criteria,
      BatteryDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    Assert.assertNotNull(response.getAggregations());
    EsLogUtils.logJson(log, "aggregations result = {}", response.getAggregations());

    Assert.assertThat(getTotalElements(response), Matchers.greaterThanOrEqualTo(1l));
  }
}
