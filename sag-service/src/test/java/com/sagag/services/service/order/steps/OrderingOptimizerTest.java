package com.sagag.services.service.order.steps;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.order.steps.orderrequest.OrderingOptimizationExectutor;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderingOptimizerTest {

  @InjectMocks
  private OrderingOptimizationExectutor orderingOptimizer;

  @Test
  @Ignore("Danh Please update UT")
  public void optimizeShouldSwitchAxToVenExternal() throws Exception {
    Availability a1_axAvai =
        Availability.builder().arrivalTime("2020-02-20T06:10:00Z").quantity(10).build();
    Availability a1_venAvai1 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z")
        .quantity(10).externalSource(true).venExternalSource(true).build();
    Availability a1_venAvai2 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z")
        .quantity(20).externalSource(true).venExternalSource(true).build();
    ArticleDocDto article1 = new ArticleDocDto();
    article1.setAmountNumber(20);
    article1.setAvailabilities(Arrays.asList(a1_axAvai, a1_venAvai1));
    article1.setVenExternalAvailability(a1_venAvai2);
    ShoppingCartItem cartItem1 = new ShoppingCartItem();
    cartItem1.setArticle(article1);

    Availability a2_venAvai1 = Availability.builder().arrivalTime("2020-02-20T10:10:00Z")
        .quantity(20).externalSource(true).venExternalSource(true).build();
    ArticleDocDto article2 = new ArticleDocDto();
    article2.setAmountNumber(20);
    article2.setAvailabilities(Arrays.asList(a2_venAvai1));
    article2.setVenExternalAvailability(a2_venAvai1);
    ShoppingCartItem cartItem2 = new ShoppingCartItem();
    cartItem2.setArticle(article2);

    List<ShoppingCartItem> cartItems = Arrays.asList(cartItem1, cartItem2);
    orderingOptimizer.optimize(cartItems);

    ArticleDocDto switchArticle = cartItems.get(0).getArticle();
    assertThat(switchArticle.getAvailabilities().size(), Matchers.is(1));
    assertTrue(switchArticle.getAvailabilities().get(0).isVenExternalSource());
    assertThat(switchArticle.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T10:10:00Z"));
  }

  @Test
  @Ignore("Danh Please update UT")
  public void optimizeShouldSwitchAxToVenExternal_1() throws Exception {
    Availability a1_axAvai =
        Availability.builder().arrivalTime("2020-02-20T06:10:00Z").quantity(10).build();
    Availability a1_venAvai1 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z")
        .quantity(10).externalSource(true).venExternalSource(true).build();
    Availability a1_venAvai2 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z")
        .quantity(20).externalSource(true).venExternalSource(true).build();
    ArticleDocDto article1 = new ArticleDocDto();
    article1.setAmountNumber(20);
    article1.setAvailabilities(Arrays.asList(a1_axAvai, a1_venAvai1));
    article1.setVenExternalAvailability(a1_venAvai2);
    ShoppingCartItem cartItem1 = new ShoppingCartItem();
    cartItem1.setArticle(article1);

    Availability a2_venAvai1 = Availability.builder().arrivalTime("2020-02-20T10:10:00Z")
        .quantity(20).externalSource(true).venExternalSource(true).build();
    ArticleDocDto article2 = new ArticleDocDto();
    article2.setAmountNumber(20);
    article2.setAvailabilities(Arrays.asList(a2_venAvai1));
    article2.setVenExternalAvailability(a2_venAvai1);
    ShoppingCartItem cartItem2 = new ShoppingCartItem();
    cartItem2.setArticle(article2);

    Availability a3_venAvai1 = Availability.builder().arrivalTime("2020-02-20T09:10:00Z")
        .quantity(20).externalSource(true).venExternalSource(true).build();
    ArticleDocDto article3 = new ArticleDocDto();
    article3.setAmountNumber(20);
    article3.setAvailabilities(Arrays.asList(a3_venAvai1));
    article3.setVenExternalAvailability(a3_venAvai1);
    ShoppingCartItem cartItem3 = new ShoppingCartItem();
    cartItem3.setArticle(article3);

    List<ShoppingCartItem> cartItems = Arrays.asList(cartItem1, cartItem2, cartItem3);
    orderingOptimizer.optimize(cartItems);

    ArticleDocDto switchArticle = cartItems.get(0).getArticle();
    assertThat(switchArticle.getAvailabilities().size(), Matchers.is(1));
    assertTrue(switchArticle.getAvailabilities().get(0).isVenExternalSource());
    assertThat(switchArticle.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T10:10:00Z"));

    ArticleDocDto optimizedDeliveryTimeArticle = cartItems.get(2).getArticle();
    assertThat(optimizedDeliveryTimeArticle.getAvailabilities().size(), Matchers.is(1));
    assertTrue(optimizedDeliveryTimeArticle.getAvailabilities().get(0).isVenExternalSource());
    assertThat(optimizedDeliveryTimeArticle.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T10:10:00Z"));
  }

  @Test
  @Ignore("Danh Please update UT")
  public void optimizeShouldSwitchToVenExternalAndKeepAx() throws Exception {
    Availability a1_axAvai =
        Availability.builder().arrivalTime("2020-02-20T06:10:00Z").quantity(10).build();
    Availability a1_venAvai1 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z")
        .quantity(10).externalSource(true).venExternalSource(true).build();
    Availability a1_venAvai2 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z")
        .quantity(20).externalSource(true).venExternalSource(true).build();
    ArticleDocDto article1 = new ArticleDocDto();
    article1.setAmountNumber(20);
    article1.setAvailabilities(Arrays.asList(a1_axAvai, a1_venAvai1));
    article1.setVenExternalAvailability(a1_venAvai2);
    ShoppingCartItem cartItem1 = new ShoppingCartItem();
    cartItem1.setArticle(article1);

    Availability a2_venAvai1 = Availability.builder().arrivalTime("2020-02-20T10:10:00Z")
        .quantity(20).externalSource(true).venExternalSource(true).build();
    ArticleDocDto article2 = new ArticleDocDto();
    article2.setAmountNumber(20);
    article2.setAvailabilities(Arrays.asList(a2_venAvai1));
    article2.setVenExternalAvailability(a2_venAvai1);
    ShoppingCartItem cartItem2 = new ShoppingCartItem();
    cartItem2.setArticle(article2);

    Availability a3_venAvai1 = Availability.builder().arrivalTime("2020-02-20T09:10:00Z")
        .quantity(20).externalSource(false).venExternalSource(false).build();
    ArticleDocDto article3 = new ArticleDocDto();
    article3.setAmountNumber(20);
    article3.setAvailabilities(Arrays.asList(a3_venAvai1));
    ShoppingCartItem cartItem3 = new ShoppingCartItem();
    cartItem3.setArticle(article3);

    List<ShoppingCartItem> cartItems = Arrays.asList(cartItem1, cartItem2, cartItem3);
    orderingOptimizer.optimize(cartItems);

    ArticleDocDto switchArticle = cartItems.get(0).getArticle();
    assertThat(switchArticle.getAvailabilities().size(), Matchers.is(1));
    assertTrue(switchArticle.getAvailabilities().get(0).isVenExternalSource());
    assertThat(switchArticle.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T10:10:00Z"));

    ArticleDocDto axArticle = cartItems.get(2).getArticle();
    assertThat(axArticle.getAvailabilities().size(), Matchers.is(1));
    assertFalse(axArticle.getAvailabilities().get(0).isVenExternalSource());
    assertThat(axArticle.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T09:10:00Z"));
  }

  @Test
  public void optimizeShouldAllKeepAx() throws Exception {
    Availability a1_axAvai =
        Availability.builder().arrivalTime("2020-02-20T06:10:00Z").quantity(10).build();
    ArticleDocDto article1 = new ArticleDocDto();
    article1.setAmountNumber(20);
    article1.setAvailabilities(Arrays.asList(a1_axAvai));
    ShoppingCartItem cartItem1 = new ShoppingCartItem();
    cartItem1.setArticle(article1);

    Availability a2_venAvai1 = Availability.builder().arrivalTime("2020-02-20T10:10:00Z")
        .quantity(20).externalSource(false).venExternalSource(false).build();
    ArticleDocDto article2 = new ArticleDocDto();
    article2.setAmountNumber(20);
    article2.setAvailabilities(Arrays.asList(a2_venAvai1));
    ShoppingCartItem cartItem2 = new ShoppingCartItem();
    cartItem2.setArticle(article2);

    List<ShoppingCartItem> cartItems = Arrays.asList(cartItem1, cartItem2);
    orderingOptimizer.optimize(cartItems);

    ArticleDocDto ax1 = cartItems.get(0).getArticle();
    assertThat(ax1.getAvailabilities().size(), Matchers.is(1));
    assertFalse(ax1.getAvailabilities().get(0).isVenExternalSource());
    assertThat(ax1.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T06:10:00Z"));

    ArticleDocDto ax2 = cartItems.get(1).getArticle();
    assertThat(ax2.getAvailabilities().size(), Matchers.is(1));
    assertFalse(ax2.getAvailabilities().get(0).isVenExternalSource());
    assertThat(ax2.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T10:10:00Z"));
  }
}
