package com.sagag.services.rest.controller;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.rest.app.RestApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests class for Vehicle History REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest @Ignore
public class VehicleHistoryControllerIT extends AbstractControllerIT {

  @Test
  public void testGetVehicleHistorySuccessful() throws Exception {
    performGet("/history/vehicle").andExpect(status().isOk())
        .andExpect(json().isPresent());

  }

  @Test
  public void testGetVehicleHistoryNotFound() throws Exception {
    performGet("/history/vehicle").andExpect(status().isNotFound());

  }

  @Test
  public void testAccessHistoryForbidden() throws Exception {
    performGet("/history/vehicle").andExpect(status().isForbidden());
  }
}
