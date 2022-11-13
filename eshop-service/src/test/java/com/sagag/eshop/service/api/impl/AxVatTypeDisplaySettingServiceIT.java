package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

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
public class AxVatTypeDisplaySettingServiceIT {

  @Autowired
  private DefaultVatTypeDisplaySettingServiceImpl vatTypeDisplaySettingService;

  @Autowired
  private OrganisationCollectionService organisationCollectionService;

  @Test
  public void updateVatTypeDisplay_shouldUpdateVatTypeDisplay() throws Exception {
    final Integer collectionId = 2; // derendinger-at
    final String vatTypeDisplay = "11";

    vatTypeDisplaySettingService.updateVatTypeDisplay(collectionId, vatTypeDisplay);
    Optional<OrganisationCollectionsSettings> result =
        organisationCollectionService.findSettingsByCollectionIdAndKey(collectionId,
            Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName());
    Assert.assertThat(result.isPresent(), Matchers.equalTo(true));
    Assert.assertEquals(result.get().getSettingValue(), vatTypeDisplay);
  }


  @Test(expected = IllegalArgumentException.class)
  public void updateVatTypeDisplay_shouldThrowException() throws Exception {
    final Integer collectionId = 2; // derendinger-at
    final String vatTypeDisplay = "1";
    vatTypeDisplaySettingService.updateVatTypeDisplay(collectionId, vatTypeDisplay);
  }

  @Test
  public void findSettingsByOrgShortnameShouldReturnDefaultValue() {
    final String shortName = "matik-at";
    Optional<OrganisationCollectionsSettings> result =
        vatTypeDisplaySettingService.findSettingsByOrgShortname(shortName);
    Assert.assertThat(result.isPresent(), Matchers.equalTo(true));
  }

  @Test
  public void findSettingsByCollectionShortnameShouldReturnValue() {
    final String shortName = "derendinger-at";
    final String valueExpect = "11";
    Optional<OrganisationCollectionsSettings> result =
        vatTypeDisplaySettingService.findSettingsByCollectionShortname(shortName);
    Assert.assertThat(result.isPresent(), Matchers.equalTo(true));
    Assert.assertEquals(result.get().getSettingValue(), valueExpect);
  }



}
