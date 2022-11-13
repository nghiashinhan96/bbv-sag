package com.sagag.eshop.service.api.sso;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.AadAccountsRepository;
import com.sagag.eshop.repo.api.AadGroupRepository;
import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.GroupUserRepository;
import com.sagag.eshop.repo.api.InvoiceTypeRepository;
import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.api.PaymentMethodRepository;
import com.sagag.eshop.repo.api.SalutationRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.repo.entity.AadGroup;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.AadAccountsService;
import com.sagag.eshop.service.api.AxAccountService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileRequestDto;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractSsoLoginProfileServiceTest {

  protected static final String EMAIL = "kaka@bbv.ch";

  protected static final String USERNAME = "kaka@bbv.ch";

  protected static final String GROUP_UUID = "366a1f4a-380a-43c7-b3fe-322edb5cf491";

  protected static final String UUID = "effbd2a4-3e31-4090-8b9b-18e64d1b49dc";

  protected static final List<String> GROUP_UUIDS = new ArrayList<>(Arrays.asList(GROUP_UUID));

  @Mock
  protected AxAccountService axAccountService;

  @Mock
  protected AadAccountsService aadAccountsService;

  @Mock
  protected AadAccountsRepository aadAccountsRepo;

  @Mock
  protected AadGroupRepository aadGroupRepo;

  @Mock
  protected LanguageRepository languageRepo;

  @Mock
  protected SalutationRepository salutationRepo;

  @Mock
  protected LoginRepository loginRepo;

  @Mock
  protected EshopGroupRepository eshopGroupRepo;

  @Mock
  protected GroupUserRepository groupUserRepo;

  @Mock
  protected PaymentMethodRepository paymentRepo;

  @Mock
  protected InvoiceTypeRepository invoiceTypeRepo;

  @Mock
  protected UserSettingsRepository userSettingsRepo;

  @Mock
  protected EshopUserRepository eshopUserRepo;

  @Mock
  protected ExternalUserService externalUserService;

  protected AadAccounts mockAadAccount() {
    return AadAccounts.builder().id(1).firstName("Test 01").lastName("AX")
        .primaryContactEmail(EMAIL).permitGroup("SALES").uuid(UUID).build();
  }

  protected AadGroup aadGroup() {
    return AadGroup.builder().uuid(GROUP_UUID).name("AxTestUserGroup").id(1).build();
  }

  protected SsoLoginProfileRequestDto buildRequest() {
    return SsoLoginProfileRequestDto.builder().email(EMAIL).userName(EMAIL).uuid(UUID)
        .groupUuids(GROUP_UUIDS).firstName("Test 01").surName("AX").salutationId(1)
        .phoneNumber("123456789").languageId(1).hourlyRate(11.0).build();
  }

  protected void mockCreateUserSetting() {
    UserSettings userSettings = UserSettings.builder().id(1).build();
    when(userSettingsRepo.save(Mockito.any(UserSettings.class))).thenReturn(userSettings);
  }

  protected void mockCreateEshopUser() {
    when(languageRepo.findById(1)).thenReturn(Optional.of(new Language()));
    when(salutationRepo.findById(1)).thenReturn(Optional.ofNullable(new Salutation()));
    EshopUser eshopUser = new EshopUser();
    eshopUser.setId(1);
    when(eshopUserRepo.save(Mockito.any(EshopUser.class))).thenReturn(eshopUser);
  }

  protected void mockSssignSalesRole() {
    when(eshopGroupRepo.findEshopGroupIdByName(EshopAuthority.SALES_ASSISTANT.name()))
        .thenReturn(Optional.of(1));
    when(eshopGroupRepo.findById(1)).thenReturn(Optional.of(new EshopGroup()));
  }

  protected void verifyExternalUserCreatedSuccessfully() {
    verify(userSettingsRepo, times(1)).save(Mockito.any(UserSettings.class));
    verify(eshopUserRepo, times(1)).save(Mockito.any(EshopUser.class));
    verify(groupUserRepo, times(1)).save(Mockito.any(GroupUser.class));
    verify(externalUserService, times(1)).addExternalUser(Mockito.any(ExternalUserDto.class));
  }
}
