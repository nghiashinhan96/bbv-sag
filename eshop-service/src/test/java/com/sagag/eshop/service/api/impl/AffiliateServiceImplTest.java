package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;

import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.utils.AffiliateSettingConstants;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayOption;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateSettingDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateShortInfoDto;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Test class for affiliate service.
 *
 */
@RunWith(SpringRunner.class)
public class AffiliateServiceImplTest {

  @InjectMocks
  private AffiliateServiceImpl affiliateService;

  @Mock
  private OrganisationRepository orgRepo;

  @Mock
  private OrganisationServiceImpl organisationService;

  @Mock
  private OrganisationCollectionService organisationCollectionService;

  @Mock
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Mock
  private LanguageRepository languageRepo;

  private Map<String, String> getSettingsMap_Derendinger_CH() {
    return new HashMap<String, String>() {
      /**
       *
       */
      private static final long serialVersionUID = -6665027412220727712L;

      {
        put(SettingsKeys.Affiliate.Availability.AVAILABILITY_DISPLAY.toLowerName(),
            "[{\"availState\":\"NOT_AVAILABLE\",\"detailAvailText\":[{\"langIso\":\"DE\",\"content\":\"Sollte der Artikel nicht innerhalb von 24 Std lieferbar sein, werden Sie informiert.\"},{\"langIso\":\"EN\",\"content\":\"If the item is not available within 24 hours, you will be informed.\"},{\"langIso\":\"CS\",\"content\":\"Pokud \u010dl\u00e1nek nebude k dispozici do 24 hodin, budete o tom informov\u00e1ni.\"}],\"listAvailText\":[{\"langIso\":\"DE\",\"content\":\"Der Artikel ist nicht Verf\u00fcgbar\"},{\"langIso\":\"EN\",\"content\":\"The item is not available\"},{\"langIso\":\"CS\",\"content\":\"\u010cl\u00e1nek nen\u00ed k dispozici\"}],\"title\":\"Not Available\",\"color\":\"#0073be\",\"displayOption\":\"DISPLAY_TEXT\"}]");
        put(SettingsKeys.Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName(),
            AffiliateSettingConstants.DEFAULT_VAT_TYPE_DISPLAY);
        put(SettingsKeys.Affiliate.Settings.IS_CUSTOMER_ABS_ENABLED.toLowerName(), "true");
        put(SettingsKeys.Affiliate.Settings.CUSTOMER_BRAND_PRIORITY_AVAIL_FILTER.toLowerName(),
            "111");
        put(SettingsKeys.Affiliate.Availability.DETAIL_AVAIL_TEXT
            .appendWithString(SagConstants.UNDERSCORE + "DE").toLowerCase(), "Sollte");
      }
    };
  }

  @Test
  public void getGetShortInfosByCountry_AT() {
    final List<Organisation> organisations = Collections
        .singletonList(new Organisation("Derendinger-Austria", "300000", "derengdinger-at"));
    Mockito.when(organisationService.findOrganisationByTypeName(Mockito.anyString()))
        .thenReturn(organisations);
    Mockito.when(supportedAffiliateRepo.findCompanyNameByCountryShortCode(Mockito.anyString()))
        .thenReturn(Arrays.asList(
            com.sagag.services.common.enums.SupportedAffiliate.DERENDINGER_AT.getCompanyName()));
    final List<BackOfficeAffiliateShortInfoDto> affiliates =
        affiliateService.getShortInfosByCountry("at");
    Assert.assertThat(CollectionUtils.isEmpty(affiliates), Is.is(false));
    Mockito.verify(organisationService, times(1)).findOrganisationByTypeName(Mockito.anyString());
  }

  @Test
  public void getGetShortInfosByCountry_CH() {
    final List<Organisation> organisations = Collections
        .singletonList(new Organisation("Derendinger-Switzerland", "200000", "derendinger-ch"));
    Mockito.when(organisationService.findOrganisationByTypeName(Mockito.anyString()))
        .thenReturn(organisations);
    Mockito.when(supportedAffiliateRepo.findCompanyNameByCountryShortCode(Mockito.anyString()))
        .thenReturn(Arrays.asList(
            com.sagag.services.common.enums.SupportedAffiliate.DERENDINGER_CH.getCompanyName()));
    final List<BackOfficeAffiliateShortInfoDto> affiliates =
        affiliateService.getShortInfosByCountry("ch");
    Assert.assertThat(CollectionUtils.isEmpty(affiliates), Is.is(false));
    Mockito.verify(organisationService, times(1)).findOrganisationByTypeName(Mockito.anyString());
  }

  @Test
  public void getSettings_Derendinger_CH() {
    final String shortName = "derendinger-ch";
    final Organisation org =
        new Organisation("Derendinger-Switzerland", "200000", "derendinger-ch");
    Mockito.when(orgRepo.findOneByShortname(shortName)).thenReturn(Optional.of(org));

    Mockito.when(organisationCollectionService.findSettingsByOrgShortname(shortName))
        .thenReturn(getSettingsMap_Derendinger_CH());

    Mockito.when(languageRepo.findAll())
        .thenReturn(Collections.singletonList(Language.builder().langiso("DE").build()));

    BackOfficeAffiliateSettingDto settingDto = affiliateService.getSettings(shortName);

    Assert.assertEquals(settingDto.getCustomerBrandPriorityAvailFilter(), "111");
    Assert.assertEquals(settingDto.getVatTypeDisplay(),
        AffiliateSettingConstants.DEFAULT_VAT_TYPE_DISPLAY);
    Assert.assertTrue(settingDto.isCustomerAbsEnabled());
    Assert
        .assertTrue(
            settingDto.getAvailSetting().getDetailAvailText().stream()
                .filter(availLange -> StringUtils.equals(availLange.getLangIso(), "DE")
                    && StringUtils.equals(availLange.getContent(), "Sollte"))
                .findAny().isPresent());
    Assert.assertEquals(1, settingDto.getAvailDisplaySettings().size());
    Assert.assertTrue(settingDto.getAvailDisplaySettings().get(0).getAvailState().isNotAvailable());
    Assert.assertEquals(AvailabilityDisplayOption.DISPLAY_TEXT,
        settingDto.getAvailDisplaySettings().get(0).getDisplayOption());

    Mockito.verify(organisationCollectionService, times(1))
        .findSettingsByOrgShortname(Mockito.anyString());
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
