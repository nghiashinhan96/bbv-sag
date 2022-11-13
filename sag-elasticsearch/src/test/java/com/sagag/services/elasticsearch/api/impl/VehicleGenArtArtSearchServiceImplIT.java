package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.VehicleGenArtArtSearchService;
import com.sagag.services.elasticsearch.domain.article.FitmentArticle;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtArtDoc;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test class for Elasticsearh service for Vehicle Generic Article Article.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class VehicleGenArtArtSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private VehicleGenArtArtSearchService vehicleGenArtSearchService;

  @Test
  public void testSearchFitmentsByIds() {
    List<String> fitmentIds =
        Stream.of("V54939M26482402", "V54939M26482469").collect(Collectors.toList());
    final List<VehicleGenArtArtDoc> actual =
        vehicleGenArtSearchService.searchFitmentsByIds(fitmentIds);

    Assert.assertNotNull(actual);
    List<VehicleGenArtArtDoc> vehicleGenArtArtDocs =
        actual.stream().filter(x -> Objects.equals(x.getId(), "V54939M26482402")
            || Objects.equals(x.getId(), "V54939M26482469")).collect(Collectors.toList());
    Assert.assertThat(vehicleGenArtArtDocs.size(), Matchers.greaterThan(0));
    Assert.assertThat(actual, CoreMatchers.is(vehicleGenArtArtDocs));

  }

  @Test
  public void testSearchFitmentsByIdsHasCn() {
    List<String> fitmentIds =
        Stream.of("V54939M26482402", "V54939M26482469").collect(Collectors.toList());
    final List<VehicleGenArtArtDoc> actual =
        vehicleGenArtSearchService.searchFitmentsByIds(fitmentIds);

    Assert.assertNotNull(actual);
    List<VehicleGenArtArtDoc> vehicleGenArtArtDocs =
        actual.stream().filter(x -> Objects.equals(x.getId(), "V54939M26482402")
            || Objects.equals(x.getId(), "V54939M26482469")).collect(Collectors.toList());
    Assert.assertThat(vehicleGenArtArtDocs.size(), Matchers.greaterThan(0));
    Assert.assertThat(actual, CoreMatchers.is(vehicleGenArtArtDocs));

    Assert.assertTrue(CollectionUtils
        .isNotEmpty(actual.parallelStream().flatMap(m -> m.getArticles().parallelStream())
            .flatMap(p -> p.getCriteria().parallelStream()).map(t -> t.getCndech())
            .collect(Collectors.toSet())));

  }

  @Test
  public void testSearchFitmentsByIdsToCheckCsort() {
    List<String> fitmentIds =
        Stream.of("V54939M26482402", "V54939M26482469").collect(Collectors.toList());
    final List<VehicleGenArtArtDoc> actual =
        vehicleGenArtSearchService.searchFitmentsByIds(fitmentIds);

    List<Integer> csorts =
        actual.stream().map(x -> x.getArticles()).flatMap(List::stream).map(y -> y.getCriteria())
            .flatMap(List::stream).map(t -> t.getCsort()).collect(Collectors.toList());

    Assert.assertNotNull(actual);
    Assert.assertThat(csorts.size(), Matchers.greaterThan(0));

  }

  @Test
  @Ignore("[ax_vehicle_genart_art_fr] IndexNotFoundException[no such index]")
  public void testSearchFitmentsByIdsWithFrechLocal() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    List<String> fitmentIds =
        Stream.of("V54939M26482402", "V54939M26482469").collect(Collectors.toList());
    final List<VehicleGenArtArtDoc> actual =
        vehicleGenArtSearchService.searchFitmentsByIds(fitmentIds);
    Assert.assertNotNull(actual);
    List<VehicleGenArtArtDoc> vehicleGenArtArtDocs =
        actual.stream().filter(x -> Objects.equals(x.getId(), "V54939M26482469")
            || Objects.equals(x.getId(), "V54939M26482402")).collect(Collectors.toList());
    Assert.assertThat(vehicleGenArtArtDocs.size(), Matchers.greaterThan(0));
    Assert.assertThat(actual, CoreMatchers.is(vehicleGenArtArtDocs));

  }

  @Test
  public void testSearchFitmentsByIdsHasVga() {
    List<String> fitmentIds =
        Stream.of("V54939M26482402", "V54939M26482469").collect(Collectors.toList());
    final List<VehicleGenArtArtDoc> actual =
        vehicleGenArtSearchService.searchFitmentsByIds(fitmentIds);
    Assert.assertNotNull(actual);
    List<VehicleGenArtArtDoc> vehicleGenArtArtDocs =
        actual.stream().filter(x -> Objects.equals(x.getId(), "V54939M26482402")
            || Objects.equals(x.getId(), "V54939M26482469")).collect(Collectors.toList());
    Assert.assertThat(vehicleGenArtArtDocs.size(), Matchers.greaterThan(0));
    Assert.assertThat(actual, CoreMatchers.is(vehicleGenArtArtDocs));
    Assert.assertTrue(CollectionUtils.isNotEmpty(vehicleGenArtArtDocs.parallelStream()
        .flatMap(m -> m.getVgas().parallelStream()).collect(Collectors.toSet())));

  }

  @Test
  public void shouldGetFitmentByPimId() {
    final List<VehicleGenArtArtDoc> fitments =
        vehicleGenArtSearchService.searchFitmentsByIds(Arrays.asList("V54939M26482402"));
    Assert.assertNotNull(fitments);
    final VehicleGenArtArtDoc fitment = fitments.get(0); // only one found
    Assert.assertThat(true, Is.is(fitment.getArticles().stream().anyMatch(this::equalsPimId)));

  }

  @Test
  public void testSearchFitmentsByVehIdAndGaIds() {
    final String vehId = "V7612M1797";
    final List<String> gaIds = Arrays.asList("73", "74");

    final List<VehicleGenArtArtDoc> result =
        vehicleGenArtSearchService.searchFitments(vehId, gaIds);

    Assert.assertTrue(!result.isEmpty());
  }

  private boolean equalsPimId(final FitmentArticle art) {
    final String pimId = "1000180640";
    return StringUtils.equals(String.valueOf(pimId), art.getIdSagsys());
  }

  @Test
  public void testSearchFitmentByVehIdAndArticleIds() {
    final String vehId = "V31340M22639";
    final List<String> artIds = Arrays.asList("1000955053", "1000241792", "1000232617");

    final List<VehicleGenArtArtDoc> result =
      vehicleGenArtSearchService.searchFitmentsByVehIdAndArticleIds(vehId, artIds);

    Assert.assertTrue(!result.isEmpty());
  }
}
