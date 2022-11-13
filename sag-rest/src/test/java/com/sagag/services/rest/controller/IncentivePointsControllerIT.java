package com.sagag.services.rest.controller;

import com.sagag.eshop.service.api.MailService;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.rest.app.RestApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests class for Incentive points REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest @Ignore
public class IncentivePointsControllerIT extends AbstractControllerIT {

  private static final String INCENTIVE = "/incentive";

  @Mock
  private MailService mailService;

  @Ignore("Waiting for real data")
  @Test
  public void getHappyPoints() throws Exception {
    performGet(INCENTIVE + "/points").andExpect(status().isOk());
  }

  @Test
  public void getHappyPoints_NotFound() throws Exception {
    performGet(INCENTIVE + "/points").andExpect(status().isNotFound());
  }
}
