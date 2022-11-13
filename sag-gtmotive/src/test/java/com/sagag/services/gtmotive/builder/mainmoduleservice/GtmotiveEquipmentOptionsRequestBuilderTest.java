package com.sagag.services.gtmotive.builder.mainmoduleservice;

import static org.junit.Assert.assertNotNull;

import com.sagag.services.gtmotive.domain.request.AuthenticationData;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class GtmotiveEquipmentOptionsRequestBuilderTest {

  @Test
  public void build_shouldReturnRequest() throws Exception {
    AuthenticationData authenData = AuthenticationData.builder().gsId("SGS029700N")
        .gsPwd("FC3306DA3A").customerId("SAG_CH").userId("wsuserdev").build();
    GtmotiveEquipmentOptionsSearchRequest searchRequest = GtmotiveEquipmentOptionsSearchRequest
        .builder().partCodes(Arrays.asList("56C00")).umc("BM00801").build();
    GtmotiveEquipmentOptionsSearchCriteria criteria =
        new GtmotiveEquipmentOptionsSearchCriteria(authenData, searchRequest);
    String request = new GtmotiveEquipmentOptionsRequestBuilder().criteria(criteria).build();
    assertNotNull(request);
  }
}
