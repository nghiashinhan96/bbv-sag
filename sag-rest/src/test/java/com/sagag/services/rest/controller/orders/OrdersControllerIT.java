package com.sagag.services.rest.controller.orders;

import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.cart.CategoryDto;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.rest.app.RestApplication;
import com.sagag.services.rest.controller.AbstractControllerIT;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests class for Order REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest
@Ignore("Should re-work with new changes of us")
public class OrdersControllerIT extends AbstractControllerIT {

  @Test
  public void testCreateOrderSuccessful() throws Exception {

    addArticleToShoppingCart();

    performPost("/order/create?timezone=America/Chicago", buildOrderRequest())
        .andExpect(jsonPath("$", Matchers.notNullValue()));
  }

  @Test
  public void testCreateOrderSuccessfulAX() throws Exception {
    addArticleToShoppingCart();
    performPost("/order/create?timezone=America/Chicago", buildOrderRequest())
        .andExpect(jsonPath("$", Matchers.notNullValue()));
  }

  @Test
  public void testCreateOrderSuccessfulWithITLang() throws Exception {

    addVinPackageToShoppingCart();
    performPost("/order/create?lang=it&timezone=America/Chicago", buildOrderRequest())
        .andExpect(jsonPath("$", Matchers.notNullValue()));
  }

  @Test
  public void testCreateOrderSuccessfulWithFRLang() throws Exception {

    addVinPackageToShoppingCart();
    performPost("/order/create?lang=fr&timezone=America/Chicago", buildOrderRequest())
        .andExpect(jsonPath("$", Matchers.notNullValue()));
  }

  @Test
  public void testGetAggregationsOk() throws Exception {
    performGet("/order/aggregations").andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testViewOrderHistoryFilterAllOk() throws Exception {
    performGet("/order/view?orderStatus=ALL&username=ALL").andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testViewOrderHistoryFilterByDateOk() throws Exception {
    performGet(
        "/order/view?orderStatus=CAPTURING_FINISHED&dateFrom=2016-06-06&dateTo=2017-06-06&username=me")
            .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testViewOrderHistoryFilterByOrderStatus1Ok() throws Exception {
    performGet("/order/view?orderStatus=PARTLY_INVOICED&username=ALL")
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testViewOrderDetailOk() throws Exception {
    final Long orderId = Long.valueOf(10008);
    performGet("/order/view/" + orderId).andExpect(status().isOk());
  }

  @Test
  public void testViewOrderDetailNotFoundResult() throws Exception {
    final Long orderId = Long.valueOf(0);
    performGet("/order/view/" + orderId).andExpect(status().isNotFound());
  }

  @Test
  public void testViewOrderDetailWithWrongFormatId() throws Exception {
    performGet("/order/view/aaaa").andExpect(status().isBadRequest());
  }

  private ExternalOrderRequest buildOrderRequest() {
    final ExternalOrderRequest orderRequest = new ExternalOrderRequest();
    orderRequest.setSendMethodCode(SendMethodType.TOUR.name());
    orderRequest.setPaymentMethod(PaymentMethodType.CREDIT.name());
    orderRequest.setPartialDelivery(true);
    return orderRequest;
  }

  private void addArticleToShoppingCart() throws Exception {
    performPost("/cart/article/1000000024342259383/add", buildShoppingCartRequest());
  }

  private void addVinPackageToShoppingCart() throws Exception {
    performPost("/vin/addToCart?packageNumber=1111");
  }

  private ShoppingCartRequestBody buildShoppingCartRequest() {
    final ShoppingCartRequestBody request = new ShoppingCartRequestBody();
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1000000020566519156");
    article.setSalesQuantity(1);
    final CategoryDto category = new CategoryDto();
    category.setGaId("82");
    category.setGaDesc("Bremsscheibe");
    category.setRootDesc("Bremsen");
    final VehicleDto vehicle = new VehicleDto();
    vehicle.setVehId("V56798M24881");
    request.setArticle(article);
    request.setCategory(category);
    request.setVehicle(vehicle);
    request.setQuantity(1);
    return request;
  }

}
