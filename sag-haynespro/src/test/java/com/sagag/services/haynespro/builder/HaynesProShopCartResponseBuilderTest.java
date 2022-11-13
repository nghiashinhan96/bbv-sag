package com.sagag.services.haynespro.builder;

import com.sagag.services.haynespro.HaynesProDataProvider;

import org.junit.Test;

public class HaynesProShopCartResponseBuilderTest {

  @Test
  public void shouldReturnHaynesProShopCart() {
    new HaynesProShopCartResponseBuilder(
        HaynesProDataProvider.parseXmlToBufferedReader()).build();
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNPE() {
    new HaynesProShopCartResponseBuilder(null).build();
  }
}
