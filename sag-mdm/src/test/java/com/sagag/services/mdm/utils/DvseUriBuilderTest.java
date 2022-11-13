package com.sagag.services.mdm.utils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

@Slf4j
public class DvseUriBuilderTest {

  private static final String URI = "https://web1.dvse.de/loginh.aspx?SID=";

  private static final String SID = "443001";

  @Test
  public void test() {
    String[] saleIds = { "salesId1", "salesId2", "salesId2", "salesId3", "salesId1",
        StringUtils.SPACE };
    String uri = DvseUriBuilder.builder()
        .uri(URI).sid(SID)
        .username("user1")
        .password("password1")
        .saleId("salesId1")
        .build().getUri();
    System.out.println(String.format("URI = %s", uri));
    for (String saleId : saleIds) {
      uri = DvseUriBuilder.builder()
          .uri(uri).saleId(saleId).build().getUri();
      log.debug("URI = {}", uri);
      System.out.println(String.format("URI = %s", uri));
    }

  }

}
