package com.sagag.services.rest.controller;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.rest.app.RestApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests class for Coupon REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest @Ignore
public class CouponsControllerIT extends AbstractControllerIT {

  private static final String CUOPONS_ROOT_URL = "/coupons";

  @Test()
  public void testValidateCoupons_Expired() throws Exception {
    performPost(CUOPONS_ROOT_URL + "/addToCart?couponCode=DEV0001")
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test()
  public void testValidateCoupons_CodeNotExist() throws Exception {
    performPost(CUOPONS_ROOT_URL + "/addToCart?couponCode=TestCode")
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test()
  public void testValidateCoupons_ExceededMax() throws Exception {
    performPost(CUOPONS_ROOT_URL + "/addToCart?couponCode=DEV0002")
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test()
  public void testValidateCoupons_CustomerNrWrong() throws Exception {
    performPost(CUOPONS_ROOT_URL + "/addToCart?couponCode=DEVCUST0001")
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }


  @Test()
  public void testValidateCoupons_Customer1() throws Exception {
    performPost(CUOPONS_ROOT_URL + "/addToCart?couponCode=DEVCAT0001")
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }
}
