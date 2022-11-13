package com.sagag.eshop.service.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.LicenseRepository;
import com.sagag.eshop.repo.api.LicenseSettingsRepository;
import com.sagag.eshop.repo.api.collection.OrganisationCollectionRepository;
import com.sagag.eshop.repo.entity.License;
import com.sagag.eshop.repo.entity.LicenseSettings;
import com.sagag.eshop.service.dto.HaynesProLicenseSettingDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.LicenseNotFoundException;
import com.sagag.services.common.enums.HaynesProLicenseTypeEnum;
import com.sagag.services.common.enums.LicenseType;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.backoffice.dto.BackOfficeLicenseDto;
import com.sagag.services.domain.eshop.criteria.LicenseSearchCriteria;
import com.sagag.services.domain.eshop.dto.CustomerLicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.domain.sag.external.Customer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.validation.ValidationException;

/**
 * Unit test class for License service.
 */
@RunWith(MockitoJUnitRunner.class)
public class LicenseServiceImplTest {

  private static final String LICENSE_TYPE = "license_type";

  private static final String PACK_NAME = "PACK_NAME";

  private static final String DATE_01_02_2021 = "01.02.2021";

  private static final String DATE_01_01_2021 = "01.01.2021";

  private static final String CUST_NR_5101444 = "5101444";

  @InjectMocks
  private LicenseServiceImpl service;

  @Mock
  private LicenseRepository licenseRepo;

  @Mock
  private UserInfo user;

  @Mock
  private Page<License> page;

  @Mock
  private Customer customer;

  @Mock
  private LicenseSettingsRepository licenseSettingsRepo;

  @Mock
  private OrganisationCollectionRepository orgCollectionRepo;

  @Test
  public void givenCustomerShouldGetVinCalls() {
    final int maxCallsNum = 13;
    when(licenseRepo.findAvailableLicenseCalls(1L, LicenseType.VIN.name())).thenReturn(maxCallsNum);
    int result = service.getVinCallsLeft(1L);
    verify(licenseRepo, Mockito.times(1)).findAvailableLicenseCalls(1L, LicenseType.VIN.name());
    Assert.assertThat(maxCallsNum, Matchers.is(result));
  }

  @Test
  public void testGetLicenseSettingsByPackId_shouldReturnEmpty() {
    Optional<LicenseSettingsDto> licenseSettingsByPackId = service.getLicenseSettingsByPackId(null);
    Assert.assertFalse(licenseSettingsByPackId.isPresent());
  }

  @Test
  public void testGetLicenseSettingsByPackId_shouldReturnLicenseSetting() {
    long packId = new Random().nextLong();
    when(licenseSettingsRepo.findOneByPackId(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(new LicenseSettings()));
    Optional<LicenseSettingsDto> licenseSettingsByPackId =
        service.getLicenseSettingsByPackId(packId);
    Assert.assertTrue(licenseSettingsByPackId.isPresent());
  }

  @Test
  public void testGetLicenseSettingsByArticleId_shouldReturnEmpty() {
    Optional<LicenseSettingsDto> licenseSettingsByPackId =
        service.getLicenseSettingsByArticleId(null);
    Assert.assertFalse(licenseSettingsByPackId.isPresent());
  }

  @Test
  public void testGetLicenseSettingsByArticleId_shouldReturnLicenseSetting() {
    long articleId = new Random().nextLong();
    when(licenseSettingsRepo.findOneByPackArticleId(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(new LicenseSettings()));
    Optional<LicenseSettingsDto> licenseSettingsByPackId =
        service.getLicenseSettingsByArticleId(articleId);
    Assert.assertTrue(licenseSettingsByPackId.isPresent());
  }

  @Test
  public void testGetVinCallsLeft_shouldReturnZero() {
    int vinCallsLeft = service.getVinCallsLeft(null);
    Assert.assertTrue(NumberUtils.INTEGER_ZERO.equals(vinCallsLeft));
  }

  @Test
  public void testCreateVinLicense() {
    LicenseDto licenseDto = new LicenseDto();
    when(licenseRepo.save(Mockito.any(License.class))).thenReturn(new License());
    service.createVinLicense(licenseDto);
    verify(licenseRepo, times(1)).save(Mockito.any(License.class));
  }

  @Test
  public void testGetAllLicensePackage() {
    LicenseSettings licenseSettings = new LicenseSettings();
    when(licenseSettingsRepo.findAll()).thenReturn(Lists.newArrayList(licenseSettings));
    List<LicenseSettingsDto> allLicensePackage = service.getAllLicensePackage();
    Assert.assertTrue(CollectionUtils.isNotEmpty(allLicensePackage));
  }

  @Test(expected = ValidationException.class)
  public void testIncreaseQuantityUsed_shouldReturnValidationException() {
    Long userId = new Random().nextLong();
    String custNr = CUST_NR_5101444;
    LicenseType licenseType = LicenseType.VIN;
    when(licenseRepo.findFirstAvailableLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(Page.empty());
    service.increaseQuantityUsed(userId, custNr, licenseType);
  }

  @Test
  public void testIncreaseQuantityUsed() {
    Long userId = new Random().nextLong();
    String custNr = CUST_NR_5101444;
    LicenseType licenseType = LicenseType.VIN;
    License license = new License();
    Page<License> licenses = new PageImpl<>(Arrays.asList(license));
    when(licenseRepo.findFirstAvailableLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(licenses);
    when(licenseRepo.save(Mockito.any(License.class))).thenReturn(license);
    service.increaseQuantityUsed(userId, custNr, licenseType);
    verify(licenseRepo, times(1)).save(Mockito.any(License.class));
  }

  @Test
  public void testRedeemVinLicense_shouldDoNothing() {
    int searchCount = 1;
    String custNr = CUST_NR_5101444;
    when(licenseRepo.findAllByCustomerNrAndTypeOfLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any())).thenReturn(Lists.newArrayList());
    service.redeemVinLicense(custNr, searchCount);
    verify(licenseRepo, times(1)).findAllByCustomerNrAndTypeOfLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any());
  }

  @Test
  public void testRedeemVinLicense_shouldUpdate() {
    int searchCount = 1;
    String custNr = CUST_NR_5101444;
    License license = new License();
    ArrayList<License> licenses = Lists.newArrayList(license);
    when(licenseRepo.findAllByCustomerNrAndTypeOfLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any())).thenReturn(licenses);
    when(licenseRepo.saveAll(Mockito.any())).thenReturn(licenses);
    service.redeemVinLicense(custNr, searchCount);
    verify(licenseRepo, times(1)).findAllByCustomerNrAndTypeOfLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any());
    verify(licenseRepo, times(1)).saveAll(Mockito.any());
  }

  @Test
  public void testRedeemVinLicense_shouldNotUpdate() {
    int searchCount = 0;
    String custNr = CUST_NR_5101444;
    License license = new License();
    ArrayList<License> licenses = Lists.newArrayList(license);
    when(licenseRepo.findAllByCustomerNrAndTypeOfLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any())).thenReturn(licenses);
    service.redeemVinLicense(custNr, searchCount);
    verify(licenseRepo, times(1)).findAllByCustomerNrAndTypeOfLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any());
  }

  @Test
  public void testRedeemVinLicense_shouldUpdateQuantityUsed() {
    int searchCount = 2;
    String custNr = CUST_NR_5101444;
    License license = new License();
    license.setQuantityUsed(searchCount);
    ArrayList<License> licenses = Lists.newArrayList(license);
    when(licenseRepo.findAllByCustomerNrAndTypeOfLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any())).thenReturn(licenses);
    when(licenseRepo.saveAll(Mockito.any())).thenReturn(licenses);
    service.redeemVinLicense(custNr, searchCount);
    verify(licenseRepo, times(1)).findAllByCustomerNrAndTypeOfLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any());
  }

  @Test
  public void testGetHaynesProLicense_shouldEmpty() {
    Assert.assertFalse(service.getHaynesProLicense(null).isPresent());
  }

  @Test
  public void testGetHaynesProLicense_shouldPackageNameNotFound() {
    License license = new License();
    Page<License> licenses = new PageImpl<>(Arrays.asList(license));

    licenses.stream().findFirst().map(License::getPackName);

    when(licenseRepo.findAvailableHaynesProLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any())).thenReturn(licenses);
    Assert.assertFalse(service.getHaynesProLicense(CUST_NR_5101444).isPresent());
  }

  @Test
  public void testGetHaynesProLicense() {
    License license = new License();
    license.setPackName(HaynesProLicenseTypeEnum.ULTIMATE.getCode());
    Page<License> licenses = new PageImpl<>(Arrays.asList(license));
    when(licenseRepo.findAvailableHaynesProLicense(ArgumentMatchers.anyLong(),
        ArgumentMatchers.any())).thenReturn(licenses);
    Assert.assertTrue(service.getHaynesProLicense(CUST_NR_5101444).isPresent());
  }

  @Test
  public void testGetAllVinLicenses() {
    LicenseSettingsDto licenseSettingsDto = new LicenseSettingsDto();
    ArrayList<LicenseSettingsDto> licenseSettings = Lists.newArrayList(licenseSettingsDto);
    when(licenseSettingsRepo.searchLicenses(ArgumentMatchers.anyString()))
        .thenReturn(licenseSettings);
    List<LicenseSettingsDto> allVinLicenses = service.getAllVinLicenses();
    Assert.assertTrue(CollectionUtils.isNotEmpty(allVinLicenses));
  }

  @Test
  public void testGetAllLicensesOfCustomer_shouldReturnEmpty() {
    Page<BackOfficeLicenseDto> allLicensesOfCustomer =
        service.getAllLicensesOfCustomer(CUST_NR_5101444, null);
    Assert.assertFalse(allLicensesOfCustomer.hasContent());
  }

  @Test
  public void testGetAllLicensesOfCustomer() {
    License license = new License();
    Page<License> licenses = new PageImpl<>(Arrays.asList(license));
    when(licenseRepo.findAllByCustomerNr(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
        .thenReturn(licenses);
    Page<BackOfficeLicenseDto> allLicensesOfCustomer =
        service.getAllLicensesOfCustomer(CUST_NR_5101444, PageUtils.DEF_PAGE);
    Assert.assertTrue(allLicensesOfCustomer.hasContent());
  }

  @Test
  public void testDeleteLicenseForCustomer() {
    Mockito.doNothing().when(licenseRepo).deleteById(ArgumentMatchers.anyLong());
    service.deleteLicenseForCustomer(NumberUtils.toLong(CUST_NR_5101444));
    verify(licenseRepo, times(1)).deleteById(ArgumentMatchers.anyLong());
  }

  @Test(expected = LicenseNotFoundException.class)
  public void testUpdateLicenseForCustomer_shoudlThrowLicenseNotFound()
      throws LicenseNotFoundException {
    Long id = new Random().nextLong();
    CustomerLicenseDto licenseModel = CustomerLicenseDto.builder().beginDate(DATE_01_01_2021)
        .endDate(DATE_01_02_2021).packName(PACK_NAME).build();
    when(licenseSettingsRepo.findOneByPackName(ArgumentMatchers.any()))
        .thenReturn(Optional.empty());
    service.updateLicenseForCustomer(id, licenseModel, user);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateLicenseForCustomer_shoudlThrowIllegalArgumentException()
      throws LicenseNotFoundException {
    Long id = new Random().nextLong();
    CustomerLicenseDto licenseModel = CustomerLicenseDto.builder().beginDate(DATE_01_01_2021)
        .endDate(DATE_01_02_2021).packName(PACK_NAME).build();
    when(licenseSettingsRepo.findOneByPackName(ArgumentMatchers.any()))
        .thenReturn(Optional.of(new LicenseSettings()));
    when(licenseRepo.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
    service.updateLicenseForCustomer(id, licenseModel, user);
  }

  @Test
  public void testUpdateLicenseForCustomer() throws LicenseNotFoundException {
    Long id = new Random().nextLong();
    CustomerLicenseDto licenseModel = CustomerLicenseDto.builder().beginDate(DATE_01_01_2021)
        .endDate(DATE_01_02_2021).packName(PACK_NAME).build();
    when(licenseSettingsRepo.findOneByPackName(ArgumentMatchers.any()))
        .thenReturn(Optional.of(new LicenseSettings()));
    when(licenseRepo.findById(ArgumentMatchers.any())).thenReturn(Optional.of(new License()));
    when(licenseRepo.save(Mockito.any(License.class))).thenReturn(new License());
    service.updateLicenseForCustomer(id, licenseModel, user);
    verify(licenseRepo, times(1)).save(ArgumentMatchers.any(License.class));
  }

  @Test(expected = LicenseNotFoundException.class)
  public void testAssignLicenseToCustomer_shouldThrowLicenseNotFoundException()
      throws LicenseNotFoundException {
    CustomerLicenseDto licenseModel = CustomerLicenseDto.builder().beginDate(DATE_01_01_2021)
        .endDate(DATE_01_02_2021).packName(PACK_NAME).typeOfLicense(LICENSE_TYPE).build();
    when(licenseSettingsRepo.findOneByPackName(ArgumentMatchers.any()))
        .thenReturn(Optional.empty());
    service.assignLicenseToCustomer(licenseModel, user);
    verify(licenseSettingsRepo, times(1)).findOneByPackName(ArgumentMatchers.any());
  }

  @Test
  public void testAssignLicenseToCustomer() throws LicenseNotFoundException {
    CustomerLicenseDto licenseModel = CustomerLicenseDto.builder().beginDate(DATE_01_01_2021)
        .endDate(DATE_01_02_2021).packName(PACK_NAME).typeOfLicense(LICENSE_TYPE).build();
    when(licenseSettingsRepo.findOneByPackName(ArgumentMatchers.any()))
        .thenReturn(Optional.of(new LicenseSettings()));
    service.assignLicenseToCustomer(licenseModel, user);
    verify(licenseRepo, times(1)).save(ArgumentMatchers.any(License.class));
  }

  @Test
  public void givenCustomerShouldGetHaynesProLicense() {
    final License hpPremiumLicense = new License();
    hpPremiumLicense.setPackName("HP-PREMIUM-12");
    when(licenseRepo.findAvailableHaynesProLicense(1L, PageUtils.FIRST_ITEM_PAGE))
        .thenReturn(new PageImpl<>(Lists.newArrayList(hpPremiumLicense)));
    Optional<HaynesProLicenseSettingDto> result = service.getHaynesProLicense("1");
    verify(licenseRepo, Mockito.times(1)).findAvailableHaynesProLicense(1L,
        PageUtils.FIRST_ITEM_PAGE);
    Assert.assertTrue(result.isPresent());
    Assert.assertEquals(HaynesProLicenseTypeEnum.PROFESSIONAL.name(),
        result.get().getLicenseType());
  }

  @Test
  public void shouldSearchWithoutAffiliate() {
    // Given
    final LicenseSearchCriteria criteria = new LicenseSearchCriteria();
    criteria.setPage(0);
    criteria.setSize(10);
    Page<License> expected = Page.empty();
    when(licenseRepo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expected);
    // When
    Page<BackOfficeLicenseDto> result = service.search(criteria);
    // Then
    Assert.assertNotNull(result);
  }

  @Test
  public void shouldSearchWithAffiliate() {
    // Given
    final LicenseSearchCriteria criteria = new LicenseSearchCriteria();
    criteria.setPage(0);
    criteria.setSize(10);
    criteria.setAffiliate("derendinger-ch");
    List<License> expected = new ArrayList<>();
    when(orgCollectionRepo.findCustomersByCollectionShortName(anyString())).thenReturn(Collections.emptyList());
    when(licenseRepo.findAll(any(Specification.class))).thenReturn(expected);
    // When
    Page<BackOfficeLicenseDto> result = service.search(criteria);
    // Then
    Assert.assertNotNull(result);
  }
}
