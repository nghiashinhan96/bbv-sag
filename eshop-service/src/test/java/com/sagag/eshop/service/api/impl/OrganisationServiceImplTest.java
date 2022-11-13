package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.AddressTypeRepository;
import com.sagag.eshop.repo.api.EshopAddressRepository;
import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopPermissionRepository;
import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.api.GroupRoleRepository;
import com.sagag.eshop.repo.api.OrganisationAddressRepository;
import com.sagag.eshop.repo.api.OrganisationGroupRepository;
import com.sagag.eshop.repo.api.OrganisationPropertyRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.OrganisationTypeRepository;
import com.sagag.eshop.repo.entity.AddressType;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopAddress;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationAddress;
import com.sagag.eshop.repo.entity.OrganisationProperty;
import com.sagag.eshop.repo.entity.OrganisationType;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.tests.utils.OrganisationAssertions;
import com.sagag.eshop.service.tests.utils.TestsConstants;
import com.sagag.eshop.service.tests.utils.TestsDataProvider;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.offer.OrganisationPropertyType;
import com.sagag.services.domain.eshop.backoffice.dto.CustomerSearchResultItemDto;
import com.sagag.services.domain.eshop.common.EshopUserCreateAuthority;
import com.sagag.services.domain.eshop.dto.OrgPropertyOfferDto;
import com.sagag.services.domain.sag.erp.Address;

import org.apache.commons.lang3.StringUtils;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class OrganisationServiceImplTest {

  @InjectMocks
  private OrganisationServiceImpl orgService;

  @Mock
  private OrganisationRepository orgRepo;

  @Mock
  private OrganisationPropertyRepository orgPropertyRepo;

  @Mock
  private UserInfo userInfo;

  @Mock
  private OrganisationTypeRepository orgTypeRepo;

  @Mock
  private EshopGroupRepository groupRepo;

  @Mock
  private PermissionService permissionService;

  @Mock
  private EshopRoleRepository roleRepo;

  @Mock
  private GroupRoleRepository groupRoleRepo;

  @Mock
  private OrganisationGroupRepository orgGroupRepo;

  @Mock
  private EshopAddressRepository addressRepo;

  @Mock
  private OrganisationAddressRepository orgAddressRepo;

  @Mock
  private AddressTypeRepository addressTypeRepo;

  @Mock
  private OrganisationCollectionService orgCollectionService;

  @Mock
  private EshopPermissionRepository eshopPermissionRepo;

  @Test
  public void givenOrgCodeShouldGetOne() {
    final Organisation org = TestsDataProvider.buildOrganisation();
    final String orgCode = TestsConstants.ORG_CODE;
    when(orgRepo.findOneByOrgCode(orgCode)).thenReturn(Optional.of(org));
    final Optional<Organisation> foundOrgOpt = orgService.findOrganisationByOrgCode(orgCode);
    verify(orgRepo, times(1)).findOneByOrgCode(orgCode);
    // assert found organisation
    assertThat(foundOrgOpt.isPresent(), Matchers.is(true));
    OrganisationAssertions.assertFoundOrganisation(foundOrgOpt.get());
  }

  @Test
  public void givenUserIdShouldGetFirstOne() {
    final Organisation org = TestsDataProvider.buildOrganisation();
    final long userId = Long.valueOf(TestsConstants.USER_ID);
    when(orgRepo.findAllByUserId(userId)).thenReturn(Arrays.asList(org));
    final Optional<Organisation> foundOrgOpt = orgService.getFirstByUserId(userId);
    verify(orgRepo, times(1)).findAllByUserId(userId);
    // assert found organisation
    assertThat(foundOrgOpt.isPresent(), Matchers.is(true));
    OrganisationAssertions.assertFoundOrganisation(foundOrgOpt.get());
  }

  @Test
  public void givenUserIdShouldGetEmpty() {
    final long userId = Long.valueOf(TestsConstants.USER_ID);
    when(orgRepo.findAllByUserId(userId)).thenReturn(Collections.emptyList());
    final Optional<Organisation> foundOrgOpt = orgService.getFirstByUserId(userId);
    verify(orgRepo, times(1)).findAllByUserId(userId);
    assertThat(foundOrgOpt.isPresent(), Matchers.is(false));
  }

  @Test
  public void givenOrgIdShouldGetOne() {
    final int orgId = TestsConstants.ORG_ID;
    when(orgRepo.findByOrgId(orgId)).thenReturn(Optional.of(TestsDataProvider.buildOrganisation()));
    final Optional<Organisation> foundOrgOpt = orgService.getByOrgId(orgId);
    verify(orgRepo, times(1)).findByOrgId(orgId);
    assertThat(foundOrgOpt.isPresent(), Matchers.is(true));
    OrganisationAssertions.assertFoundOrganisation(foundOrgOpt.get());
  }

  @Test
  public void givenOrgTypeNameShouldGetOne() {
    final String orgTypeName = TestsConstants.ORG_TYPE_NAME;
    when(orgRepo.findOrganisationsByType(orgTypeName))
        .thenReturn(Arrays.asList(TestsDataProvider.buildOrganisation()));
    final List<Organisation> foundOrgs = orgService.findOrganisationByTypeName(orgTypeName);
    verify(orgRepo, times(1)).findOrganisationsByType(orgTypeName);
    assertThat(foundOrgs, Matchers.hasSize(1));
    OrganisationAssertions.assertFoundOrganisation(foundOrgs.get(0));
  }

  @Test
  public void givenOrgTypeNameShouldGetEmpty() {
    final String orgTypeName = StringUtils.EMPTY;
    final List<Organisation> foundOrgs = orgService.findOrganisationByTypeName(orgTypeName);
    verify(orgRepo, times(0)).findOrganisationsByType(orgTypeName);
    assertThat(foundOrgs, Matchers.hasSize(0));
  }

  @Test
  public void givenShortNameShouldGetCompanyName() {
    final String shortName = TestsConstants.ORG_SHORTNAME;
    final String companyName = TestsConstants.ORG_NAME;
    when(orgRepo.findCompanyNameByShortname(shortName)).thenReturn(Optional.of(companyName));
    final Optional<String> companyNameOpt = orgService.searchCompanyName(shortName);
    verify(orgRepo, times(1)).findCompanyNameByShortname(shortName);
    assertThat(companyNameOpt.isPresent(), Matchers.is(true));
    assertThat(companyNameOpt.get(), Matchers.is(companyName));
  }

  @Test
  public void givenShortNameShouldGetEmptyCompanyName() {
    final String shortName = StringUtils.EMPTY;
    final Optional<String> companyNameOpt = orgService.searchCompanyName(shortName);
    verify(orgRepo, times(0)).findCompanyNameByShortname(shortName);
    assertThat(companyNameOpt.isPresent(), Matchers.is(false));
  }

  @Test
  public void testSearchCustomerProfile() {
    Organisation org = new Organisation();
    Page<Organisation> orgs = new PageImpl<Organisation>(Lists.newArrayList(org));
    when(orgRepo.findByNameAndOrgCodeAndAffiliate(ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(orgs);
    Page<CustomerSearchResultItemDto> customerProfiles = orgService.searchCustomerProfile(null);
    Assert.assertTrue(customerProfiles.hasContent());
  }

  @Test
  public void testGetFirstByUserId_shouldReturnEmpty() {
    Optional<Organisation> orgOpt = orgService.getFirstByUserId(null);
    Assert.assertFalse(orgOpt.isPresent());
  }

  @Test
  public void testFindOrganisationPropertiesById() {
    final Long id = new Random().nextLong();
    final Long orgId = new Random().nextLong();
    OrganisationProperty orgProp = OrganisationProperty.builder().id(id).organisationId(orgId)
        .type(OrganisationPropertyType.OFFER_FOOTER_TEXT.name()).value("value").build();
    List<OrganisationProperty> properties = Lists.newArrayList(orgProp);
    when(orgPropertyRepo.findByOrganisationId(ArgumentMatchers.anyLong())).thenReturn(properties);
    OrgPropertyOfferDto orgPropOffer = orgService.findOrganisationPropertiesById(id);
    Assert.assertNotNull(orgPropOffer);
  }

  @Test
  public void testCreateCustomer() {
    int affiliateId = new Random().nextInt();
    CustomerSettings custSettings = new CustomerSettings();
    OrganisationType organisationType = new OrganisationType();
    when(orgTypeRepo.getCustomerOrgType()).thenReturn(organisationType);
    when(orgRepo.findIdByShortName(ArgumentMatchers.any())).thenReturn(Optional.of(affiliateId));
    when(orgRepo.save(ArgumentMatchers.any(Organisation.class))).thenReturn(new Organisation());
    Organisation org = orgService.createCustomer(SupportedAffiliate.DERENDINGER_AT, "1000",
        "Derendinger-Switzerland", custSettings);
    Assert.assertNotNull(org);
  }

  @Test
  public void testAssignCustomerGroupAndDefaultPermission_shouldDoNothing() {
    Organisation customer = new Organisation();
    List<PermissionEnum> defaultPermission = Lists.newArrayList(PermissionEnum.HOME);
    List<EshopGroup> savedUserGroups = Lists.newArrayList(new EshopGroup());
    when(groupRepo.saveAll(ArgumentMatchers.any())).thenReturn(savedUserGroups);
    Mockito.doNothing().when(permissionService).setPermissions(ArgumentMatchers.any(),
        ArgumentMatchers.any(), ArgumentMatchers.anyBoolean());
    when(roleRepo.findEshopRolesByName(ArgumentMatchers.any())).thenReturn(Lists.newArrayList());

    orgService.assignCustomerGroupAndDefaultPermission(customer, defaultPermission);
    verify(roleRepo, times(1)).findEshopRolesByName(ArgumentMatchers.any());
  }

  @Test
  public void testAssignCustomerGroupAndDefaultPermission() {
    Organisation customer = new Organisation();
    List<PermissionEnum> defaultPermission = Lists.newArrayList(PermissionEnum.HOME);
    List<EshopGroup> savedUserGroups = Lists.newArrayList(new EshopGroup());
    EshopRole eshopRole = new EshopRole();
    List<EshopRole> eshopRoles = Lists.newArrayList(eshopRole);
    eshopRole.setName(EshopUserCreateAuthority.USER_ADMIN.name());
    List<GroupRole> eshopGroupRoles = Lists.newArrayList(new GroupRole());
    when(groupRepo.saveAll(ArgumentMatchers.any())).thenReturn(savedUserGroups);
    Mockito.doNothing().when(permissionService).setPermissions(ArgumentMatchers.any(),
        ArgumentMatchers.any(), ArgumentMatchers.anyBoolean());
    when(roleRepo.findEshopRolesByName(ArgumentMatchers.any())).thenReturn(eshopRoles);
    when(groupRoleRepo.saveAll(ArgumentMatchers.any())).thenReturn(eshopGroupRoles);
    when(orgGroupRepo.saveAll(ArgumentMatchers.any())).thenReturn(Lists.newArrayList());

    orgService.assignCustomerGroupAndDefaultPermission(customer, defaultPermission);
    verify(orgGroupRepo, times(1)).saveAll(ArgumentMatchers.any());
  }

  @Test
  public void testCreateCustomerAddresses() {
    Organisation customer = new Organisation();
    List<Address> addresses = Lists.newArrayList(new Address());
    List<EshopAddress> eshopAddresses = Lists.newArrayList(new EshopAddress());
    List<OrganisationAddress> result = Lists.newArrayList(new OrganisationAddress());

    when(addressTypeRepo.findAddressByType(ArgumentMatchers.any()))
        .thenReturn(Optional.of(new AddressType()));
    when(addressRepo.saveAll(ArgumentMatchers.any())).thenReturn(eshopAddresses);
    when(orgAddressRepo.saveAll(ArgumentMatchers.any())).thenReturn(result);

    orgService.createCustomerAddresses(customer, addresses);
    verify(orgGroupRepo, times(0)).saveAll(ArgumentMatchers.any());
  }

  @Test
  public void testFindAffiliateByOrgId() {
    when(orgRepo.findAffiliateByOrgId(ArgumentMatchers.anyInt())).thenReturn(StringUtils.EMPTY);

    int orgId = new Random().nextInt();
    orgService.findAffiliateByOrgId(orgId);
    verify(orgRepo, times(1)).findAffiliateByOrgId(ArgumentMatchers.anyInt());
  }

  @Test
  public void testFindOrgCodeById() {
    when(orgRepo.findOrgCodeById(ArgumentMatchers.anyInt())).thenReturn(StringUtils.EMPTY);

    int orgId = new Random().nextInt();
    orgService.findOrgCodeById(orgId);
    verify(orgRepo, times(1)).findOrgCodeById(ArgumentMatchers.anyInt());
  }

  @Test
  public void testFindOrgSettingsByShortName() {
    when(orgCollectionService.findSettingsByCollectionShortname(ArgumentMatchers.anyString()))
        .thenReturn(null);

    orgService.findOrgSettingsByShortName(StringUtils.EMPTY);
    verify(orgCollectionService, times(1))
        .findSettingsByCollectionShortname(ArgumentMatchers.anyString());
  }

  @Test
  public void testFindOrgSettingByKey() {
    when(orgCollectionService.findSettingValueByCollectionShortnameAndKey(ArgumentMatchers.any(),
        ArgumentMatchers.anyString())).thenReturn(Optional.of("org-setting"));

    orgService.findOrgSettingByKey("shortname", "key");
    verify(orgCollectionService, times(1)).findSettingValueByCollectionShortnameAndKey(
        ArgumentMatchers.any(), ArgumentMatchers.anyString());
  }

  @Test
  public void testFindIdByShortName() {
    int id = new Random().nextInt();
    when(orgRepo.findIdByShortName(ArgumentMatchers.anyString())).thenReturn(Optional.of(id));

    orgService.findIdByShortName("affiliateShortName");
    verify(orgRepo, times(1)).findIdByShortName(ArgumentMatchers.anyString());
  }

  @Test
  public void testIsWholesalerCustomer() {
    int orgId = new Random().nextInt();
    when(eshopPermissionRepo.findPermissionIdByName(ArgumentMatchers.anyString()))
        .thenReturn(Optional.of(orgId));

    orgService.isWholesalerCustomer(orgId);
    verify(eshopPermissionRepo, times(1)).findPermissionIdByName(ArgumentMatchers.anyString());
  }

}
