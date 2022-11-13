package com.sagag.services.service.mail.orderconfirmation;

import static org.junit.Assert.assertThat;

import com.sagag.services.common.annotation.CzEshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagPriceUtils;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.DataProvider;
import com.sagag.services.service.SagServiceApplication;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@CzEshopIntegrationTest
@SpringBootTest(classes = SagServiceApplication.class)
public class OrderConfirmationMailSenderCustomByAffDataBuilderFactoryIT {

  @Autowired
  private OrderConfirmationMailSenderCustomByAffDataBuilderFactory factory;

  @Test
  public void testGetDisplayedArrivalTimeWithCustomerView() {
    final ShoppingCartItem cartItem = DataProvider.buildShoppingCartItem(
        Calendar.getInstance().getTime());
    String arrivalTime = factory.getDisplayedArrivalTime(cartItem, TimeZone.getDefault(),
        Locale.GERMAN, false);

    assertThat(arrivalTime, Matchers.not(Matchers.isEmptyOrNullString()));
  }

  @Test
  public void testGetDisplayedArrivalTimeWithFinalCustomerView() {
    final ShoppingCartItem cartItem = DataProvider.buildShoppingCartItem(
        Calendar.getInstance().getTime());
    String arrivalTime = factory.getDisplayedArrivalTime(cartItem, TimeZone.getDefault(),
        Locale.GERMAN, true);

    assertThat(arrivalTime, Matchers.not(Matchers.isEmptyOrNullString()));
  }

  @Test
  public void testGetDisplayedPriceTypeWithFalseFlag() {
    final ShoppingCartItem cartItem = DataProvider.buildShoppingCartItem(
        Calendar.getInstance().getTime());
    String priceType = factory.getDisplayedPriceType(cartItem, false,
        SupportedAffiliate.STAKIS_CZECH);
    assertThat(priceType, Matchers.isEmptyOrNullString());
  }

  @Test
  public void testGetDisplayedPriceTypeWithTrueFlagAndDisplayedPrice() {
    final ShoppingCartItem cartItem = DataProvider.buildShoppingCartItem(
        Calendar.getInstance().getTime());

    String priceType = factory.getDisplayedPriceType(cartItem, true,
        SupportedAffiliate.STAKIS_CZECH);
    assertThat(priceType, Matchers.is("Typ Brand"));
  }

  @Test
  public void testGetDisplayedPriceTypeWithTrueFlagAndGrossPrice() {
    final ShoppingCartItem cartItem = DataProvider.buildShoppingCartItem(
        Calendar.getInstance().getTime());
    cartItem.getArticle().setDisplayedPrice(null);

    String priceType = factory.getDisplayedPriceType(cartItem, true,
        SupportedAffiliate.STAKIS_CZECH);
    assertThat(priceType, Matchers.not(SagPriceUtils.GROSS_PRICE_TYPE));
  }
}
