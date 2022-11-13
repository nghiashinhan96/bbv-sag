package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.OrganisationCollectionException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayOption;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayState;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.AvailabilitySettingMasterDataDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateSettingDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingRequest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class AffiliateSettingsServiceImplIT {

  @Autowired
  private AffiliateSettingsServiceImpl affiliateSettingsServiceImpl;

  @Autowired
  private AffiliateServiceImpl affiliateServiceImpl;

  @Test
  public void testUpdateSettingsShouldUpdateAvailDisplaySettings()
      throws OrganisationCollectionException {
    final String shortName = "derendinger-at";
    final String vatTypeDisplay = "10";
    boolean abs = false;

    final List<String> langs = new ArrayList<String>();
    langs.add("de");
    final List<AffiliateAvailabilityDisplaySettingDto> availSettings =
        Arrays.asList(AffiliateAvailabilityDisplaySettingDto.builder()
            .availState(AvailabilityDisplayState.SAME_DAY)
            .displayOption(AvailabilityDisplayOption.NONE).title("Same Day").color("Red").build());

    final BoAffiliateSettingRequest requestBody = BoAffiliateSettingRequest.builder().vat(18D)
        .salesAbsEnabled(true).shortName(shortName).customerBrandFilterEnabled(true)
        .salesBrandFilterEnabled(true).ksoEnabled(true).customerAbsEnabled(abs)
        .c4sBrandPriorityAvailFilter("011").availDisplaySettings(availSettings)
        .customerBrandPriorityAvailFilter("010").vatTypeDisplay(vatTypeDisplay).build();

    affiliateSettingsServiceImpl.updateSettings(requestBody);

    BackOfficeAffiliateSettingDto result = affiliateServiceImpl.getSettings(shortName);
    Assert.assertNotNull(result);
    Assert.assertFalse(result.isCustomerAbsEnabled());
    Assert.assertEquals(result.getVatTypeDisplay(), vatTypeDisplay);
    Assert.assertEquals(result.getC4sBrandPriorityAvailFilter(), "011");
    Assert.assertEquals(result.getCustomerBrandPriorityAvailFilter(), "010");
    Assert.assertEquals(1, result.getAvailDisplaySettings().size());
    Assert.assertEquals(AvailabilityDisplayState.SAME_DAY,
        result.getAvailDisplaySettings().get(0).getAvailState());
    Assert.assertEquals(AvailabilityDisplayOption.NONE,
        result.getAvailDisplaySettings().get(0).getDisplayOption());
  }


  @Test
  public void testUpdateSettingsShouldUpdateAvailDisplayKeySetting()
      throws OrganisationCollectionException {
    final String shortName = "derendinger-at";
    final String vatTypeDisplay = "10";
    boolean abs = false;

    final List<AffiliateAvailabilityDisplaySettingDto> availSettings =
        Arrays.asList(AffiliateAvailabilityDisplaySettingDto.builder()
            .availState(AvailabilityDisplayState.NOT_AVAILABLE)
            .detailAvailText(Collections.emptyList()).listAvailText(Collections.emptyList())
            .displayOption(AvailabilityDisplayOption.DOT).title("Not Available").color("Blue")
            .build());

    final BoAffiliateSettingRequest requestBody = BoAffiliateSettingRequest.builder().vat(18D)
        .salesAbsEnabled(true).shortName(shortName).customerBrandFilterEnabled(true)
        .salesBrandFilterEnabled(true).ksoEnabled(true).customerAbsEnabled(abs)
        .c4sBrandPriorityAvailFilter("011").availDisplaySettings(availSettings)
        .customerBrandPriorityAvailFilter("010").vatTypeDisplay(vatTypeDisplay).build();

    affiliateSettingsServiceImpl.updateSettings(requestBody);

    BackOfficeAffiliateSettingDto result = affiliateServiceImpl.getSettings(shortName);
    Assert.assertNotNull(result);
    Assert.assertFalse(result.isCustomerAbsEnabled());
    Assert.assertEquals(result.getVatTypeDisplay(), vatTypeDisplay);
    Assert.assertEquals(result.getC4sBrandPriorityAvailFilter(), "011");
    Assert.assertEquals(result.getCustomerBrandPriorityAvailFilter(), "010");
    Assert.assertEquals(1, result.getAvailDisplaySettings().size());
    Assert.assertTrue(result.getAvailDisplaySettings().get(0).getAvailState().isNotAvailable());
    Assert.assertTrue(result.getAvailSetting().getAvailIcon());
    Assert.assertFalse(result.getAvailSetting().getDropShipmentAvail());
  }

  @Test
  public void testUpdateSettings_shouldUpdateSuccess() throws OrganisationCollectionException {
    final String shortName = "derendinger-at";
    final String vatTypeDisplay = "10";
    boolean abs = false;

    final BoAffiliateSettingRequest requestBody =
        BoAffiliateSettingRequest.builder().vat(18D).salesAbsEnabled(true).shortName(shortName)
            .customerBrandFilterEnabled(true).salesBrandFilterEnabled(true).ksoEnabled(true)
            .customerAbsEnabled(abs).c4sBrandPriorityAvailFilter("011")
            .customerBrandPriorityAvailFilter("010").vatTypeDisplay(vatTypeDisplay).build();

    affiliateSettingsServiceImpl.updateSettings(requestBody);

    BackOfficeAffiliateSettingDto result = affiliateServiceImpl.getSettings(shortName);
    Assert.assertNotNull(result);
    Assert.assertFalse(result.isCustomerAbsEnabled());
    Assert.assertEquals(result.getVatTypeDisplay(), vatTypeDisplay);
    Assert.assertEquals(result.getC4sBrandPriorityAvailFilter(), "011");
    Assert.assertEquals(result.getCustomerBrandPriorityAvailFilter(), "010");
    Assert.assertEquals(1, result.getAvailDisplaySettings().size());
    Assert.assertTrue(result.getAvailDisplaySettings().get(0).getAvailState().isNotAvailable());
    Assert.assertEquals(AvailabilityDisplayOption.DISPLAY_TEXT,
        result.getAvailDisplaySettings().get(0).getDisplayOption());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateSettings_shouldThrowException() throws OrganisationCollectionException {
    final String shortName = "derendinger-at";
    final String vatTypeDisplay = "";
    boolean abs = false;

    final BoAffiliateSettingRequest requestBody =
        BoAffiliateSettingRequest.builder().vat(18D).salesAbsEnabled(true).shortName(shortName)
            .customerBrandFilterEnabled(true).salesBrandFilterEnabled(true).ksoEnabled(true)
            .customerAbsEnabled(abs).c4sBrandPriorityAvailFilter("01")
            .customerBrandPriorityAvailFilter("01").vatTypeDisplay(vatTypeDisplay).build();

    affiliateSettingsServiceImpl.updateSettings(requestBody);
  }

  @Test
  public void testGetAvailabilitySettingMasterData() throws OrganisationCollectionException {

    AvailabilitySettingMasterDataDto masterDto =
        affiliateSettingsServiceImpl.getAvailabilitySettingMasterData();
    Assert.assertNotNull(masterDto);
    Assert.assertThat(masterDto.getSupportedLanguages().size(), Matchers.greaterThan(6));


  }
}
