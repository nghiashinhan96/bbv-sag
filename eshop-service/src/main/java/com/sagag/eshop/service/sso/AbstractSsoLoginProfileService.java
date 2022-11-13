package com.sagag.eshop.service.sso;

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
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.AadAccountsService;
import com.sagag.eshop.service.api.AxAccountService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileRequestDto;
import com.sagag.services.common.contants.UserSettingConstants;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.security.CompositePasswordEncoder;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class AbstractSsoLoginProfileService {

  private static final String DEFAULT_GENDER = "Male";

  private static final String PERMITED_GROUP_SALES = "SALES";

  @Autowired
  protected AxAccountService axAccountService;

  @Autowired
  protected AadGroupRepository aadGroupRepo;

  @Autowired
  protected AadAccountsRepository aadAccountsRepo;

  @Autowired
  protected AadAccountsService aadAccountsService;

  @Autowired
  private PaymentMethodRepository paymentRepo;

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepo;

  @Autowired
  private UserSettingsRepository userSettingsRepo;

  @Autowired
  private LanguageRepository languageRepo;

  @Autowired
  private SalutationRepository salutationRepo;

  @Autowired
  private LoginRepository loginRepo;

  @Autowired
  private EshopGroupRepository eshopGroupRepo;

  @Autowired
  private GroupUserRepository groupUserRepo;

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Autowired
  protected ExternalUserService externalUserService;

  @Autowired
  private CompositePasswordEncoder passwordEncoder;

  protected void createBrandNewSalesAccount(SsoLoginProfileRequestDto request) {
    AadAccounts newAadAccount = AadAccounts.builder()
        .uuid(StringUtils.isNotBlank(request.getUuid()) ? request.getUuid() : null)
        .primaryContactEmail(request.getEmail()).firstName(request.getFirstName())
        .lastName(request.getSurName()).permitGroup(PERMITED_GROUP_SALES)
        .createdDate(Calendar.getInstance().getTime()).gender(DEFAULT_GENDER).build();
    aadAccountsRepo.save(newAadAccount);
    createFullExternalUser(request);
  }

  protected ExternalUser createFullExternalUser(SsoLoginProfileRequestDto request) {
    UserSettings createdUserSettings = createDefaultUserSettings();
    EshopUser createdUser = createDefaultSalesUser(request, createdUserSettings.getId());
    return createExternalUser(request.getUserName(), createdUser.getId());
  }

  private UserSettings createDefaultUserSettings() {
    final UserSettings userSettings = new UserSettings();
    userSettings.setAllocationId(UserSettingConstants.ALLOCATION_ID_DEFAULT);
    userSettings.setCollectiveDelivery(UserSettingConstants.COLLECTIVE_DELIVERY_ID_DEFAULT);
    userSettings.setDeliveryId(UserSettingConstants.DELIVERY_ID_DEFAULT);
    userSettings.setPaymentMethod(
        paymentRepo.findById(UserSettingConstants.PAYMENT_METHOD_ID_DEFAULT).orElse(null));
    userSettings.setInvoiceType(
        invoiceTypeRepo.findById(UserSettingConstants.INVOICE_TYPE_ID_DEFAULT).orElse(null));
    userSettings.setClassicCategoryView(UserSettingConstants.CLASSIC_CATEGORY_VIEW_DEFAULT);
    userSettings.setSingleSelectMode(UserSettingConstants.SINGLE_SELECT_MODE_DEFAULT);
    return userSettingsRepo.save(userSettings);
  }

  private EshopUser createDefaultSalesUser(SsoLoginProfileRequestDto request, int settingsId) {
    final EshopUser eshopUser = new EshopUser();
    eshopUser.setUsername(request.getUserName());
    eshopUser.setEmail(request.getEmail());
    eshopUser.setFirstName(request.getFirstName());
    eshopUser.setLastName(request.getSurName());
    eshopUser.setPhone(request.getPhoneNumber());
    eshopUser.setSetting(settingsId);
    eshopUser.setLanguage(languageRepo.findById(request.getLanguageId()).orElse(null));
    eshopUser.setSalutation(salutationRepo.findById(request.getSalutationId()).orElse(null));
    eshopUser.setHourlyRate(request.getHourlyRate());
    EshopUser createdUser = eshopUserRepo.save(eshopUser);
    createLogin(createdUser);
    assignSalesRole(createdUser);
    return createdUser;
  }

  private ExternalUser createExternalUser(String username, Long eshopUserId) {
    final ExternalUserDto externalUser =
        ExternalUserDto.builder().eshopUserId(eshopUserId).externalApp(ExternalApp.AX)
            .username(username).password(passwordEncoder.encodeBlockVar(username)).active(true)
            .createdDate(Calendar.getInstance().getTime()).build();
    return externalUserService.addExternalUser(externalUser);
  }

  private void createLogin(EshopUser user) {
    final Login login = new Login();
    login.setUserActive(true);
    login.setEshopUser(user);
    loginRepo.save(login);
  }

  private void assignSalesRole(EshopUser user) {
    Integer id = eshopGroupRepo.findEshopGroupIdByName(EshopAuthority.SALES_ASSISTANT.name())
        .orElseThrow(() -> new NoSuchElementException("Not found eshop group id for sales type"));

    final GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(eshopGroupRepo.findById(id).orElse(null));
    groupUser.setEshopUser(user);
    groupUserRepo.save(groupUser);
  }

  protected void updateUuidForCurrentAadAccount(AadAccounts aadAccounts, String uuid) {
    aadAccounts.setUuid(uuid);
    aadAccountsRepo.save(aadAccounts);
  }

  protected void matchUuidCase(String email, AadAccounts aadAccount) {
    String oldEmail = aadAccount.getPrimaryContactEmail();
    if (!email.equals(oldEmail)) {
      aadAccountsService.updateEmail(aadAccount, email);
    }
  }

  protected Optional<AadAccounts> findAadAccountByUuid(String uuid) {
    return StringUtils.isNoneBlank(uuid) ? aadAccountsRepo.findByUuid(uuid) : Optional.empty();
  }
}
