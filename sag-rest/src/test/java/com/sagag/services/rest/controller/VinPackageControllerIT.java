package com.sagag.services.rest.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.rest.app.RestApplication;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration tests class for Vin REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest @Ignore
public class VinPackageControllerIT extends AbstractControllerIT {
  private static final String VIN_ROOT_URL = "/vin";

  @Test()
  public void testAddVinPackageOrder() throws Exception {
    MvcResult response =
        performPost(VIN_ROOT_URL + "/addToCart?packageNumber=1111").andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
    Assert.assertTrue("1".equals(response.getResponse().getContentAsString()));
  }

  @Test()
  public void testGetAvailableVinCalls() throws Exception {
    performGet(VIN_ROOT_URL + "/calls").andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test()
  public void testGetVinPackages() throws Exception {
    performGet(VIN_ROOT_URL + "/packages").andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }
}
