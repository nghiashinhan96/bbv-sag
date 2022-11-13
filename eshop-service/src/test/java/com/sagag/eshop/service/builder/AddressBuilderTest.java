package com.sagag.eshop.service.builder;

import com.sagag.services.common.contants.SagConstants;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;


public class AddressBuilderTest {

  @Test
  public void testBuildAddressNoInfo() {

    final AddressBuilder builder = new AddressBuilder();
    final String addressStr = builder.build();

    Assert.assertEquals(StringUtils.EMPTY, addressStr);
  }

  @Test
  public void testBuildAddressNoSeparator() {

    final AddressBuilder builder = new AddressBuilder();
    builder.companyName("bbv Switzerland").street("123").postCode("7777").city("Bern")
    .country("Switzerland");
    final String addressStr = builder.build();
    Assert.assertEquals("bbv Switzerland, 123, 7777, Bern, Switzerland", addressStr);
  }

  @Test
  public void testBuildAddressWithoutCountry() {

    final AddressBuilder builder = new AddressBuilder();
    builder.companyName("bbv Switzerland").street("123").postCode("7777").city("Bern");
    final String addressStr = builder.build();
    Assert.assertEquals("bbv Switzerland, 123, 7777, Bern", addressStr);
  }

  @Test
  public void testBuildAddressWithSemicolonSepartor() {

    final AddressBuilder builder = new AddressBuilder();
    builder.companyName("bbv Switzerland").street("123").postCode("7777").city("Bern")
    .country("Switzerland").separator(SagConstants.SEMICOLON);
    final String addressStr = builder.build();
    Assert.assertEquals("bbv Switzerland;123;7777;Bern;Switzerland", addressStr);
  }

}
