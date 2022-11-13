package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class OrganisationServiceImplIT {

  @Autowired
  private OrganisationServiceImpl service;

  @Test
  public void testFindOrgSettingByKey() {
    final String shortName = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    final String settingKey = SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName();
    Optional<String> settingValue = service.findOrgSettingByKey(shortName, settingKey);
    Assert.assertThat(settingValue.isPresent(), Matchers.is(true));
    settingValue.ifPresent(value -> Assert.assertThat(value, Matchers.is("shop@derendinger.at")));
  }

}
