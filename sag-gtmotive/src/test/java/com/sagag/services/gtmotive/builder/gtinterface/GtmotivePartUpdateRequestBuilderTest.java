package com.sagag.services.gtmotive.builder.gtinterface;

import static org.junit.Assert.assertNotNull;

import com.sagag.services.gtmotive.domain.request.AuthenticationData;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartUpdateCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartUpdateRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GtmotivePartUpdateRequestBuilderTest {

  @Test
  public void build_shouldReturnXmlRquest() throws Exception {
    AuthenticationData authenData = AuthenticationData.builder().gsId("SGS029700N")
        .gsPwd("FC3306DA3A").customerId("SAG_CH").userId("ctlgpro").build();
    GtmotivePartUpdateRequest request = GtmotivePartUpdateRequest.builder()
        .estimateId("11117921556532138015").shortNumber("M0300").build();
    GtmotivePartUpdateCriteria criteria = new GtmotivePartUpdateCriteria(authenData, request);

    String result = new GtmotivePartUpdateRequestBuilder().criteria(criteria).build();
    assertNotNull(result);
  }
}
