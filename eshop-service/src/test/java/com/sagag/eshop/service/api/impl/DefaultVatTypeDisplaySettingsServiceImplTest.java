package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.collection.OrganisationCollectionsSettingsRepository;
import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.utils.AffiliateSettingConstants;
import com.sagag.eshop.service.validator.vat.display.VatTypeDisplayValidator;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class DefaultVatTypeDisplaySettingsServiceImplTest {

  @InjectMocks
  private DefaultVatTypeDisplaySettingServiceImpl defaultVatTypeDisplaySettingServiceImpl;

  @Mock
  private OrganisationCollectionsSettingsRepository orgCollectionSettingsRepo;

  @Mock
  private OrganisationCollectionService collectionService;

  @Spy
  private VatTypeDisplayValidator vatTypeDisplayValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void findSettingsByOrgShortnameShouldReturnDefaultValue() {
    final String shortName = "derendinger-at";
    when(orgCollectionSettingsRepo.findSettingValueByOrgShortnameAndSettingKey(shortName,
        Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName())).thenReturn(Optional.empty());

    Optional<OrganisationCollectionsSettings> result =
        defaultVatTypeDisplaySettingServiceImpl.findSettingsByOrgShortname(shortName);

    Assert.assertTrue(result.isPresent());
    Assert.assertThat(result.get().getSettingValue(),
        Matchers.is(AffiliateSettingConstants.DEFAULT_VAT_TYPE_DISPLAY));
  }

  @Test
  public void findSettingsByOrgShortnameShouldReturnValue() {
    final String shortName = "derendinger-at";
    final String value = "11";
    when(orgCollectionSettingsRepo.findSettingValueByOrgShortnameAndSettingKey(shortName,
        Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName())).thenReturn(Optional.of(value));

    Optional<OrganisationCollectionsSettings> result =
        defaultVatTypeDisplaySettingServiceImpl.findSettingsByOrgShortname(shortName);

    Assert.assertTrue(result.isPresent());
    Assert.assertThat(result.get().getSettingValue(), Matchers.is(value));
  }

  @Test
  public void findSettingsByCollectionShortnameShouldReturnDefaultValue() {
    final String shortName = "derendinger-at";
    when(orgCollectionSettingsRepo.findSettingValueByCollectionShortnameAndSettingKey(shortName,
        Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName())).thenReturn(Optional.empty());

    Optional<OrganisationCollectionsSettings> result =
        defaultVatTypeDisplaySettingServiceImpl.findSettingsByCollectionShortname(shortName);

    Assert.assertTrue(result.isPresent());
    Assert.assertThat(result.get().getSettingValue(),
        Matchers.is(AffiliateSettingConstants.DEFAULT_VAT_TYPE_DISPLAY));
  }

  @Test
  public void findSettingsByCollectionShortnameShouldReturnValue() {
    final String shortName = "derendinger-at";
    final String value = "11";
    when(orgCollectionSettingsRepo.findSettingValueByCollectionShortnameAndSettingKey(shortName,
        Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName())).thenReturn(Optional.of(value));

    Optional<OrganisationCollectionsSettings> result =
        defaultVatTypeDisplaySettingServiceImpl.findSettingsByCollectionShortname(shortName);

    Assert.assertTrue(result.isPresent());
    Assert.assertThat(result.get().getSettingValue(), Matchers.is(value));
  }

  @Test(expected = IllegalArgumentException.class)
  public void updateVatTypeDisplay_shouldThrowException() throws Exception {
    final Integer collectionId = 1;
    final String vatTypeDisplay = "234";

    defaultVatTypeDisplaySettingServiceImpl.updateVatTypeDisplay(collectionId, vatTypeDisplay);
  }

  @Test
  public void updateVatTypeDisplay_shouldUpdateVatTypeDisplay() throws Exception {
    final Integer collectionId = 1;
    final String vatTypeDisplay = "11";

    defaultVatTypeDisplaySettingServiceImpl.updateVatTypeDisplay(collectionId, vatTypeDisplay);

    Mockito.verify(vatTypeDisplayValidator, times(1)).validate(vatTypeDisplay);
    Mockito.verify(collectionService, times(1)).updateVatTypeDisplay(collectionId, vatTypeDisplay);
  }

}
