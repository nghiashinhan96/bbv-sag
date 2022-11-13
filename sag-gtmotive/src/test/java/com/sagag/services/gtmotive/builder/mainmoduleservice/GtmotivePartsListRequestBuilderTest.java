package com.sagag.services.gtmotive.builder.mainmoduleservice;

import static org.junit.Assert.assertNotNull;

import com.sagag.services.gtmotive.domain.request.AuthenticationData;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class GtmotivePartsListRequestBuilderTest {

  @Test
  public void build_shouldReturnRequest_givenRawData() throws Exception {
    AuthenticationData authenData = AuthenticationData.builder().gsId("SGS029700N")
        .gsPwd("FC3306DA3A").customerId("SAG_CH").userId("wsuserdev").build();
    GtmotivePartsListSearchRequest searchRequest =
        GtmotivePartsListSearchRequest.builder().umc("AR01001").build();
    GtmotivePartsListSearchCriteria criteria =
        new GtmotivePartsListSearchCriteria(authenData, searchRequest);
    String request = new GtmotivePartsListRequestBuilder().criteria(criteria).build();
    assertNotNull(request);
  }

  @Test
  public void build_shouldReturnRequest_givenDataHasEquipments() throws Exception {
    AuthenticationData authenData = AuthenticationData.builder().gsId("SGS029700N")
        .gsPwd("FC3306DA3A").customerId("SAG_CH").userId("wsuserdev").build();
    GtmotivePartsListSearchRequest searchRequest = GtmotivePartsListSearchRequest.builder()
        .umc("AR01001").equipments(Arrays.asList("CB04", "ZESP")).build();
    GtmotivePartsListSearchCriteria criteria =
        new GtmotivePartsListSearchCriteria(authenData, searchRequest);
    String request = new GtmotivePartsListRequestBuilder().criteria(criteria).build();
    assertNotNull(request);
  }
}
