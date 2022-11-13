package com.sagag.services.admin.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.EshopAddressRepository;
import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.ExternalUserRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.eshop.repo.entity.RoleType;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.impl.ExternalUserServiceImpl;
import com.sagag.eshop.service.api.impl.GroupUserServiceImpl;
import com.sagag.eshop.service.api.impl.LanguageServiceIml;
import com.sagag.eshop.service.api.impl.PaymentMethodServiceImpl;
import com.sagag.eshop.service.api.impl.RoleServiceImpl;
import com.sagag.eshop.service.api.impl.SalutationServiceImpl;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.admin.controller.AbstractControllerTest;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.eshop.dto.BackOfficeUserDto;
import com.sagag.services.domain.eshop.dto.BackOfficeUserSettingDto;
import com.sagag.services.domain.eshop.dto.EshopRoleDto;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;
import com.sagag.services.domain.eshop.dto.LanguageDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.SalutationDto;
import com.sagag.services.service.api.impl.UserBusinessServiceImpl;

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

@EshopMockitoJUnitRunner
@Ignore("all cases failed")
public class BackOfficeBusinessServiceImplTest extends AbstractControllerTest {

  @InjectMocks
  private BackOfficeServiceImpl backOfficeService;

  @Mock
  private EshopUserRepository eshopUserRepo;

  @Mock
  private UserService userService;

  @Mock
  private PaymentSettingDto paymentSettingDto;

  @Mock
  private LanguageServiceIml languageService;

  @Mock
  private SalutationServiceImpl salutationService;

  @Mock
  private EshopUser eshopOpt;

  @Mock
  private Organisation organisation;

  @Mock
  private List<LanguageDto> languageDtos;

  @Mock
  private List<SalutationDto> salutationDtos;

  @Mock
  private PaymentMethod paymentMethod;

  @Mock
  private InvoiceType invoiceType;

  @Mock
  private RoleServiceImpl roleService;

  @Mock
  private com.sagag.eshop.repo.entity.UserSettings userSettings;

  @Mock
  private Salutation salutation;

  @Mock
  private Language language;

  @Mock
  private List<EshopRoleDto> rolesDtos;

  @Mock
  private RoleType roleType;

  @Mock
  private BackOfficeUserSettingDto backOfficeUserSettingDto;

  @Mock
  private EshopRole eshopRole;

  @Mock
  private UserBusinessServiceImpl userBusinessService;

  @Mock
  private PaymentMethodServiceImpl paymentMethodService;

  @Mock
  private EshopGroupRepository eshopGroupRepo;

  @Mock
  private EshopGroup eshopGroup;

  @Mock
  private GroupUserServiceImpl groupUserService;

  @Mock
  private GroupUser groupUser;

  @Mock
  private ExternalUserServiceImpl expternalUserService;

  @Mock
  private OrganisationRepository organisationRepository;

  @Mock
  private EshopAddressRepository addressRepository;

  @Mock
  private ExternalUserRepository externalUserRepository;

  @Test
  public void testGetBackOfficeSetting() throws Exception {
    MockitoAnnotations.initMocks(this);
    when(userService.getPaymentSetting(false, false)).thenReturn(paymentSettingDto);
    when(eshopUserRepo.findById(13L)).thenReturn(Optional.of(eshopOpt));
    when(languageService.getAllLanguage()).thenReturn(languageDtos);
    when(salutationService.getProfileSalutations()).thenReturn(salutationDtos);
    when(eshopOpt.firstOrganisation()).thenReturn(Optional.of(organisation));
    when(eshopOpt.getUserSetting()).thenReturn(userSettings);
    when(userSettings.getPaymentMethod()).thenReturn(paymentMethod);
    when(userSettings.getInvoiceType()).thenReturn(invoiceType);
    when(eshopOpt.getRoles()).thenReturn(new ArrayList<String>(Arrays.asList("1", "12")));
    when(roleService.findRoleByName(Mockito.anyString())).thenReturn(Optional.of(eshopRole));
    when(eshopOpt.getSalutation()).thenReturn(salutation);
    when(eshopOpt.getLanguage()).thenReturn(language);
    when(eshopOpt.getRolesType()).thenReturn(Optional.of(roleType));
    when(expternalUserService.getDvseExternalUserByUserId(13L)).thenReturn(
        Optional.of(ExternalUserDto.builder().id(13L).active(true).username("ex_user").build()));

    BackOfficeUserDto result = backOfficeService.getBackOfficeSetting(13);
    assertNotNull(result);
    assertEquals("ex_user", result.getExternalUserName());
    verify(userService, times(1)).getPaymentSetting(false, false);
    verify(eshopUserRepo, times(1)).findById(13L);
    verify(languageService, times(1)).getAllLanguage();
    verify(salutationService, times(1)).getProfileSalutations();
    verify(eshopOpt, times(1)).firstOrganisation();
  }

  @Test
  public void testSaveUserSetting() throws UserValidationException {
    when(eshopUserRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(eshopOpt));
    when(salutationService.getById(Mockito.anyInt())).thenReturn(salutation);
    when(languageService.getOneById(Mockito.anyInt())).thenReturn(language);
    when(backOfficeUserSettingDto.getEmailNotificationOrder()).thenReturn(true);
    when(eshopOpt.getUserSetting()).thenReturn(userSettings);
    when(paymentMethodService.getPaymentMethodById(Mockito.anyInt()))
        .thenReturn(Optional.of(paymentMethod));
    when(eshopOpt.firstOrganisation()).thenReturn(Optional.of(organisation));
    when(eshopGroupRepo.findOneByOrgCodeAndRoleId(Mockito.anyString(), Mockito.anyInt()))
        .thenReturn(Optional.of(eshopGroup));
    when(groupUserService.findOneByEshopUser(eshopOpt)).thenReturn(groupUser);
    backOfficeService.saveUserSetting(backOfficeUserSettingDto);

    verify(eshopUserRepo, times(1)).findById(Mockito.anyLong());
    verify(salutationService, times(1)).getById(Mockito.anyInt());
    verify(languageService, times(1)).getOneById(Mockito.anyInt());
    verify(paymentMethodService, times(1)).getPaymentMethodById(Mockito.anyInt());
    verify(eshopGroupRepo, times(1)).findOneByOrgCodeAndRoleId(Mockito.anyString(),
        Mockito.anyInt());
    verify(groupUserService, times(1)).saveGroupUser(groupUser);
    verify(userService, times(1)).saveEshopUser(eshopOpt);
  }
}
