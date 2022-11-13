package com.sagag.services.gtmotive.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.gtmotive.DataProvider;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.app.GtmotiveApplication;
import com.sagag.services.gtmotive.domain.request.GtmotiveVehicleCriteria;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsListResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeReference;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { GtmotiveApplication.class })
@EshopIntegrationTest
@Slf4j
public class GtmotiveServiceImplIT {

  @Autowired
  private GtmotiveService gtmotiveService;

  @Test
  public void shouldGetMakeCode_KI1_FromValidVin_U5YHN813GDL034339() {
    Assert.assertThat("KI1", Is.is(gtmotiveService.getMakeCodeFromVinDecoder("U5YHN813GDL034339")));
  }

  @Test
  public void shouldGetMakeCode_VW1_FromValidVin_WVWZZZAUZEP575926() {
    Assert.assertThat("VW1", Is.is(gtmotiveService.getMakeCodeFromVinDecoder("WVWZZZAUZEP575926")));
  }

  @Test
  public void shouldGetVehicleInfoByVin_WVWZZZAUZEP575926() {
    final String custNr = String.valueOf(DataProvider.CUST_NR_1100005);
    final String vin = DataProvider.VIN_WVWZZZAUZEP575926;
    final GtmotiveVehicleCriteria criteria = DataProvider.criteria_WVWZZZAUZEP575926();

    final Optional<GtmotiveVehicleDto> actual = gtmotiveService.getVehicleInfo(custNr, criteria);
    Assert.assertThat(actual.isPresent(), Matchers.is(true));
    log.debug("result = {}", SagJSONUtil.convertObjectToPrettyJson(actual.get()));
    Assert.assertThat(actual.get().getVin(), Matchers.is(vin));
  }

  @Test
  public void shouldGetVehicleInfoByVin_VSSZZZ5FZH6561928() {
    final String custNr = String.valueOf(DataProvider.CUST_NR_1100005);
    final String vin = DataProvider.VIN_VSSZZZ5FZH6561928;
    final GtmotiveVehicleCriteria criteria = DataProvider.criteria_VSSZZZ5FZH6561928();

    final Optional<GtmotiveVehicleDto> actual = gtmotiveService.getVehicleInfo(custNr, criteria);
    Assert.assertThat(actual.isPresent(), Matchers.is(true));
    log.debug("result = {}", SagJSONUtil.convertObjectToPrettyJson(actual.get()));
    Assert.assertThat(actual.get().getVin(), Matchers.is(vin));
  }

  @Test
  public void shouldGetVehicleInfoByMaintenance_V58944M27193() {
    final GtmotiveVehicleCriteria criteria = DataProvider.criteria_V58944M27193();
    Optional<GtmotiveVehicleDto> actual =
        gtmotiveService.getVehicleInfo(String.valueOf(DataProvider.CUST_NR_1100005), criteria);

    Assert.assertThat(actual.isPresent(), Matchers.is(true));
    log.debug("result = {}", SagJSONUtil.convertObjectToPrettyJson(actual.get()));
    Assert.assertThat(actual.get().getUmc(), Matchers.is(DataProvider.UMC_VW04802));
  }

  @Test
  public void shouldGetVehicleInfoByMaintenance_V119702M33633() {
    final GtmotiveVehicleCriteria criteria = DataProvider.criteria_V119702M33633();
    Optional<GtmotiveVehicleDto> actual =
        gtmotiveService.getVehicleInfo(String.valueOf(DataProvider.CUST_NR_1100005), criteria);

    Assert.assertThat(actual.isPresent(), Matchers.is(true));
    log.debug("result = {}", SagJSONUtil.convertObjectToPrettyJson(actual.get()));
  }

  @Test
  public void seachReferencePartsThree_shouldReturnPartsThreeResponse_givenSearchRequest()
      throws Exception {
    GtmotivePartsThreeSearchRequest searchRequest =
        GtmotivePartsThreeSearchRequest.builder().umc("AR01001").partCode("4005L").build();
    GtmotivePartsThreeResponse response = gtmotiveService.searchReferencesByPartCode(searchRequest);
    List<GtmotivePartsThreeReference> references = response.getReferences();
    GtmotivePartsThreeReference reference = references.get(0);
    Assert.assertThat(references.size(), Matchers.is(2));
    Assert.assertThat(reference.getCode(), Matchers.is("0000071775979"));
  }

  @Test
  public void searchPartsList_shouldReturnPartsListResponse_givenSearchRequest() throws Exception {
    GtmotivePartsListSearchRequest searchRequest =
        GtmotivePartsListSearchRequest.builder().umc("VW04801").build();
    GtmotivePartsListResponse response = gtmotiveService.searchVehiclePartsList(searchRequest);
    Assert.assertNotNull(response);
  }
}
