package com.sagag.services.admin.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.ExternalOrganisationRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.VUserExportRepository;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationGroup;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.repo.entity.VUserExport;
import com.sagag.eshop.repo.specification.VUserExportSpecification;
import com.sagag.eshop.service.api.DeliveryTypeAdditionalService;
import com.sagag.eshop.service.api.ExternalOrganisationService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.LanguageService;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.RoleService;
import com.sagag.eshop.service.api.SalutationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.admin.controller.AbstractControllerTest;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.domain.eshop.backoffice.dto.ExportingUserDto;
import com.sagag.services.domain.eshop.criteria.UserExportRequest;
import com.sagag.services.domain.eshop.dto.BackOfficeUserDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BackOfficeServiceImplTest extends AbstractControllerTest {

  @InjectMocks
  private BackOfficeServiceImpl backOfficeServiceImpl;

  @Mock
  private EshopUserRepository eshopUserRepo;

  @Mock
  private UserService userService;

  @Mock
  private LoginService loginService;

  @Mock
  private VUserExportRepository vUserExportRepo;

  @Mock
  private LanguageService languageService;

  @Mock
  private SalutationService salutationService;

  @Mock
  private UserSettingsService settingsService;

  @Mock
  private RoleService roleService;

  @Mock
  private OrganisationRepository organisationRepo;

  @Mock
  private ExternalUserService externalUserService;

  @Mock
  private ExternalOrganisationService externalOrganisationService;

  @Mock
  private ExternalOrganisationRepository extOrgRepo;

  @Mock
  private DeliveryTypeAdditionalService deliveryTypeAdditionalHandler;

  @Test
  public void testGetExportingUsers() {
    List<VUserExport> users = new ArrayList<>();
    users.add(new VUserExport());
    when(vUserExportRepo.findAll(any(VUserExportSpecification.class))).thenReturn(users);
    List<ExportingUserDto> result =
        backOfficeServiceImpl.getExportingUsers(UserExportRequest.builder().build());
    assertThat(result, Matchers.hasSize(1));
  }

  @Test
  public void testGetBackOfficeSetting() throws UserValidationException {
    List<VUserExport> users = new ArrayList<>();
    users.add(new VUserExport());
    EshopUser user = new EshopUser(1L);
    Organisation org = new Organisation("Derendinger-Switzerland", "200000", "derendinger-ch");
    OrganisationGroup organisationGroup = new OrganisationGroup();
    EshopGroup group = new EshopGroup();
    GroupUser grpUser = new GroupUser();
    GroupRole grpRole = new GroupRole();
    grpRole.setEshopRole(new EshopRole());

    organisationGroup.setOrganisation(org);
    group.setOrganisationGroups(Arrays.asList(organisationGroup));
    grpUser.setEshopGroup(group);
    group.setGroupRoles(Arrays.asList(grpRole));
    user.setGroupUsers(Arrays.asList(grpUser));

    UserSettings settings = new UserSettings();
    PaymentMethod paymentMethod = new PaymentMethod();
    InvoiceType invoiceType = new InvoiceType();
    invoiceType.setId(1);
    paymentMethod.setId(1);
    settings.setClassicCategoryView(true);
    settings.setSingleSelectMode(true);
    settings.setPaymentMethod(paymentMethod);
    settings.setInvoiceType(invoiceType);
    user.setUserSetting(settings);
    user.setSalutation(new Salutation());
    user.setLanguage(new Language());
    user.setType("ON_BEHALF_ADMIN");

    PaymentSettingDto payment = new PaymentSettingDto();

    Login login = new Login();
    when(eshopUserRepo.findById(1L)).thenReturn(Optional.of(user));
    when(userService.getPaymentSetting(false, false)).thenReturn(payment);
    when(loginService.getLoginForUser(1L)).thenReturn(login);
    when(languageService.getAllLanguage()).thenReturn(Collections.emptyList());
    when(salutationService.getProfileSalutations()).thenReturn(Collections.emptyList());
    when(settingsService.syncShowHappyPointWithCustomer(any(), any(), any())).thenReturn(settings);
    when(roleService.getAllRoleDto()).thenReturn(Collections.emptyList());
    when(organisationRepo.findAffiliateByOrgId(1)).thenReturn(StringUtils.EMPTY);
    when(externalUserService.getDvseExternalUserByUserId(1L)).thenReturn(Optional.empty());
    when(roleService.findRoleByName(any())).thenReturn(Optional.empty());
    when(extOrgRepo.findExternalCustomerNameByOrgIdAndExternalApp(1, ExternalApp.DVSE))
        .thenReturn(null);

    BackOfficeUserDto result = backOfficeServiceImpl.getBackOfficeSetting(1L);
    assertNotNull(result);
    assertEquals("ON_BEHALF_ADMIN", result.getType());
  }


}
