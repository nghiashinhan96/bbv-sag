package com.sagag.services.thule.config;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.thule.DataProvider;
import com.sagag.services.thule.ThuleApplication;
import com.sagag.services.thule.api.ThuleService;
import com.sagag.services.thule.domain.BuyersGuideData;
import com.sagag.services.thule.domain.ThuleProperties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ThuleApplication.class })
@EshopIntegrationTest
public class AtThuleConfigurationIT {

  @Autowired
  private ThuleProperties properties;

  @Autowired
  private ThuleService thuleService;

  @Autowired
  private ThuleConfiguration configuration;

  @Test
  public void testThuleConfiguration() {
    Assert.assertThat(configuration, Matchers.notNullValue());
  }

  @Test
  public void testThulePropertiesWhenAtProfile() {
    Assert.assertThat(properties.getEndpoint(), Matchers.notNullValue());
    Assert.assertThat(properties.getDealerIdMap(), Matchers.notNullValue());
  }

  @Test
  public void testThuleServiceWhenAtProfile() {
    final Optional<String> dealerIdValueOpt = thuleService.findThuleDealerUrlByAffiliate(
        SupportedAffiliate.DERENDINGER_AT.getAffiliate(), true, Locale.GERMAN);
    Assert.assertThat(dealerIdValueOpt.isPresent(), Matchers.is(false));

    final Map<String, String> formData = DataProvider.buildSampleMap();
    final Optional<BuyersGuideData> buyerGuideData = thuleService.addBuyersGuide(formData);
    Assert.assertThat(buyerGuideData.isPresent(), Matchers.is(true));
  }


}
