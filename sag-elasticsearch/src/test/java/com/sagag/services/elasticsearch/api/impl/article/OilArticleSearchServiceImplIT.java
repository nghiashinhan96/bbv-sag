package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.oils.OilArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.common.OilConstants;
import com.sagag.services.elasticsearch.criteria.article.OilArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.OilArticleResponse;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.utils.OilDataTestUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
@Slf4j
public class OilArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private OilArticleSearchServiceImpl service;

  @Test
  public void testSearchOilArticlesByCriteriaDefault() {
    final OilArticleResponse response = service.search(new OilArticleSearchCriteria(), PageUtils.MAX_PAGE);
    log.debug("result = {}", response.getAggregations());

    Assert.assertThat(response.getArticles().hasContent(), Matchers.is(true));
    Assert.assertThat(response.getAggregations().isEmpty(), Matchers.is(false));
  }

  @Test
  @Ignore
  public void testSearchOilArticlesByCriteriaWithBottleSize() {
    final OilArticleSearchCriteria criteria = OilArticleSearchCriteria.builder()
      .bottleSizes(OilDataTestUtils.BOTTLE_SIZE_0DOT3).build();
    final OilArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);
    log.debug("result = {}", response.getAggregations());

    Assert.assertThat(response.getArticles().hasContent(), Matchers.is(true));
    final boolean isBottleSizeMatched = response.getArticles().getContent().stream()
      .flatMap(article -> article.getCriteria().stream())
      .filter(cr -> OilConstants.BOTTLE_SIZE_CID == Integer.parseInt(cr.getCid()))
      .allMatch(cr -> OilDataTestUtils.BOTTLE_SIZE_0DOT3.get(0).equals(cr.getCvp()));
    Assert.assertThat(isBottleSizeMatched, Matchers.is(true));
    Assert.assertThat(response.getAggregations().get(OilConstants.BOTTLE_SIZE_MAP_NAME).size(),
      Matchers.is(NumberUtils.INTEGER_ONE));
  }

  @Test
  public void shouldReturnOilArticlesWithVisicosity() {
    final OilArticleSearchCriteria criteria =
      OilArticleSearchCriteria.builder().viscosities(OilDataTestUtils.VISCOSITY_10W40).build();
    final OilArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);
    log.debug("result = {}", response);

    Assert.assertThat(response.getArticles().hasContent(), Matchers.is(true));
    final boolean isVisicosityMatched = response.getArticles().getContent().stream()
      .flatMap(article -> article.getCriteria().stream())
      .filter(cr -> OilConstants.VISCOSITY_CID == Integer.parseInt(cr.getCid()))
      .anyMatch(cr -> OilDataTestUtils.VISCOSITY_10W40.get(0).equals(cr.getCvp()));
    Assert.assertThat(isVisicosityMatched, Matchers.is(true));
    Assert.assertThat(response.getAggregations().get(OilConstants.VISCOSITY_MAP_NAME).size(),
      Matchers.greaterThanOrEqualTo(NumberUtils.INTEGER_ONE));
  }

  @Test
  public void testSearchOilArticlesWithVisicosityAndBottleSize() {
    final OilArticleSearchCriteria criteria =
      OilArticleSearchCriteria.builder().bottleSizes(OilDataTestUtils.BOTTLE_SIZE_60)
        .viscosities(OilDataTestUtils.VISCOSITY_10W40).build();
    final OilArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);
    log.debug("result = {}", response);

    Assert.assertThat(response.getArticles().hasContent(), Matchers.is(true));
    final boolean isArticleMatched = response.getArticles().getContent().stream()
      .flatMap(article -> article.getCriteria().stream())
      .filter(cr -> OilConstants.VISCOSITY_CID == Integer.parseInt(cr.getCid())
        && OilConstants.BOTTLE_SIZE_CID == Integer.parseInt(cr.getCid()))
      .allMatch(cr -> OilDataTestUtils.VISCOSITY_10W40.get(0).equals(cr.getCvp()));
    Assert.assertThat(isArticleMatched, Matchers.is(true));
    Assert.assertThat(response.getAggregations().get(OilConstants.VISCOSITY_MAP_NAME).size(),
      Matchers.greaterThanOrEqualTo(NumberUtils.INTEGER_ONE));
    Assert.assertThat(response.getAggregations().get(OilConstants.BOTTLE_SIZE_MAP_NAME).size(),
      Matchers.greaterThanOrEqualTo(NumberUtils.INTEGER_ONE));
  }

  @Test
  public void testSearchOilArticlesWithVehicleAndSpec() {
    final OilArticleSearchCriteria criteria =
      OilArticleSearchCriteria.builder().vehicles(OilDataTestUtils.VEHICLE_MOTORAD)
        .specifications(OilDataTestUtils.SPEC_ISOLEGD).build();
    final OilArticleResponse response = service.search(criteria, PageUtils.MAX_PAGE);
    log.debug("result = {}", response);

    if (!response.hasArticles()) {
      Assert.assertThat(response.getArticles().hasContent(), Matchers.is(false));
      return;
    }
    Assert.assertThat(response.getArticles().hasContent(), Matchers.is(true));
    final boolean isArticleMatched = response.getArticles().getContent().stream()
      .flatMap(article -> article.getCriteria().stream())
      .filter(cr -> OilConstants.VISCOSITY_CID == Integer.parseInt(cr.getCid())
        && OilConstants.BOTTLE_SIZE_CID == Integer.parseInt(cr.getCid()))
      .allMatch(cr -> OilDataTestUtils.VISCOSITY_10W40.get(0).equals(cr.getCvp()));
    Assert.assertThat(isArticleMatched, Matchers.is(true));
  }

  @Test
  public void testSearchOilArticlesByFilterDefault() {

    final OilArticleSearchCriteria criteria = OilArticleSearchCriteria.builder().build();
    final ArticleFilteringResponse response = service.filter(criteria,
        OilDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  @Test
  public void testSearchOilArticlesByFilterWithViscosityAndBottleSize() {

    final OilArticleSearchCriteria criteria =
      OilArticleSearchCriteria.builder().viscosities(OilDataTestUtils.VISCOSITY_10W40)
        .bottleSizes(OilDataTestUtils.BOTTLE_SIZE_2).build();
    final ArticleFilteringResponse response = service.filter(criteria,
      OilDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  @Test
  public void testSearchOilArticlesByFilterWithAllCriteria() {

    final OilArticleSearchCriteria criteria = OilArticleSearchCriteria.builder()
      .vehicles(OilDataTestUtils.VEHILCE_LKW).aggregates(OilDataTestUtils.AGGREGATE_HYDRAULIK)
      .viscosities(OilDataTestUtils.VISCOSITY_10W).bottleSizes(OilDataTestUtils.BOTTLE_SIZE_20)
      .approved(OilDataTestUtils.APPROVED_35VQ25).specifications(OilDataTestUtils.SPEC_SF)
      .build();
    final ArticleFilteringResponse response = service.filter(criteria,
      OilDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  @Test
  public void testSearchOilArticlesByFilterWithBottleSizesAndSpecs() {

    final OilArticleSearchCriteria criteria =
      OilArticleSearchCriteria.builder().viscosities(OilDataTestUtils.VISCOSITY_10W40)
        .bottleSizes(OilDataTestUtils.BOTTLE_SIZES_1_60).approved(OilDataTestUtils.APPROVED_505)
        .specifications(OilDataTestUtils.SPECS_A3_B4).build();
    final ArticleFilteringResponse response = service.filter(criteria,
      OilDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  @Test
  public void testSearchOilArticlesByFilterWithSuppliersAndBottleSizes() {

    final OilArticleSearchCriteria criteria =
      OilArticleSearchCriteria.builder().viscosities(OilDataTestUtils.VISCOSITY_10W40)
        .bottleSizes(OilDataTestUtils.BOTTLE_SIZES_1_60)
        .vehicles(OilDataTestUtils.VEHICLE_MOTORAD).build();

    criteria.setSupplierRaws(OilDataTestUtils.SUPPLIERS_ENI_MOBIL);

    final ArticleFilteringResponse response = service.filter(criteria,
      OilDataTestUtils.PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING);

    assertArticlesFound(response);
  }

  private void assertArticlesFound(ArticleFilteringResponse response) {
    Assert.assertNotNull(response.getAggregations());
    Assert.assertNotNull(response.getArticles());
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(0L));
    Assert.assertThat(response.getAggregations().getOrDefault(ArticleField.SUPPLIER_RAW.name(),
        Collections.emptyList()).size(), Matchers.greaterThanOrEqualTo(0));
  }
}
