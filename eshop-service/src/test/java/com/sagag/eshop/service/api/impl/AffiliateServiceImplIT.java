package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.api.AffiliateService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateSettingDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateShortInfoDto;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

/**
 * Integration tests for affiliate service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class AffiliateServiceImplIT {

  @Autowired
  private AffiliateService affiliateService;

  @Test
  public void getGetShortInfosByCountry_AT() {
    final List<BackOfficeAffiliateShortInfoDto> affiliates =
        affiliateService.getShortInfosByCountry("at");
    Assert.assertThat(CollectionUtils.isEmpty(affiliates), Is.is(false));
    Assert.assertThat(affiliates.size(), Is.is(2));
  }

  @Test
  public void getGetShortInfosByCountry_CH() {
    final List<BackOfficeAffiliateShortInfoDto> affiliates =
        affiliateService.getShortInfosByCountry("ch");
    Assert.assertThat(CollectionUtils.isEmpty(affiliates), Is.is(false));
    Assert.assertThat(affiliates.size(), Matchers.greaterThanOrEqualTo(4));
  }

  @Test
  public void getSettings_Derendinger_AT() {
    final String shortName = "derendinger-at";
    BackOfficeAffiliateSettingDto settingDto = affiliateService.getSettings(shortName);

    Assert.assertNotNull(settingDto);
    Assert.assertTrue(settingDto.isCustomerAbsEnabled());
    Assert.assertEquals(settingDto.getVatTypeDisplay(), "11");
    Assert.assertTrue(settingDto.getAvailSetting().getAvailIcon());
  }

  @Test(expected = NoSuchElementException.class)
  public void getGetShortInfosByCountry_NotSupport() {
    affiliateService.getShortInfosByCountry("ABC");
  }

  @Test(expected = IllegalArgumentException.class)
  public void getGetShortInfosByCountry_Null() {
    affiliateService.getShortInfosByCountry(null);
  }
}
