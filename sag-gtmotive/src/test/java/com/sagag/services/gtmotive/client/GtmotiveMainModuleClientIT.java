package com.sagag.services.gtmotive.client;

import static org.junit.Assert.assertNotNull;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.gtmotive.app.GtmotiveApplication;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionsResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsListResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { GtmotiveApplication.class })
@EshopIntegrationTest
public class GtmotiveMainModuleClientIT {

  @Autowired
  private GtmotiveMainModuleClient gtmotiveMainModuleClient;

  @Test
  public void getPartsThreeResponse_shouldReturnPartsThreeResponse_givenRequestModel()
      throws Exception {
    GtmotivePartsThreeSearchRequest searchRequest = GtmotivePartsThreeSearchRequest.builder()
        .umc("BM01802").partCode("7025L").equipments(Arrays.asList("CT05", "MU01")).build();
    GtmotivePartsThreeResponse response =
        gtmotiveMainModuleClient.searchReferencesByPartCode(searchRequest);
    assertNotNull(response);
  }

  @Test
  public void getPartsThreeResponse_shouldReturnPartsThreeResponseCaseOneAndTwo_givenRequestModel()
      throws Exception {
    GtmotivePartsThreeSearchRequest searchRequest =
        GtmotivePartsThreeSearchRequest.builder().umc("BM00901").partCode("4851L").build();
    GtmotivePartsThreeResponse response =
        gtmotiveMainModuleClient.searchReferencesByPartCode(searchRequest);
    assertNotNull(response);
  }

  @Test
  public void getPartsListResponse_shouldReturnPartsListResponse_givenRequestModel()
      throws Exception {
    GtmotivePartsListSearchRequest searchRequest =
        GtmotivePartsListSearchRequest.builder().umc("VW04801").build();
    GtmotivePartsListResponse response =
        gtmotiveMainModuleClient.searchVehiclePartList(searchRequest);
    assertNotNull(response);
    System.out.println(response);
  }

  @Test
  public void searchEquipmentOptions_shouldReturnEquipmentOptions_givenSearchRequest()
      throws Exception {

    GtmotiveEquipmentOptionsSearchRequest searchRequest = GtmotiveEquipmentOptionsSearchRequest
        .builder().umc("BM00801").partCodes(Arrays.asList("56C00")).build();

    GtmotiveEquipmentOptionsResponse response =
        gtmotiveMainModuleClient.searchEquipmentOptions(searchRequest);
    assertNotNull(response);
  }
}
