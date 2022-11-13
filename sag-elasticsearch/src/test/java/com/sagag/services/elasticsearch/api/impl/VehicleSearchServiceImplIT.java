package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.criteria.VehicleFilteringTerms;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleFilteringCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchSortCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.domain.article.GTMotiveLink;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.elasticsearch.dto.FreetextVehicleDto;
import com.sagag.services.elasticsearch.dto.FreetextVehicleResponse;
import com.sagag.services.elasticsearch.dto.VehicleSearchResponse;
import com.sagag.services.elasticsearch.enums.FreetextSearchOption;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortOrder;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Integration test class for Vehicle search service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
@Slf4j
public class VehicleSearchServiceImplIT extends AbstractElasticsearchIT {

  private static final String[] MOD_CODES = { "CB03", "CB05" };
  private static final String ENGCODE = "MTAD";
  private static final String MAKECODE_VW04801 = "VW04801";
  private static final Object VEHID_V18004M18081 = "V18004M18081";

  private static final String VEH_ENG_CODE_FULL_B47D20A = "B47D20A";

  @Autowired
  private VehicleSearchService vehicleSearchService;

  @Override
  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void testSearchVehiclesByVehId() {
    final String vehId = "V58944M27193";
    final Optional<VehicleDoc> expected = vehicleSearchService.searchVehicleByVehId(vehId);
    Assert.assertNotNull(expected);
    Assert.assertThat(expected.isPresent(), Is.is(true));
    Assert.assertThat(expected.get().getVehId(), Matchers.equalTo(vehId));
  }

  @Test
  public void testSearchVehiclesByVehIdWithEmpty() {
    final Optional<VehicleDoc> expected =
        vehicleSearchService.searchVehicleByVehId(StringUtils.EMPTY);
    Assert.assertNotNull(expected);
    Assert.assertThat(expected.isPresent(), Is.is(false));
  }

  @Test
  public void testSearchVehiclesByVehIdWithNull() {
    final Optional<VehicleDoc> expected = vehicleSearchService.searchVehicleByVehId(null);
    Assert.assertNotNull(expected);
    Assert.assertThat(expected.isPresent(), Is.is(false));
  }

  @Test
  public void testSearchTypesByMakeModel() {
    Integer idMake = 111;
    Integer idMode = 8657;
    final List<VehicleDoc> vehicleDocs =
        vehicleSearchService.searchTypesByMakeModel(idMake, idMode, StringUtils.EMPTY,
          StringUtils.EMPTY);

    Assert.assertNotNull(vehicleDocs);
    Assert.assertTrue(vehicleDocs.parallelStream()
        .allMatch(p -> p.getIdMake().equals(idMake) && p.getIdModel().equals(idMode)));

  }

  @Test
  public void shouldGetVehicleByGtUmc() {
    String gtMakeCode = "PE01001";
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(gtMakeCode,
        Collections.emptyList(), Collections.emptyList(), Collections.emptyList()).orElse(null);

    final Optional<GTMotiveLink> firstGtLink = vehicle.getGtLinks().stream()
        .filter(gtLink -> StringUtils.equals(gtMakeCode, gtLink.getGtMakeCode())).findFirst();

    Assert.assertThat(vehicle, Matchers.notNullValue());
    Assert.assertThat(firstGtLink.isPresent(), Is.is(true));
    Assert.assertThat(firstGtLink.get().getGtMakeCode(), Matchers.equalTo(gtMakeCode));
  }

  @Test
  public void shouldGetVehicleByModAndEngine() {
    String gtModelCode = "CR05";
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(MAKECODE_VW04801,
        Arrays.asList(gtModelCode), Arrays.asList(ENGCODE), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.notNullValue());
    assertVehicle(vehicle);
  }

  @Test
  public void shouldGetVehicleByContainsGtModAlt() {
    String gtModelCode = "CB05";
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(MAKECODE_VW04801,
        Arrays.asList(gtModelCode), Arrays.asList(ENGCODE), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.notNullValue());
    assertVehicle(vehicle);
  }

  @Test
  public void shouldGetVehicleByContainsMultipleGtModAlts() {
    String[] gtModelCodes = { "CB05", "CW11" };
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(MAKECODE_VW04801,
        Arrays.asList(gtModelCodes), Arrays.asList(ENGCODE), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.notNullValue());
    assertVehicle(vehicle);
  }

  @Test
  public void shouldGetFrenchVehicleByGtUmc() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(MAKECODE_VW04801,
        Arrays.asList(MOD_CODES), Arrays.asList(ENGCODE), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.notNullValue());
    assertVehicle(vehicle);
  }

  @Test
  public void shouldGetGermanVehicleByGtUmc() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(MAKECODE_VW04801,
        Arrays.asList(MOD_CODES), Arrays.asList(ENGCODE), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.notNullValue());
    assertVehicle(vehicle);
  }

  @Test
  public void shouldGetItalianVehicleByGtUmc() {
    LocaleContextHolder.setLocale(Locale.ITALIAN);
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(MAKECODE_VW04801,
        Arrays.asList(MOD_CODES), Arrays.asList(ENGCODE), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.notNullValue());
    assertVehicle(vehicle);
  }

  @Test
  public void shouldGetDefaultGermanVehicleByGtUmc() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(MAKECODE_VW04801,
        Arrays.asList(MOD_CODES), Arrays.asList(ENGCODE), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.notNullValue());
    assertVehicle(vehicle);
  }

  private static void assertVehicle(final VehicleDoc vehicle) {
    String gtMakeCode = MAKECODE_VW04801;
    String gtEngineCode = ENGCODE;
    GTMotiveLink firstGtLink =
        vehicle.getGtLinks().stream()
            .filter(gtLink -> gtMakeCode.equalsIgnoreCase(gtLink.getGtMakeCode())
                && gtEngineCode.equalsIgnoreCase(gtLink.getGtEngineCode()))
            .findFirst().orElse(null);
    Assert.assertThat(firstGtLink, Matchers.notNullValue());
    Assert.assertEquals(gtMakeCode, firstGtLink.getGtMakeCode());
    Assert.assertEquals(gtEngineCode, firstGtLink.getGtEngineCode());
    Assert.assertEquals(SagConstants.NULL_STR, firstGtLink.getTransmisionCode());
  }

  @Test
  public void shouldNotGetVehicleByContainsGtModAlts() {
    String[] gtModelCodes = { "CF85", "CW11" };
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(MAKECODE_VW04801,
        Arrays.asList(gtModelCodes), Arrays.asList(ENGCODE), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.nullValue());
  }

  @Test
  public void shouldGetVehicleByContainsMultipleGtModAltsAll() {
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle(MAKECODE_VW04801,
        Arrays.asList(MOD_CODES), Arrays.asList(ENGCODE), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.notNullValue());
    assertVehicle(vehicle);
  }

  @Test
  public void shouldNotGetGtMotiveVehicle() throws IOException {
    final VehicleDoc vehicle = vehicleSearchService.searchGtmotiveVehicle("Unknown",
        Arrays.asList("CR05"), Collections.emptyList(), Collections.emptyList()).orElse(null);
    Assert.assertThat(vehicle, Matchers.nullValue());
  }


  @Test
  public void shouldGetResultsForAudiFreetextSearch() {
    final String text = "audi";
    final Optional<VehicleFilteringTerms> filterTerms = Optional.empty();
    final int pageSize = 15;
    final PageRequest pageable = PageUtils.defaultPageable(pageSize);
    final Optional<FreetextVehicleResponse> response = vehicleSearchService
        .searchVehiclesByFreetext(FreetextSearchOption.VEHICLES, text, filterTerms, pageable);
    Assert.assertThat(true, Is.is(response.isPresent()));
    final FreetextVehicleResponse vehResponse = response.get();
    Set<String> collect = vehResponse.getVehicles().getContent().parallelStream()
        .map(m -> m.getVehBranch()).collect(Collectors.toSet());
    Assert.assertTrue(collect.contains(text.toUpperCase()));
  }

  @Test
  public void shouldGetResultsForAudiFreetextSearchWithFilterings() {
    final String text = "AUDI TT Roadster (8N9) 1.8 T  AUM";
    searchVehiclesByFreetext(text);
  }

  @Test
  public void shouldGetResultsForAudiFreetextSearchWithFilteringsWithSupportedParseEngineCodeDelimiter() {
    final String text = "AUDI TT Roadster (8N9) 1,8 T  AUM";
    searchVehiclesByFreetext(text);
  }

  private void searchVehiclesByFreetext(String text) {
    final PageRequest pageable = PageUtils.defaultPageable(1);
    final Optional<FreetextVehicleResponse> response =
        vehicleSearchService.searchVehiclesByFreetext(FreetextSearchOption.VEHICLES, text,
            Optional.empty(), pageable);
    Assert.assertThat(response.isPresent(), Is.is(true));
    final FreetextVehicleResponse vehResponse = response.get();
    Assert.assertThat(vehResponse.getVehicles().getTotalElements(), Is.is(1L));
    // Assert attributes to increase the coverage of the response dto, warn that the vehicle AUDI TT
    // Roadster (8N9) 1.8 T AUM should be in test elasticsearch index
    final FreetextVehicleDto vehicle = vehResponse.getVehicles().getContent().get(0);
    assertAudiVehicleValues(vehicle);
  }

  private void assertAudiVehicleValues(final FreetextVehicleDto vehicle) {
    Assert.assertThat(vehicle.getGtEnd(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getGtEngineCode(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getGtMakeCode(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getGtModAlt(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getGtModelCode(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getGtmodName(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getGtStart(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getTransmisionCode(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehBodyType(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehBranch(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehBuiltYearFrom(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehBuiltYearTo(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehCodeType(), Matchers.nullValue());
    Assert.assertThat(vehicle.getVehDisplay(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehDriveType(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehEngine(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehEngineCode(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehFuelType(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehId(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehMakeId(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehModel(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehModelId(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehMotorId(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehName(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehPowerKw(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehTypeDesc(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getKtType(), Matchers.notNullValue());
    Assert.assertThat(vehicle.getVehZylinder(), Matchers.notNullValue());
  }

  @Test
  public void shouldGetResultsForMercedesFreetextSearchWithFilteringsPage1() {
    final String text = "mercedes";
    final VehicleFilteringTerms filtering = VehicleFilteringTerms.builder().vehYearFrom(1997)
        .vehYearTo(2004).vehModel("A-CLASS (W168)").build();
    final PageRequest page = PageUtils.DEF_PAGE;
    final Optional<FreetextVehicleResponse> response =
        vehicleSearchService.searchVehiclesByFreetext(FreetextSearchOption.VEHICLES, text,
            Optional.of(filtering), page);
    Assert.assertThat(true, Is.is(response.isPresent()));
    final FreetextVehicleResponse vehResponse = response.get();
    Assert.assertThat(vehResponse.getVehicles().hasContent(), Is.is(true));
  }

  @Test
  public void shouldGetResultsForMercedesW169FreetextSearchWithFilteringsPage1() {
    final String text = "mercedes";
    final VehicleFilteringTerms filtering = VehicleFilteringTerms.builder().vehYearFrom(2004)
        .vehYearTo(2012).vehModel("A-CLASS (W169)").build();
    final PageRequest page = PageUtils.DEF_PAGE;
    final Optional<FreetextVehicleResponse> response =
        vehicleSearchService.searchVehiclesByFreetext(FreetextSearchOption.VEHICLES, text,
            Optional.of(filtering), page);
    Assert.assertThat(true, Is.is(response.isPresent()));
    final FreetextVehicleResponse vehResponse = response.get();
    Assert.assertThat(vehResponse.getVehicles().getNumberOfElements(), Is.is(10));
    Assert.assertThat(vehResponse.getVehicles().getTotalElements(),
        Matchers.greaterThanOrEqualTo(11L));
  }

  @Test
  public void shouldGetResultsForMercedesW169FreetextSearchWithFilteringsWithPaging() {
    final String text = "mercedes";
    final VehicleFilteringTerms filtering = VehicleFilteringTerms.builder().vehYearFrom(2004)
        .vehYearTo(2012).vehModel("A-CLASS (W169)").build();
    final int pageSize = 10;
    final PageRequest page = PageUtils.defaultPageable(1, pageSize);

    final Optional<FreetextVehicleResponse> response =
        vehicleSearchService.searchVehiclesByFreetext(FreetextSearchOption.VEHICLES, text,
            Optional.of(filtering), page);
    Assert.assertThat(true, Is.is(response.isPresent()));
    final FreetextVehicleResponse vehResponse = response.get();
    Assert.assertThat(vehResponse.getVehicles().getNumberOfElements(),
        Matchers.greaterThanOrEqualTo(1));
    Assert.assertThat(vehResponse.getVehicles().getTotalElements(),
        Matchers.greaterThanOrEqualTo(11L));
  }

  @Test
  public void testGetTopResultVehicleByKtypes() throws Exception {
    final List<Integer> kTypeNrs = Arrays.asList(26552, 29994, 30441, 30970, 31992, 32589, 32609,
        33250, 58524, 5017, 5420, 17970, 31340, 17971, 17973, 17974, 18248, 18249, 18766, 18769,
        19499, 20048, 32063, 28173, 29990, 29992, 30442, 32585, 32586, 32621, 33031, 756, 5020,
        7894, 19458, 22940, 23263, 26623, 28174, 28204, 28206, 29993, 31591, 31593, 32601, 34879,
        34793, 34795, 4535, 5013, 58749, 6042, 6055, 9621, 18478, 19972, 20002, 22554, 31587, 31590,
        31592, 31708, 4819, 33246, 57733, 762, 7868, 10431, 17972, 18767, 18770, 22556, 23163,
        23290, 28203, 28602, 30969, 30971, 31586, 33245, 6799, 6800, 7867, 18246, 18247, 18479,
        20001, 22553, 18481, 27539, 28207, 29991, 29995, 31594, 32606, 32625, 7904, 28172, 18768,
        19459, 19820, 20049, 22555, 28167, 33030, 10524, 18480, 23195, 23795);

    final List<VehicleDoc> vehicles = vehicleSearchService.getTopResultVehicleByKtypes(kTypeNrs);
    Assert.assertTrue(!vehicles.isEmpty());
    Assert.assertTrue(vehicles.size() <= kTypeNrs.size());
  }
  

  @Test
  public void testGetTopResultVehicleByKtypesWithNull() throws Exception {
    final List<Integer> kTypeNrs = null;
    final List<VehicleDoc> vehicles = vehicleSearchService.getTopResultVehicleByKtypes(kTypeNrs);
    Assert.assertThat(vehicles, Matchers.empty());
  }

  @Test
  public void testGetTopResultVehicleByKtypesNotFound() throws Exception {
    final List<Integer> kTypeNrs = Arrays.asList(112222, 334444, 556666, 778668);
    final List<VehicleDoc> vehicles = vehicleSearchService.getTopResultVehicleByKtypes(kTypeNrs);
    Assert.assertThat(vehicles, Matchers.empty());
  }

  @Test
  public void testSearchVehiclesByKTypeNr() throws Exception {
    final Integer kTypeNr = 4912;

    final List<VehicleDoc> vehicles = vehicleSearchService.searchVehiclesByKTypeNr(kTypeNr);

    Assert.assertThat(vehicles.stream().allMatch(vehicle -> kTypeNr.equals(vehicle.getKtType())),
        Matchers.equalTo(true));
  }

  @Test
  public void testSearchVehiclesByKTypeNrWithNull() throws Exception {
    final Integer kTypeNr = null;
    final List<VehicleDoc> vehicles = vehicleSearchService.searchVehiclesByKTypeNr(kTypeNr);
    Assert.assertThat(vehicles, Matchers.empty());
  }

  @Test
  public void testSearchVehiclesByKTypeNrNotFound() throws Exception {
    final Integer kTypeNr = 1112222;
    final List<VehicleDoc> vehicles = vehicleSearchService.searchVehiclesByKTypeNr(kTypeNr);
    Assert.assertThat(vehicles, Matchers.empty());
  }

  @Test
  public void shouldGetResultsForGolfAlhAsFreetextSearch() {
    final String text = "golf alh";
    final int pageSize = 15;
    final PageRequest pageable = PageUtils.defaultPageable(pageSize);
    final Optional<FreetextVehicleResponse> response = vehicleSearchService
        .searchVehiclesByFreetext(FreetextSearchOption.VEHICLES, text, Optional.empty(), pageable);

    Assert.assertThat(true, Is.is(response.isPresent()));
    final FreetextVehicleResponse vehResponse = response.get();
    Assert.assertThat(vehResponse.getVehicles().hasContent(), Is.is(true));
  }

  @Test
  public void shouldGetResultsForGolfAlhAsMotorCode() {
    final String text = "golf alh";
    final int pageSize = 15;
    final PageRequest pageable = PageUtils.defaultPageable(pageSize);
    final Optional<FreetextVehicleResponse> response =
        vehicleSearchService.searchVehiclesByFreetext(FreetextSearchOption.VEHICLES_MOTOR, text,
            Optional.empty(), pageable);
    if (!response.isPresent()) {
      return;
    }

    Assert.assertThat(response.isPresent(), Is.is(true));
    final FreetextVehicleResponse vehResponse = response.get();
    Assert.assertThat(vehResponse.getVehicles().hasContent(), Is.is(true));
  }

  @Test
  public void testSearchVehicleByVehicleDataWithVehCodes() {
    final String[] vehCodes = { "1SA276", "6PA454" };
    final PageRequest pageable = PageUtils.defaultPageable(10);
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleCodes(Arrays.asList(vehCodes)).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).build();
    final Page<VehicleDoc> vehicle =
        vehicleSearchService.searchVehiclesByCriteria(criteria, pageable).getVehicles();

    Assert.assertThat(vehicle, Matchers.notNullValue());
    Assert.assertThat(vehicle.getTotalElements(), Is.is(2L));
    Assert.assertThat(vehicle.getContent().get(0).getVehId(), Matchers.is(VEHID_V18004M18081));
  }

  @Test
  public void testSearchVehicleByVehicleDataWithVehCodes_WithAgg() {
    final String[] vehCodes = { "1SA276", "6PA454" };
    final PageRequest pageable = PageUtils.defaultPageable(10);
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleCodes(Arrays.asList(vehCodes)).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).aggregation(true).build();
    final VehicleSearchResponse res =
        vehicleSearchService.searchVehiclesByCriteria(criteria, pageable);

    log.debug("Result = {}", SagJSONUtil.convertObjectToPrettyJson(res));

    final Page<VehicleDoc> vehicle = res.getVehicles();
    Assert.assertThat(vehicle, Matchers.notNullValue());
    Assert.assertThat(vehicle.getTotalElements(), Is.is(2L));
    Assert.assertThat(vehicle.getContent().get(0).getVehId(), Matchers.is(VEHID_V18004M18081));
  }

  @Test
  public void testSearchVehicleByVehicleDataWithVehCode() {
    String vehData = "1SA276";
    final PageRequest pageable = PageUtils.defaultPageable(10);
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).build();
    final Page<VehicleDoc> vehicle =
        vehicleSearchService.searchVehiclesByCriteria(criteria, pageable).getVehicles();

    Assert.assertThat(vehicle, Matchers.notNullValue());
    Assert.assertThat(vehicle.getTotalElements(), Is.is(1L));
    Assert.assertThat(vehicle.getContent().get(0).getVehId(), Matchers.is(VEHID_V18004M18081));
  }

  @Test
  public void testSearchVehicleByVehDataWithNationalCode() throws Exception {
    final String vehData = "137050";
    final PageRequest pageable = PageUtils.defaultPageable(10);
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).build();
    final Page<VehicleDoc> vehicle =
        vehicleSearchService.searchVehiclesByCriteria(criteria, pageable).getVehicles();

    Assert.assertThat(vehicle, Matchers.notNullValue());
    Assert.assertThat(vehicle.getTotalElements(), Is.is(1L));
    Assert.assertThat(vehicle.getContent().get(0).getVehId(), Matchers.is(VEHID_V18004M18081));
  }

  @Test
  public void testSearchVehicleByVehDataWithIdSagType() throws Exception {
    final String vehData = "1000000000193540352";
    final PageRequest pageable = PageUtils.defaultPageable(10);
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).build();
    final Page<VehicleDoc> vehicle =
        vehicleSearchService.searchVehiclesByCriteria(criteria, pageable).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Is.is(0L));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldSearchVehiclesByEmptyValues() {
    final String vehicleDesc = StringUtils.EMPTY;
    final String vehicleBuiltYear = StringUtils.EMPTY;
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom(vehicleBuiltYear).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    final Page<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();

    Assert.assertThat(vehicles.hasContent(), Matchers.is(false));
    logVehNames(vehicles);
  }

  @Test
  public void shouldSearchVehiclesByBrand() {
    final String vehicleDesc = "VOLVO";
    final String vehicleBuiltYear = StringUtils.EMPTY;
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom(vehicleBuiltYear).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    final Page<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();

    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
    logVehNames(vehicles);
  }

  @Test
  public void shouldSearchVehiclesByBrandAndBuiltYear() {
    final String vehicleDesc = "VOLVO";
    final String vehicleBuiltYear = "200007";
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom(vehicleBuiltYear).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    final Page<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();

    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
    logVehNames(vehicles);
  }

  @Test
  public void shouldSearchVehiclesByBrandAndModel() {
    final String vehicleDesc = "audi a7";
    final String vehicleBuiltYear = "201501";
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom(vehicleBuiltYear).build();
    final VehicleSearchCriteria criteria = VehicleSearchCriteria.builder().searchTerm(searchTerm)
        .filtering(filtering).aggregation(true).build();
    final VehicleSearchResponse response =
        vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged());
    final Page<VehicleDoc> vehicles = response.getVehicles();

    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
    logVehNames(vehicles);
  }

  @Test
  public void shouldSearchVehiclesByBrandAndModelAndEngineCode() {
    final String vehicleDesc = "VOLVO V40 Estate B 4184 SJ";
    final String vehicleBuiltYear = "200106";
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom(vehicleBuiltYear).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    final Page<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();

    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
    logVehNames(vehicles);
  }

  @Test
  public void shouldSearchVehiclesByBrandAndModel_VOLVO() {
    final String vehicleDesc = "VOLVO V40 Estate";
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    final Page<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();

    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
    logVehNames(vehicles);
  }

  @Test
  public void shouldSearchVehiclesByBrandAndModel_VOLVO_AndFilter_VehName1() {
    final String vehicleDesc = "VOLVO V40 Estate";
    final String vehName = "VOLVO V40 Estate 1.8 S3";
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering = VehicleFilteringCriteria.builder()
        .vehFullName(vehName).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    final Page<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();

    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
    logVehNames(vehicles);
  }

  @Test
  @Ignore
  public void shouldSearchVehiclesByBrandAndModel_VOLVO_AndFilter_VehName2() {
    final String vehicleDesc = "VOLVO V40 Estate";
    final String vehName = "Est 1.9 T2";
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =VehicleFilteringCriteria.builder()
        .vehFullName(vehName).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    final Page<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();

    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
    logVehNames(vehicles);
  }

  @Test
  @Ignore
  public void shouldSearchVehiclesByBrandAndModel_VOLVO_AndFilter_VehName3() {
    final String vehicleDesc = "VOLVO V40 Estate";
    final String vehName = "Es 1.9 T2";
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =VehicleFilteringCriteria.builder()
        .vehFullName(vehName).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    final Page<VehicleDoc> vehicles =
        vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();

    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
  }

  @Test
  public void shouldSearchVehiclesByBrandAndModel_WithMultiSortingFields() {
    final String vehicleDesc = "VOLVO V40 Estate";
    final String vehicleBuiltYear = "200106";
    final VehicleSearchTermCriteria searchTerm =
      VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =
      VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom(vehicleBuiltYear).build();
    final VehicleSearchSortCriteria sort = VehicleSearchSortCriteria.builder()
      .vehBodyType(SortOrder.ASC)
      .vehFuelType(SortOrder.DESC)
      .vehZylinder(SortOrder.ASC)
      .vehEngine(SortOrder.DESC)
      .vehMotorCode(SortOrder.ASC)
      .vehPower(SortOrder.ASC)
      .vehDriveType(SortOrder.DESC)
      .build();
    final VehicleSearchCriteria criteria =
      VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering)
        .sort(sort).build();
    final Page<VehicleDoc> vehicles =
      vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();
    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
    logVehNames(vehicles);
  }

  @Test
  public void shouldSearchVehiclesByBrandAndModel_WithSingleSortingFields() {
    final String vehicleDesc = "VOLVO V40 Estate";
    final String vehicleBuiltYear = "200106";
    final VehicleSearchTermCriteria searchTerm =
      VehicleSearchTermCriteria.builder().vehicleDesc(vehicleDesc).build();
    final VehicleFilteringCriteria filtering =
      VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom(vehicleBuiltYear).build();
    final VehicleSearchSortCriteria sort = VehicleSearchSortCriteria.builder()
      .vehFuelType(SortOrder.DESC)
      .build();
    final VehicleSearchCriteria criteria =
      VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering)
        .sort(sort).build();
    final Page<VehicleDoc> vehicles =
      vehicleSearchService.searchVehiclesByCriteria(criteria, Pageable.unpaged()).getVehicles();
    Assert.assertThat(vehicles.hasContent(), Matchers.is(true));
    logVehNames(vehicles);
  }

  private static void logVehNames(final Page<VehicleDoc> vehicles) {
    final List<String> vehNames = vehicles.getContent().stream()
      .map(VehicleDoc::getVehicleFullName)
      .collect(Collectors.toList());

    log.debug("Result = {}", SagJSONUtil.convertObjectToPrettyJson(vehNames));
  }

  @Test
  public void givenFreeTextShouldBeSearchFound() {
    Optional<FreetextVehicleResponse> res = vehicleSearchService.searchVehiclesByFreetext(
        FreetextSearchOption.VEHICLES, VEH_ENG_CODE_FULL_B47D20A, Optional.empty(),
        PageUtils.DEF_PAGE);
    Assert.assertThat(res.isPresent(), Matchers.is(true));
  }

  @Test
  public void givenVehicleDescriptionShouldBeSearchFound() {
    final VehicleSearchTermCriteria term = new VehicleSearchTermCriteria();
    term.setVehicleDesc(VEH_ENG_CODE_FULL_B47D20A);

    VehicleSearchCriteria criteria = new VehicleSearchCriteria();
    criteria.setSearchTerm(term);

    VehicleSearchResponse res = vehicleSearchService.searchVehiclesByCriteria(criteria,
        PageUtils.DEF_PAGE);
    Assert.assertThat(res.getVehicles().hasContent(), Matchers.is(true));
  }

  @Test
  public void givenArticleNrContainSpecialCharacterShouldBeSearchNotFoundSafely() {
    final String freetext = "OC90:";
    final Optional<FreetextVehicleResponse> res = vehicleSearchService.searchVehiclesByFreetext(
        FreetextSearchOption.VEHICLES, freetext, Optional.empty(), PageUtils.DEF_PAGE);
    Assert.assertThat(res.isPresent(), Matchers.is(false));
  }
}
