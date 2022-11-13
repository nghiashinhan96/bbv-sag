package com.sagag.services.incentive.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.IncentiveApplication;
import com.sagag.services.incentive.api.OutletService;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IncentiveApplication.class })
@EshopIntegrationTest
@Slf4j
public class OutletServiceImplIT {

  @Autowired
  private OutletService service;

  @Test
  public void testGetOutletUrl() {
    final SupportedAffiliate[] affiliates = { SupportedAffiliate.DERENDINGER_CH,
        SupportedAffiliate.TECHNOMAG };
    final String lang = Locale.GERMAN.getLanguage();
    final String email = "nu1.gc@sag-ag.ch";
    final String username = "nu1.gc";
    final long custNr = 111111l;

    for (SupportedAffiliate affiliate : affiliates) {
      final String url = service.getOutletUrl(affiliate, lang, email, username, custNr);
      log.debug("Outlet URL = {}", url);
      Assert.assertThat(url, Matchers.notNullValue());
    }
  }

}
