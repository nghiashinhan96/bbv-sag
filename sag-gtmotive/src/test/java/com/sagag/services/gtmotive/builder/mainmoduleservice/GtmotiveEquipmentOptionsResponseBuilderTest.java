package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.gtmotive.DataProvider;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionsResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GtmotiveEquipmentOptionsResponseBuilderTest {

  @Test
  public void build_shouldReturnResponse_givenRequest() throws Exception {
    GtmotiveEquipmentOptionsResponse response = new GtmotiveEquipmentOptionsResponseBuilder()
        .unescapseXmlData(DataProvider.gtmotiveEquipmentOptionsResponse()).build();

    Assert.assertNotNull(response);
  }
}
