package com.sagag.services.gtmotive.builder.mainmoduleservice;

import static org.junit.Assert.assertNotNull;

import com.sagag.services.gtmotive.domain.request.AuthenticationData;
import com.sagag.services.gtmotive.domain.response.EquipmentRank;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GtmotivePartsThreeRequestBuilderTest {

  @Test
  public void build_shouldReturnRequest_givenRawData() throws Exception {
    AuthenticationData authenData = AuthenticationData.builder().gsId("SGS029700N")
        .gsPwd("FC3306DA3A").customerId("SAG_CH").userId("wsuserdev").build();
    GtmotivePartsThreeSearchRequest searchRequest =
        GtmotivePartsThreeSearchRequest.builder().partCode("4005L").umc("AR01001").build();
    GtmotivePartsThreeSearchCriteria criteria =
        new GtmotivePartsThreeSearchCriteria(authenData, searchRequest);
    String request = new GtmotivePartsThreeRequestBuilder().criteria(criteria).build();
    assertNotNull(request);
  }

  @Test
  public void build_shouldReturnRequest_givenDataHasEquipments() throws Exception {
    AuthenticationData authenData = AuthenticationData.builder().gsId("SGS029700N")
        .gsPwd("FC3306DA3A").customerId("SAG_CH").userId("wsuserdev").build();
    GtmotivePartsThreeSearchRequest searchRequest = GtmotivePartsThreeSearchRequest.builder()
        .partCode("4005L").umc("AR01001").equipments(Arrays.asList("CB04", "ZESP")).build();
    GtmotivePartsThreeSearchCriteria criteria =
        new GtmotivePartsThreeSearchCriteria(authenData, searchRequest);
    String request = new GtmotivePartsThreeRequestBuilder().criteria(criteria).build();
    assertNotNull(request);
  }

  @Test
  public void build_shouldReturnRequest_givenDataHasEquipmentsAndRanks() throws Exception {
    AuthenticationData authenData = AuthenticationData.builder().gsId("SGS029700N")
        .gsPwd("FC3306DA3A").customerId("SAG_CH").userId("wsuserdev").build();
    EquipmentRank rank1 =
        EquipmentRank.builder().family("RNG").subFamily("FEC").value("20041111").build();
    EquipmentRank rank2 =
        EquipmentRank.builder().family("RNG").subFamily("NCH").value("2005136010").build();
    List<EquipmentRank> ranks = Arrays.asList(rank1, rank2);
    GtmotivePartsThreeSearchRequest searchRequest =
        GtmotivePartsThreeSearchRequest.builder().partCode("4005L").umc("AR01001")
            .equipments(Arrays.asList("CB04", "ZESP")).equipmentRanks(ranks).build();
    GtmotivePartsThreeSearchCriteria criteria =
        new GtmotivePartsThreeSearchCriteria(authenData, searchRequest);
    String request = new GtmotivePartsThreeRequestBuilder().criteria(criteria).build();
    assertNotNull(request);
  }
}
