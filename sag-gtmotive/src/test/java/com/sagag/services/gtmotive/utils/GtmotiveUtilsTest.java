package com.sagag.services.gtmotive.utils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class GtmotiveUtilsTest {

  @Test
  public void shouldGenerateEstimateIdWithExistedCustomer() {
    final String custNr = "1100005";
    String estimateId = GtmotiveUtils.generateEstimateId(custNr);
    Assert.assertThat(estimateId, Matchers.containsString(custNr));
  }

  @Test
  public void shouldGenerateEstimateIdWithNoCustomer() {
    String estimateId = GtmotiveUtils.generateEstimateId(null);
    Assert.assertThat(estimateId, Matchers.notNullValue());
  }

}
