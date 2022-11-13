package com.sagag.services.common.utils;

import com.sagag.services.common.enums.SupportedAffiliate;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Stream;

public class WholesalerUtilsTest {

  private static final String[] WHOLESALER_AFFILIATES = new String[] {
      "eh-ch",
      "eh-at"
  };

  private static final String[] CUSTOMER_AFFILIATES = Stream.of(SupportedAffiliate.values())
      .map(SupportedAffiliate::getAffiliate).toArray(String[]::new);

  @Test
  public void shouldReturnTrueWithValidWholesalerAffiliates() {
    for (String aff : WHOLESALER_AFFILIATES) {
      Assert.assertThat(WholesalerUtils.isFinalCustomerEndpoint(aff), Matchers.is(true));
    }
  }

  @Test
  public void shouldReturnFalseWithInvalidWholesalerAffiliates() {
    final String[] wholesalerAffiliates = new String[] {
        "derendinger-at-eh-*",
        "derendinger-eh-ch",
        StringUtils.EMPTY,
        " matik-ch-eh "
    };

    for (String aff : wholesalerAffiliates) {
      Assert.assertThat(WholesalerUtils.isFinalCustomerEndpoint(aff), Matchers.is(false));
    }
  }

  @Test
  public void shouldReturnFalseWithValidCustomerAffiliates() {
    for (String aff : CUSTOMER_AFFILIATES) {
      Assert.assertThat(WholesalerUtils.isFinalCustomerEndpoint(aff), Matchers.is(false));
    }
  }

  @Test
  public void shouldReturnFalseWithValidCustomerNrAndCustAff() {
    final String orgCode = "11100004";
    for (String aff : CUSTOMER_AFFILIATES) {
      Assert.assertThat(WholesalerUtils.isFinalCustomer(aff, orgCode), Matchers.is(false));
    }
  }

  @Test
  public void shouldReturnFalseWithEmptyCustomerNrAndCustAff() {
    final String orgCode = StringUtils.EMPTY;
    for (String aff : CUSTOMER_AFFILIATES) {
      Assert.assertThat(WholesalerUtils.isFinalCustomer(aff, orgCode), Matchers.is(false));
    }
  }

  @Test
  public void shouldReturnFalseWithValidCustomerNrAndWholesalerAff() {
    final String orgCode = "11100004";
    for (String aff : WHOLESALER_AFFILIATES) {
      Assert.assertThat(WholesalerUtils.isFinalCustomer(aff, orgCode), Matchers.is(false));
    }
  }

  @Test
  public void shouldReturnTrueWithEmptyCustomerNrAndWholesalerAff() {
    final String orgCode = StringUtils.EMPTY;
    for (String aff : WHOLESALER_AFFILIATES) {
      Assert.assertThat(WholesalerUtils.isFinalCustomer(aff, orgCode), Matchers.is(true));
    }
  }

}
