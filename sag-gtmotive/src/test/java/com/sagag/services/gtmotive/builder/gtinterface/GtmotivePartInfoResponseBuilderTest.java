package com.sagag.services.gtmotive.builder.gtinterface;

import static org.junit.Assert.assertNotNull;

import com.sagag.services.gtmotive.DataProvider;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotivePartInfoResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GtmotivePartInfoResponseBuilderTest {

  @Test
  public void build_shouldReturnPartInfoResponse_givenXmlRepsonse() throws Exception {

    GtmotivePartInfoResponse response = new GtmotivePartInfoResponseBuilder()
        .unescapseXmlData(DataProvider.gtmotivePartInfoResponse()).build();
    assertNotNull(response);
  }
}
