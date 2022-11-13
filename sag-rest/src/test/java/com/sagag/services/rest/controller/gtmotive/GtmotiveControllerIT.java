package com.sagag.services.rest.controller.gtmotive;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.gtmotive.app.GtInterfaceAccountsConfiguration;
import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import com.sagag.services.gtmotive.domain.request.GtmotiveRequestMode;
import com.sagag.services.rest.app.RestApplication;
import com.sagag.services.rest.controller.AbstractControllerIT;
import com.sagag.services.service.request.GetGtmotiveUrlRequestBody;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Integration tests class for GTMotive REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest
@Ignore
public class GtmotiveControllerIT extends AbstractControllerIT {

  private static final String SEARCH_ROOT_URL = "/gtmotive";

  @Autowired
  private GtInterfaceAccountsConfiguration configuration;

  @Test
  public void shouldMethodNotAllowedGetGtmotiveUrl() throws Exception {
    performGet(SEARCH_ROOT_URL + "/graphics").andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void testGetGraphicalIFrameInfoForVin() throws Exception {

    final String vin = "WBAPH5G59ANM35638";
    final GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setVin(vin);
    criteria.setRequestMode(GtmotiveRequestMode.VIN);
    criteria.bindGtmotiveProfile(configuration.getAccounts().getDe());

    final GetGtmotiveUrlRequestBody body = new GetGtmotiveUrlRequestBody();
    body.setCriteria(criteria);
    performPost(SEARCH_ROOT_URL + "/graphics", body).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.estimateId", Is.is(Matchers.notNullValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.url", Is.is(Matchers.notNullValue())));
  }

  @Test
  public void testGetGraphicalIFrameInfoForVehicle() throws Exception {
    final GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setEstimateId(String.valueOf(System.currentTimeMillis()));
    criteria.setVehicle("V58944M27193");
    final GetGtmotiveUrlRequestBody body = new GetGtmotiveUrlRequestBody();
    body.setCriteria(criteria);

    performPost(SEARCH_ROOT_URL + "/graphics", body).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.estimateId", Is.is(Matchers.notNullValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.url", Is.is(Matchers.notNullValue())));
  }

}
