package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.collection.CollectionPermissionRepository;
import com.sagag.eshop.repo.api.collection.CollectionRelationRepository;
import com.sagag.eshop.repo.api.collection.OrganisationCollectionRepository;
import com.sagag.eshop.repo.api.collection.OrganisationCollectionsSettingsRepository;
import com.sagag.eshop.repo.api.collection.VCollectionRepository;
import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate;
import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate.Theme;
import com.sagag.eshop.repo.entity.VCollectionSearch;
import com.sagag.eshop.repo.entity.collection.CollectionPermission;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.eshop.service.exception.OrganisationCollectionException;
import com.sagag.eshop.service.exception.OrganisationCollectionException.OrganisationCollectionErrorCase;
import com.sagag.services.domain.eshop.backoffice.dto.CollectionSearchResultDto;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayOption;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayState;
import com.sagag.services.domain.eshop.criteria.CollectionSearchCriteria;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class OrganisationCollectionServiceImplTest {

  @InjectMocks
  private OrganisationCollectionServiceImpl collectionService;

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Mock
  private VCollectionRepository vCollectionRepo;

  @Mock
  private OrganisationCollectionRepository orgCollectionRepo;

  @Mock
  private OrganisationRepository orgRepo;

  @Mock
  private OrganisationCollectionsSettingsRepository orgCollectionSettingsRepo;

  @Mock
  private CollectionPermissionRepository collectionPermissionRepository;

  @Mock
  private CollectionRelationRepository collectionRelationRepository;

  @Mock
  private DefaultVatTypeDisplaySettingServiceImpl vatTypeDisplaySettingService;

  @Test
  public void searchCollection_shouldReturnCollections_givenAffilate() throws Exception {
    final String affiliate = "wbb";
    CollectionSearchCriteria criteria =
        CollectionSearchCriteria.builder().affiliate(affiliate).build();

    Page<VCollectionSearch> collectionSearchPage =
        new PageImpl<>(Arrays.asList(VCollectionSearch.builder().affiliate(affiliate).build()));

    Mockito.when(vCollectionRepo.findAll(ArgumentMatchers.<Specification<VCollectionSearch>>any(),
        Mockito.any(Pageable.class))).thenReturn(collectionSearchPage);

    Page<CollectionSearchResultDto> results = collectionService.searchCollection(criteria);

    Mockito.verify(orgCollectionRepo, times(0)).findCollectionNameByOrgCode(Mockito.anyString());
    assertThat(results.getContent().get(0).getAffiliate(), Matchers.is(affiliate));
  }

  @Test
  public void searchCollection_shouldReturnCollections_givenCustomerNr() throws Exception {
    final String customerNr = "8000016";
    CollectionSearchCriteria criteria =
        CollectionSearchCriteria.builder().customerNr(customerNr).build();
    List<String> collectionByOrgCode = Arrays.asList("Matik-Austria");
    Mockito.when(orgCollectionRepo.findCollectionNameByOrgCode(Mockito.anyString()))
        .thenReturn(collectionByOrgCode);

    Page<VCollectionSearch> collectionSearchPage =
        new PageImpl<>(Arrays.asList(VCollectionSearch.builder().customerNr(customerNr).build()));

    Mockito.when(vCollectionRepo.findAll(ArgumentMatchers.<Specification<VCollectionSearch>>any(),
        Mockito.any(Pageable.class))).thenReturn(collectionSearchPage);
    Page<CollectionSearchResultDto> results = collectionService.searchCollection(criteria);
    assertThat(results.getContent().get(0).getCustomerNr(), Matchers.is(customerNr));
  }

  @Test
  public void update_shouldThrowOrganisationCollectionException_WhenCall_1() throws OrganisationCollectionException {
    expectedEx.expect(OrganisationCollectionException.class);
    expectedEx.expect(Matchers.hasProperty("code", Matchers.is(OrganisationCollectionErrorCase.OCE_NFA_001.name())));
    final String affShortName = "derendinger-at";
    Mockito.when(orgRepo.findIdByShortName(affShortName)).thenReturn(Optional.empty());
    OrganisationCollectionDto request = OrganisationCollectionDto.builder()
        .affiliateShortName(affShortName)
        .name(affShortName)
        .permissions(anyList())
        .build();
    collectionService.update(request);
  }

  @Test
  public void update_shouldThrowOrganisationCollectionException_WhenCall_2() throws OrganisationCollectionException {
    expectedEx.expect(OrganisationCollectionException.class);
    expectedEx.expect(Matchers.hasProperty("code", Matchers.is(OrganisationCollectionErrorCase.OCE_NFC_001.name())));
    final String affShortName = "derendinger-at";
    Mockito.when(orgRepo.findIdByShortName(affShortName)).thenReturn(Optional.of(1));
    OrganisationCollectionDto request = OrganisationCollectionDto.builder()
        .affiliateShortName(affShortName)
        .name(affShortName)
        .permissions(anyList())
        .build();
    collectionService.update(request);
  }

  @Test
  public void update_shouldThrowOrganisationCollectionException_WhenCall_3() throws OrganisationCollectionException {
    expectedEx.expect(OrganisationCollectionException.class);
    expectedEx.expect(Matchers.hasProperty("code", Matchers.is(OrganisationCollectionErrorCase.OCE_CCA_001.name())));
    final String affShortName = "derendinger-at";
    Mockito.when(orgRepo.findIdByShortName(affShortName)).thenReturn(Optional.of(1));
    Mockito.when(orgCollectionRepo.findByShortname(affShortName)).thenReturn(Optional.of(
        OrganisationCollection.builder()
            .affiliateId(2)
            .name(affShortName)
            .shortname(affShortName)
            .build()));
    OrganisationCollectionDto request = OrganisationCollectionDto.builder()
        .affiliateShortName(affShortName)
        .name(affShortName)
        .permissions(anyList())
        .collectionShortName(affShortName)
        .build();
    collectionService.update(request);
  }

  @Test
  public void update_shouldThrowOrganisationCollectionException_WhenCall_5() throws OrganisationCollectionException {
    expectedEx.expect(OrganisationCollectionException.class);
    expectedEx.expect(Matchers.hasProperty("code", Matchers.is(OrganisationCollectionErrorCase.OCE_CEL_001.name())));
    final String affShortName = "derendinger-at";
    Mockito.when(orgRepo.findIdByShortName(affShortName)).thenReturn(Optional.of(1));
    Mockito.when(orgCollectionRepo.findByShortname(affShortName)).thenReturn(Optional.of(
        OrganisationCollection.builder()
            .affiliateId(1)
            .name(affShortName)
            .shortname(affShortName)
            .build()));
    Mockito.when(orgCollectionSettingsRepo.findSettingsByOrgShortname(affShortName))
        .thenReturn(Arrays.asList(OrganisationCollectionsSettings.builder()
            .settingKey(Theme.LOGO_IMAGE.toLowerName())
            .settingValue("simpleValue").build()));
    Mockito.when(collectionPermissionRepository.findByCollectionShortName(affShortName))
        .thenReturn(Arrays.asList(CollectionPermission.builder().eshopPermissionId(20).build()));
    //collectionRelationRepo.countByCollectionId
    Mockito.when(collectionRelationRepository.countByCollectionId(anyInt()))
        .thenReturn(1000L);
    Mockito.when(vatTypeDisplaySettingService.findSettingsByOrgShortname(Mockito.anyString()))
        .thenReturn(Optional.empty());

    OrganisationCollectionDto request = OrganisationCollectionDto.builder()
        .affiliateShortName(affShortName)
        .name(affShortName)
        .permissions(Arrays.asList(PermissionConfigurationDto.builder().permissionId(20).enable(false).build()))
        .collectionShortName(affShortName)
        .build();
    collectionService.update(request);
  }

  @Test
  public void updateVatTypeDisplayShouldUpdateSuccess() {
    final Integer collectionId = 2;
    final String settingValue = "11";

    Mockito
        .when(orgCollectionSettingsRepo.findByCollectionIdAndKey(collectionId,
            Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName()))
        .thenReturn(Optional.of(
            OrganisationCollectionsSettings.builder().collectionId(collectionId).settingValue("10")
                .settingKey(Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName()).build()));

    collectionService.updateVatTypeDisplay(collectionId, settingValue);
    verify(orgCollectionSettingsRepo, times(1)).findByCollectionIdAndKey(collectionId,
        Affiliate.Settings.VAT_TYPE_DISPLAY.toLowerName());
    verify(orgCollectionSettingsRepo, times(1)).saveAll(Mockito.any());
  }
  
  @Test
  public void updateAvailabilityDisplaySettingShouldUpdateSuccess() {
    final Integer collectionId = 2;
    final AffiliateAvailabilityDisplaySettingDto settingValue =
        AffiliateAvailabilityDisplaySettingDto.builder()
            .availState(AvailabilityDisplayState.NOT_AVAILABLE)
            .displayOption(AvailabilityDisplayOption.DISPLAY_TEXT).title("Not Available").build();
    final String settingValueStr =
        "[{\"availState\":\"NOT_AVAILABLE\",\"detailAvailText\":[{\"langIso\":\"DE\",\"content\":\"Sollte der Artikel nicht innerhalb von 24 Std lieferbar sein, werden Sie informiert.\"},{\"langIso\":\"EN\",\"content\":\"If the item is not available within 24 hours, you will be informed.\"},{\"langIso\":\"CS\",\"content\":\"Pokud \u010dl\u00e1nek nebude k dispozici do 24 hodin, budete o tom informov\u00e1ni.\"}],\"listAvailText\":[{\"langIso\":\"DE\",\"content\":\"Der Artikel ist nicht Verf\u00fcgbar\"},{\"langIso\":\"EN\",\"content\":\"The item is not available\"},{\"langIso\":\"CS\",\"content\":\"\u010cl\u00e1nek nen\u00ed k dispozici\"}],\"title\":\"Not Available\",\"color\":\"#0073be\",\"displayOption\":\"DISPLAY_TEXT\"}]";
    Mockito
        .when(orgCollectionSettingsRepo.findByCollectionIdAndKey(collectionId,
            Affiliate.Availability.AVAILABILITY_DISPLAY.toLowerName()))
        .thenReturn(Optional.of(OrganisationCollectionsSettings.builder().collectionId(collectionId)
            .settingValue(settingValueStr)
            .settingKey(Affiliate.Availability.AVAILABILITY_DISPLAY.toLowerName()).build()));

    collectionService.updateAvailabilityDisplaySetting(collectionId, Arrays.asList(settingValue));
    verify(orgCollectionSettingsRepo, times(1)).findByCollectionIdAndKey(collectionId,
        Affiliate.Availability.AVAILABILITY_DISPLAY.toLowerName());
    verify(orgCollectionSettingsRepo, times(1)).saveAll(Mockito.any());
  }

}
