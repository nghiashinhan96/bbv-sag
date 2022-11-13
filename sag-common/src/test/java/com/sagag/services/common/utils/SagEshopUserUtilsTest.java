package com.sagag.services.common.utils;

import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class SagEshopUserUtilsTest {

  private static final int MAX_LENGTH_OF_USERNAME = 32;

  @Test
  public void givenEmptyCustomerNameShouldGetDefaulNoCustomerName() {
    final String customerName = StringUtils.EMPTY;
    executeAndAssertGetDefaultVirtualUsername(customerName, "No Customer Name");
  }

  @Test
  public void givenValidCustomerNameShouldStartWholeName() {
    final String customerName = "Garage Braun AG";
    executeAndAssertGetDefaultVirtualUsername(customerName, customerName);
  }

  @Test
  public void givenLongCustomerNameShouldStartPartOfNameCase1() {
    final String customerName = "Gotthard-Garage Calcagni GmbH";
    executeAndAssertGetDefaultVirtualUsername(customerName, "Gotthard-Garage Ca");
  }

  @Test
  public void givenLongCustomerNameShouldStartPartOfNameCase2() {
    final String customerName = "INSIDERPOWER, Philipp Christen";
    executeAndAssertGetDefaultVirtualUsername(customerName, "INSIDERPOWER, Phili");
  }

  private void executeAndAssertGetDefaultVirtualUsername(String customerName, String expected) {
    String username = SagEshopUserUtils.getDefaultVirtualUsername(customerName);
    assertThat(username,  Matchers.startsWith(expected));
    assertThat(username.length(), Matchers.lessThanOrEqualTo(MAX_LENGTH_OF_USERNAME));
  }

  @Test
  public void givenUsernameShouldGetDefaultHaynesProUsername() {
    final Map<String, String> actualAndExpectedMap = new LinkedHashMap<>();
    actualAndExpectedMap
    .put("Pneuhaus Luegisland AG1634026780818", "Pneuhaus Luegisland AG1634026780");

    actualAndExpectedMap
    .put("LA-Garage1634026783512", "LA-Garage1634026783512");

    actualAndExpectedMap
    .put("Bucher Agro-Technik AG1634024732396", "Bucher Agro-Technik AG1634024732");

    actualAndExpectedMap
    .put("INSIDERPOWER, Philipp Christen1634027939044", "INSIDERPOWER, Philipp Christen16");
    actualAndExpectedMap
    .put(StringUtils.EMPTY, StringUtils.EMPTY);

    actualAndExpectedMap.forEach(this::executeAndAssertDefaultHaynesProUsername);
  }

  private void executeAndAssertDefaultHaynesProUsername(String username, String expected) {
    final String actual = SagEshopUserUtils.defaultHaynesProUsername(username);
    assertThat(actual, Matchers.is(expected));
    assertThat(actual.length(), Matchers.lessThanOrEqualTo(MAX_LENGTH_OF_USERNAME));
  }

}
