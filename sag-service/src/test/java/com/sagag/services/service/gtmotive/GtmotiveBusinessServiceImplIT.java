package com.sagag.services.service.gtmotive;

import static org.junit.Assert.assertNotNull;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByVinRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveReferenceSearchByPartCodesCriteria;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.api.GtmotiveBusinessService;
import com.sagag.services.service.response.gtmotive.GtmotivePartsListSearchResponse;
import com.sagag.services.service.response.gtmotive.GtmotiveReferenceSearchResponse;
import com.sagag.services.service.response.gtmotive.GtmotiveVehicleSearchResponse;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class GtmotiveBusinessServiceImplIT {

  @Autowired
  private GtmotiveBusinessService gtmotiveBusinessService;


  @Test
  public void searchArticleByReferences_shouldReturnArticlesList_givenValidSearchCriteria()
      throws Exception {
    GtmotivePartsThreeSearchRequest partsThreeRequest =
        GtmotivePartsThreeSearchRequest.builder().partCode("85010").umc("TY03101").build();
    GtmotiveReferenceSearchByPartCodesCriteria criteria =
        new GtmotiveReferenceSearchByPartCodesCriteria();
    criteria.setGtmotivePartsThreeSearchRequest(partsThreeRequest);
    GtmotiveReferenceSearchResponse response =
        gtmotiveBusinessService.searchReferencesByPartCode(criteria);
    assertNotNull(response);
  }

  @Test
  @Ignore("This VIN got error code = 7")
  public void searchVehicle_shouldReturnVehicleResponse_givenCustNrAndSearchCriteria()
      throws Exception {
    GtmotiveVehicleSearchByVinRequest criteria = new GtmotiveVehicleSearchByVinRequest();
    criteria.setVin("WVWZZZ3CZCE152232");
    criteria.setEstimateId("11111011538649502269");
    GtmotiveVehicleSearchResponse response =
        gtmotiveBusinessService.searchVehicleByVin("1111101", criteria);
    assertNotNull(response);
  }

  @Test
  public void searchGtPartsList_shouldReturnGtPartList_givenValidSearchCriteria() throws Exception {
    GtmotivePartsListSearchRequest searchRequest = GtmotivePartsListSearchRequest.builder()
        .umc("VW04801").equipments(Arrays.asList("CR05")).build();
    GtmotivePartsListSearchResponse response =
        gtmotiveBusinessService.searchGtPartsList(searchRequest);
    assertNotNull(response.getFunctionalGroups());
  }
}
