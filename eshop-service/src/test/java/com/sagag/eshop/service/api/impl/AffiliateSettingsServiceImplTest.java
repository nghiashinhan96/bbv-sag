package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.VatTypeDisplaySettingService;
import com.sagag.eshop.service.validator.avail.filter.AffiliateAvailabilityDisplaySettingValidator;
import com.sagag.eshop.service.validator.avail.filter.AffiliateSettingAvailabilityValidator;
import com.sagag.eshop.service.validator.avail.filter.BrandPriorityAvailabilityFilterValidator;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayOption;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayState;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class AffiliateSettingsServiceImplTest {

  @InjectMocks
  private AffiliateSettingsServiceImpl affiliateSettingsService;

  @Mock
  private OrganisationRepository organisationRepo;

  @Mock
  private OrganisationCollectionService organisationCollectionService;

  @Mock
  private BrandPriorityAvailabilityFilterValidator brandPriorityAvailabilityFilterValidator;

  @Mock
  private AffiliateSettingAvailabilityValidator affiliateAvailabilitySettingValidator;

  @Mock
  private VatTypeDisplaySettingService vatTypeDisplaySettingService;

  @Mock
  private LanguageRepository languageRepo;

  @Mock
  private AffiliateAvailabilityDisplaySettingValidator availabilityDisplaySettingValidator;
  
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void updateSettings_shouldDonothing() throws Exception {

    final String shortName = "derendinger-at";
    final Double vatValue = 18D;
    final BoAffiliateSettingRequest requestBody = BoAffiliateSettingRequest.builder()
        .shortName(shortName).vat(vatValue).customerAbsEnabled(true)
        .c4sBrandPriorityAvailFilter("011").customerBrandPriorityAvailFilter("010").build();

    when(organisationRepo.findOneByShortname(shortName))
        .thenReturn(Optional.of(new Organisation(1, null, null, 1, null)));
    when(organisationCollectionService.findByAffiliateId(1)).thenReturn(Collections.emptyList());
    Mockito.when(brandPriorityAvailabilityFilterValidator.validate(Mockito.anyString()))
        .thenReturn(true);
    Mockito.when(affiliateAvailabilitySettingValidator.validate(Mockito.any())).thenReturn(true);

    affiliateSettingsService.updateSettings(requestBody);

    Mockito.verify(organisationRepo, times(1)).findOneByShortname(shortName);
    Mockito.verify(organisationCollectionService, times(1)).findByAffiliateId(1);
  }

  @Test
  public void updateSettings_shouldUpdateAbsAndVatAndVatTypeDisplay() throws Exception {

    final String shortName = "derendinger-at";
    final String vatTypeDisplay = "11";
    final Double vatValue = 18D;

    boolean abs = true;
    final BoAffiliateSettingRequest requestBody = BoAffiliateSettingRequest.builder()
        .shortName(shortName).vat(vatValue).customerAbsEnabled(abs).salesAbsEnabled(abs)
        .c4sBrandPriorityAvailFilter("011").customerBrandPriorityAvailFilter("010")
        .vatTypeDisplay(vatTypeDisplay).build();

    int affId = 2;
    int collectionId = 3;
    List<OrganisationCollection> collections =
        Arrays.asList(new OrganisationCollection(collectionId, null, null, affId, null));
    when(organisationRepo.findOneByShortname(shortName))
        .thenReturn(Optional.of(new Organisation(affId, null, null, 1, null)));
    when(organisationCollectionService.findByAffiliateId(affId)).thenReturn(collections);
    Mockito.doNothing().when(organisationCollectionService).updateVatSetting(collectionId,
        vatValue);
    Mockito.doNothing().when(organisationCollectionService).updateAbsCustomerSetting(collectionId,
        abs);
    Mockito.when(brandPriorityAvailabilityFilterValidator.validate(Mockito.anyString()))
        .thenReturn(true);
    Mockito.when(affiliateAvailabilitySettingValidator.validate(Mockito.any())).thenReturn(true);
    Mockito.when(languageRepo.findAll())
        .thenReturn(Arrays.asList(Language.builder().langiso("en").build()));
    Mockito.when(availabilityDisplaySettingValidator.validate(Mockito.any())).thenReturn(true);

    affiliateSettingsService.updateSettings(requestBody);

    Mockito.verify(organisationRepo, times(1)).findOneByShortname(shortName);
    Mockito.verify(organisationCollectionService, times(1)).findByAffiliateId(affId);
    Mockito.verify(organisationCollectionService, times(1)).updateVatSetting(collectionId,
        vatValue);
    Mockito.verify(organisationCollectionService, times(1)).updateAbsCustomerSetting(collectionId,
        abs);
    Mockito.verify(vatTypeDisplaySettingService, times(1)).updateVatTypeDisplay(collectionId,
        vatTypeDisplay);
    Mockito.verify(organisationCollectionService, times(1)).updateAbsSalesSetting(collectionId,
        abs);
  }
 
  @Test
  public void updateSettingsShouldUpdateAvailDisplaySetting() throws Exception {

    final String shortName = "derendinger-at";
    final String vatTypeDisplay = "11";
    final Double vatValue = 18D;
    final List<String> langs = new ArrayList<String>();
    langs.add("de");
    final List<AffiliateAvailabilityDisplaySettingDto> availSettings = Arrays.asList(
        AffiliateAvailabilityDisplaySettingDto.builder()
            .availState(AvailabilityDisplayState.SAME_DAY)
            .displayOption(AvailabilityDisplayOption.NONE).title("Same Day").color("Red").build(),
        AffiliateAvailabilityDisplaySettingDto.builder()
            .availState(AvailabilityDisplayState.NOT_AVAILABLE)
            .displayOption(AvailabilityDisplayOption.DOT).title("Not Available").color("Blue")
            .build());
    
    boolean abs = true;
    final BoAffiliateSettingRequest requestBody = BoAffiliateSettingRequest.builder()
        .shortName(shortName).vat(vatValue).customerAbsEnabled(abs).salesAbsEnabled(abs)
        .c4sBrandPriorityAvailFilter("011").customerBrandPriorityAvailFilter("010")
        .availDisplaySettings(availSettings).vatTypeDisplay(vatTypeDisplay).build();

    int affId = 2;
    int collectionId = 3;
    List<OrganisationCollection> collections =
        Arrays.asList(new OrganisationCollection(collectionId, null, null, affId, null));
    when(organisationRepo.findOneByShortname(shortName))
        .thenReturn(Optional.of(new Organisation(affId, null, null, 1, null)));
    when(organisationCollectionService.findByAffiliateId(affId)).thenReturn(collections);
    Mockito.doNothing().when(organisationCollectionService).updateVatSetting(collectionId,
        vatValue);
    Mockito.doNothing().when(organisationCollectionService).updateAbsCustomerSetting(collectionId,
        abs);
    Mockito.when(brandPriorityAvailabilityFilterValidator.validate(Mockito.anyString()))
        .thenReturn(true);
    Mockito.when(affiliateAvailabilitySettingValidator.validate(Mockito.any())).thenReturn(true);
    Mockito.when(languageRepo.findAll())
        .thenReturn(Arrays.asList(Language.builder().langiso("en").build()));
    Mockito.when(availabilityDisplaySettingValidator.validate(Mockito.any())).thenReturn(true);

    affiliateSettingsService.updateSettings(requestBody);

    Mockito.verify(organisationRepo, times(1)).findOneByShortname(shortName);
    Mockito.verify(organisationCollectionService, times(1)).findByAffiliateId(affId);
    Mockito.verify(organisationCollectionService, times(1)).updateVatSetting(collectionId,
        vatValue);
    Mockito.verify(organisationCollectionService, times(1)).updateAbsCustomerSetting(collectionId,
        abs);
    Mockito.verify(vatTypeDisplaySettingService, times(1)).updateVatTypeDisplay(collectionId,
        vatTypeDisplay);
    Mockito.verify(organisationCollectionService, times(1))
        .updateAvailabilityDisplaySetting(collectionId, availSettings);
    Mockito.verify(organisationCollectionService, times(1)).updateAvailabilitySetting(Mockito.any(),
        Mockito.any());
    Mockito.verify(organisationCollectionService, times(1)).updateAbsSalesSetting(collectionId,
        abs);
  }

}
