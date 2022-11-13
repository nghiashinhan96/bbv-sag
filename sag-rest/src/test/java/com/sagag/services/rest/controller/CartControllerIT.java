package com.sagag.services.rest.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.rest.app.RestApplication;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests class for Shopping Cart REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest
@Ignore("Should re-work with new changes of us")
public class CartControllerIT extends AbstractControllerIT {

  private static final String CART_ROOT_URL = "/cart";

  private static final String ADD_ARTICLE_URL =
      "/article/1000000011401161422/add?quantity=2&curGaId=234&curGenArtDesc=Geberzylinde&rootDesc=Bremsen&vehicleInfo=HYUNDAI i30 (FD) 1.6 90 kW G4FC";

  @Test
  public void testAddShoppingCart() throws Exception {
    performPost(CART_ROOT_URL + ADD_ARTICLE_URL).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  private void addArticleToShoppingCart() throws Exception {
    performPost(CART_ROOT_URL + ADD_ARTICLE_URL);
  }

  @Test
  public void testUpdateShoppingCart0() throws Exception {
    performPost(CART_ROOT_URL + "/article/1000000011401161422/update?quantity=0&vehId=V7265M5155")
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test
  public void testUpdateShoppingCart() throws Exception {
    addArticleToShoppingCart();
    performPost(CART_ROOT_URL + "/article/1000000011401161422/update?quantity=3&vehId=V7265M5155")
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test
  public void testRemoveShoppingCart0() throws Exception {
    addArticleToShoppingCart();
    performPost(CART_ROOT_URL + "/article/remove?idsSagSys=1000000011401161422&vehId=V7265M5155")
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test
  public void shouldForbiddenGetCategoriesByVehicleId() throws Exception {
    performPost(CART_ROOT_URL + "/parts/1000000011401161422/add?quantity=2&vehId=V7265M5155")
        .andExpect(status().isForbidden());
  }

  @Test
  public void shouldBeCartUnauthorized() throws Exception {
    performGet(CART_ROOT_URL + "/order/5112NF").andExpect(status().isUnauthorized());
  }

  @Test
  public void shouldForbiddenCart() throws Exception {
    performGet(CART_ROOT_URL + "/order?cateIds=78&vehIds=V25477M20342")
        .andExpect(status().isForbidden());
  }
}
