package com.sagag.services.gtmotive.builder.gtinterface;

import static org.junit.Assert.assertNotNull;

import com.sagag.services.gtmotive.domain.request.AuthenticationData;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartInfoCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartInfoRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class GtmotivePartInfoRequestBuilderTest {

  @Test
  public void build_shouldReturnXmlRquest() throws Exception {
    AuthenticationData authenData = AuthenticationData.builder().gsId("SGS029700N")
        .gsPwd("FC3306DA3A").customerId("SAG_CH").userId("wsuserdev").build();
    GtmotivePartInfoRequest searchRequest =
        GtmotivePartInfoRequest.builder().estimateId("11117921556532138015").umc("AU02801")
            .equipments(Arrays.asList("CB05", "MU12")).build();
    GtmotivePartInfoCriteria criteria = new GtmotivePartInfoCriteria(authenData, searchRequest);

    String result = new GtmotivePartInfoRequestBuilder().criteria(criteria).build();
    assertNotNull(result);
  }
}
