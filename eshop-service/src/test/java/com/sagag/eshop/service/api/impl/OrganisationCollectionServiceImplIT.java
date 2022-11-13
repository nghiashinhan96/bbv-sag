package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
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
public class OrganisationCollectionServiceImplIT {

  @Autowired
  private OrganisationCollectionServiceImpl collectionService;

  @Test
  public void testUpdateVatTypeDisplay_shouldUpdateSuccess() {
    final Integer collectionId = 2;
    final String settingValue = "11";

    collectionService.updateVatTypeDisplay(collectionId, settingValue);

    Optional<OrganisationCollectionsSettings> result =
        collectionService.findSettingsByCollectionIdAndKey(collectionId,
            Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName());
    Assert.assertThat(result.isPresent(), Matchers.is(true));
    result
        .ifPresent(value -> Assert.assertThat(value.getSettingValue(), Matchers.is(settingValue)));
  }


  @Test
  public void testUpdateVatTypeDisplay_shouldCreateSuccess() {
    final Integer collectionId = 3;
    final String settingValue = "11";

    Optional<OrganisationCollectionsSettings> settingVatDisplay =
        collectionService.findSettingsByCollectionIdAndKey(collectionId,
            Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName());

    Assert.assertThat(settingVatDisplay.isPresent(), Matchers.is(false));

    collectionService.updateVatTypeDisplay(collectionId, settingValue);

    Optional<OrganisationCollectionsSettings> result =
        collectionService.findSettingsByCollectionIdAndKey(collectionId,
            Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName());
    Assert.assertThat(result.isPresent(), Matchers.is(true));
    result
        .ifPresent(value -> Assert.assertThat(value.getSettingValue(), Matchers.is(settingValue)));
  }

}
