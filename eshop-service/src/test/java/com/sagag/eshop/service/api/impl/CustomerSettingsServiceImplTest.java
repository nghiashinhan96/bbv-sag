package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.OrganisationPropertyRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationGroup;
import com.sagag.eshop.repo.entity.OrganisationProperty;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.UserService;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.eshop.dto.CustomerSettingsDto;
import com.sagag.services.domain.eshop.dto.OrgPropertyOfferDto;
import com.sagag.services.domain.eshop.dto.PriceDisplaySettingDto;

import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

/**
 * UT to verify for {@link CustomerSettingsServiceImpl}.
 */
@EshopMockitoJUnitRunner
public class CustomerSettingsServiceImplTest {

  @InjectMocks
  private CustomerSettingsServiceImpl customerSettingsService;

  @Mock
  private CustomerSettingsRepository customerSettingsRepo;

  @Mock
  private EshopUserRepository eshopUserRepo;

  @Mock
  private OrganisationPropertyRepository organisationPropertyRepo;

  @Mock
  private UserService userService;

  @Mock
  private DozerBeanMapper dozerBeanMapper;

  @Mock
  private UserSettingsRepository userSettingsRepo;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testUpdateCustomerSetting() throws Exception {

    // Initialize data request
    final boolean netPriceView = true;
    final boolean netPriceConfirm = false;
    final boolean viewBilling = false;

    // Build request parameters
    final long userId = 8;

    final EshopUser eshopUser = buildEshopUser(userId);
    final Organisation org = eshopUser.firstOrganisation().orElse(null);

    final CustomerSettingsDto customerSettingsDto = new CustomerSettingsDto();
    customerSettingsDto.setNetPriceView(netPriceView);
    customerSettingsDto.setNetPriceConfirm(netPriceConfirm);
    customerSettingsDto.setViewBilling(viewBilling);
    customerSettingsDto.setOrgPropertyOffer(new OrgPropertyOfferDto(new ArrayList<>()));
    customerSettingsDto.setPriceDisplaySettings(
        Arrays.asList(PriceDisplaySettingDto.builder().id(1).editable(true).enable(true).build()));

    final int orgId = 5;
    when(userService.getOrgIdByUserId(userId)).thenReturn(Optional.of(orgId));
    when(customerSettingsRepo.findSettingsByOrgId(orgId)).thenReturn(org.getCustomerSettings());

    final List<OrganisationProperty> orgProperties = new ArrayList<>();
    when(organisationPropertyRepo.findByOrganisationId(Mockito.any())).thenReturn(orgProperties);
    when(customerSettingsRepo.save(org.getCustomerSettings()))
        .thenReturn(org.getCustomerSettings());

    UserSettings userSettings = UserSettings.builder().id(1).build();
    when(userSettingsRepo.findActiveUserSettingsByOrgId(5))
        .thenReturn(Optional.of(Arrays.asList(userSettings)));

    customerSettingsService.updateCustomerSetting(userId, customerSettingsDto);

    verify(customerSettingsRepo).save(org.getCustomerSettings());
  }

  @Test(expected = ValidationException.class)
  public void testUpdateCustomerSettingFailed() throws Exception {

    // Initialize data request
    final boolean netPriceView = true;
    final boolean netPriceConfirm = false;
    final boolean viewBilling = false;

    // Build request parameters
    final long userId = 8;

    final CustomerSettingsDto customerSettingsDto = new CustomerSettingsDto();
    customerSettingsDto.setNetPriceView(netPriceView);
    customerSettingsDto.setNetPriceConfirm(netPriceConfirm);
    customerSettingsDto.setViewBilling(viewBilling);

    when(eshopUserRepo.findById(userId)).thenThrow(new ValidationException());

    customerSettingsService.updateCustomerSetting(userId, customerSettingsDto);

    verify(eshopUserRepo).findById(userId);
  }

  @Test
  @Ignore("Please write a gain the UT, not as expected")
  public void testGetCustomerSetting() {
    // Initialize data request
    final boolean netPriceView = true;
    final boolean netPriceConfirm = false;
    final boolean viewBilling = false;

    // Build request parameters
    final long userId = 8;

    final CustomerSettingsDto customerSettingsDto = new CustomerSettingsDto();
    customerSettingsDto.setNetPriceView(netPriceView);
    customerSettingsDto.setNetPriceConfirm(netPriceConfirm);
    customerSettingsDto.setViewBilling(viewBilling);

    final EshopUser eshopUser = buildEshopUser(userId);

    when(eshopUserRepo.findById(userId).orElse(null)).thenReturn(eshopUser);

    customerSettingsService.getCustomerSetting(userId);

    verify(eshopUserRepo).findById(userId);
  }

  @Test(expected = ValidationException.class)
  public void testGetCustomerSettingFailed() throws Exception {
    // Build request parameters
    final long userId = 8;
    when(userService.getOrgIdByUserId(userId)).thenReturn(Optional.<Integer>empty());
    customerSettingsService.getCustomerSetting(userId);
  }

  private EshopUser buildEshopUser(long userId) {
    final EshopUser eshopUser = new EshopUser();
    eshopUser.setId(userId);

    final Organisation firstOrg = new Organisation();
    final OrganisationGroup orgGroup = new OrganisationGroup();
    orgGroup.setOrganisation(firstOrg);

    final EshopGroup eshopGroup = new EshopGroup();
    eshopGroup.setOrganisationGroups(Arrays.asList(orgGroup));

    final GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(eshopGroup);

    eshopUser.setGroupUsers(Arrays.asList(groupUser));

    final CustomerSettings cusSettings = new CustomerSettings();
    firstOrg.setCustomerSettings(cusSettings);
    return eshopUser;
  }

}
