package com.sagag.services.common.utils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SagJSONUtilTest {

  @Test
  public void testConvertListToJson() {
    List<String> foo = new ArrayList<String>();
    foo.add("A");
    foo.add("B");
    foo.add("C");
    String json = SagJSONUtil.convertObjectToJson(foo);
    Assert.assertTrue(json.contains("A"));
  }

  @Test
  public void testConvertJsonToList() {
    String json = "[\"A\", \"B\",\"C\"]";
    List<String> foos =
        SagJSONUtil.convertArrayJsonToList(json, String.class);
    Assert.assertTrue(foos.size() == 3);
  }

  @Test
  public void testConvertJsonToObject() {

    final String json = "{\"username\":\"gdt.admin\",\"companyName\":\"Derendinger-Switzerland\","
        + "\"affiliateInUrl\":\"derendinger-ch\","
        + "\"affiliateEmail\":\"shop@derendinger.ch\",\"netPriceView\":true,"
        + "\"customer\":{},\"lastName\":\"Karlou_A\",\"firstName\":null,"
        + "\"email\":\"Lefkothea.Karlou@sag-ag.ch\",\"language\":\"de\","
        + "\"emailOrderConfirmation\":false,\"sessionTimeoutSeconds\":3600,"
        + "\"id\":69,\"customerAddress\":[],\"vatConfirm\":true,"
        + "\"roleName\":\"USER_ADMIN\",\"gaTrackingCode\":null,\"hourlyRate\":null,"
        + "\"showTyresDiscount\":false,\"showTyresGrossPriceHeader\":false,\"salesUser\":false,"
        + "\"normalUser\":false,\"userAdminRole\":true}";
    SagJSONUtil.convertJsonToObject(json, Object.class);
  }

  @Test
  public void jsonToMap() {
    final String json =
        "{\"1\":\"1001432669\",\"2\":\"1001432735\",\"3\":\"1001432734\",\"4\":\"1001432668\"}";
    final Map<String, String> mapValues = SagJSONUtil.jsonToMap(json);

    Assert.assertThat(mapValues.isEmpty(), Matchers.is(false));
  }

}
