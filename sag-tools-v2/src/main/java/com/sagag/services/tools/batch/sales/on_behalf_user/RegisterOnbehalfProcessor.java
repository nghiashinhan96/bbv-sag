package com.sagag.services.tools.batch.sales.on_behalf_user;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.domain.target.EshopGroup;
import com.sagag.services.tools.domain.target.EshopRole;
import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.GroupUser;
import com.sagag.services.tools.domain.target.InvoiceType;
import com.sagag.services.tools.domain.target.Language;
import com.sagag.services.tools.domain.target.Login;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.domain.target.PasswordHash;
import com.sagag.services.tools.domain.target.Salutation;
import com.sagag.services.tools.domain.target.UserProfileDto;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.repository.target.EshopGroupRepository;
import com.sagag.services.tools.repository.target.EshopRoleRepository;
import com.sagag.services.tools.repository.target.EshopUserRepository;
import com.sagag.services.tools.repository.target.GroupUserRepository;
import com.sagag.services.tools.repository.target.InvoiceTypeRepository;
import com.sagag.services.tools.repository.target.LanguageRepository;
import com.sagag.services.tools.repository.target.LoginRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.repository.target.SalutationRepository;
import com.sagag.services.tools.repository.target.UserSettingsRepository;
import com.sagag.services.tools.service.OrganisationService;
import com.sagag.services.tools.support.ErpInvoiceTypeCode;
import com.sagag.services.tools.support.EshopUserCreateAuthority;
import com.sagag.services.tools.support.HashType;
import com.sagag.services.tools.support.SupportedAffiliate;
import com.sagag.services.tools.support.UserType;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.UserSettingConstants;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@StepScope
@OracleProfile
public class RegisterOnbehalfProcessor implements ItemProcessor<Organisation, Organisation> {

  private static final String DEFAULT_LANG = "DE";

  // #3270: Sales for AX: Default e-mail address and phone number for virtual users
  private static final String DEFAULT_EMAIL = "noReply@sag-austria.at";

  // Default password: agenda01
  private static final String ENCRYPTED_DEFAULT_PASSWORD = "FdcFONWLNYYKY";

  private static final String SALUTATION_OTHER = "SALUTATION_OTHER";

  private static final String NOT_CREATED_VIRTUAL_USER_CSV = SystemUtils.getUserDir() + "/csv/customer_not_created.csv";

  private List<String> custNrCanNotCreated = new ArrayList<>();

  @Autowired
  private OrganisationRepository organisationRepository;

  @Autowired
  private UserSettingsRepository userSettingsRepository;

  @Autowired
  private EshopUserRepository eshopUserRepository;

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private GroupUserRepository groupUserRepo;

  @Autowired
  private LanguageRepository languageRepository;

  @Autowired
  private SalutationRepository salutationRepo;

  @Autowired
  private EshopGroupRepository eshopGroupRepo;

  @Autowired
  private EshopRoleRepository eshopRoleRepo;

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepo;

  @Override
  public Organisation process(final Organisation organisation) throws Exception {
    final String customerNr = organisation.getOrgCode();

    if (StringUtils.isBlank(customerNr)) {
      throw new Exception("Customer number is not valid: " + customerNr);
    }

    final Optional<Organisation> affOrgOpt = organisationRepository.findOneById(organisation.getParentId());

    if (!affOrgOpt.isPresent()) {
      custNrCanNotCreated.add(customerNr);
      return null;
    }
    final String username = UserProfileDto.ON_BEHALF_AGENT_PREFIX + customerNr;
    final List<EshopUser> users = eshopUserRepository.findUsersByUsername(username);
    if (!CollectionUtils.isEmpty(users)) {
      return null;
    }

    final CustomerSettings customerSettings = buildCustomerSettings(organisation);
    final UserProfileDto userProfileDto = buildUserProfileDto(organisation, affOrgOpt.get(), customerSettings);
    if (!Objects.isNull(userProfileDto)) {
      createAdminUser(userProfileDto, organisation, affOrgOpt.get());
    }
    CsvUtils.write(NOT_CREATED_VIRTUAL_USER_CSV, custNrCanNotCreated);
    return organisation;
  }

  protected UserProfileDto buildUserProfileDto(final Organisation org, final Organisation affOrg, final CustomerSettings customerSettings) {
    final UserProfileDto userProfileDto = new UserProfileDto();

    final String customerNr = org.getOrgCode();

    userProfileDto.setUserName(UserProfileDto.ON_BEHALF_AGENT_PREFIX + customerNr);
    userProfileDto.setFirstName(customerNr);
    userProfileDto.setSurName(UserProfileDto.ON_BEHALF_AGENT_SURNAME);
    userProfileDto.setEmail(DEFAULT_EMAIL);
    userProfileDto.setUserType(UserType.ON_BEHALF_ADMIN.name());

    return userProfileDto;
  }

  private EshopUser createAdminUser(UserProfileDto userProfile, Organisation currOrg, Organisation affiOrg) throws Exception {

    validateUserProfile(userProfile, currOrg, affiOrg);

    final CustomerSettings customerSettings = buildCustomerSettings(currOrg);

    final UserSettings userSettings = buildUserSettings(customerSettings, affiOrg);

    userSettings.setDeliveryType(customerSettings.getDeliveryType());
    final UserSettings newUserSettings = userSettingsRepository.save(userSettings);

    final EshopUser eshopUser = buildEshopUser(userProfile, newUserSettings);
    final EshopUser newEshopUser = eshopUserRepository.save(eshopUser);

    final Login login = buildEshopLogin(newEshopUser, affiOrg);
    loginRepository.save(login);

    final EshopGroup adminGroup = getAdminGroup(currOrg.getOrgCode());
    final GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(adminGroup);
    groupUser.setEshopUser(newEshopUser);
    groupUserRepo.save(groupUser);
    return newEshopUser;
  }


  private CustomerSettings buildCustomerSettings(Organisation currOrg) {
    final CustomerSettings customerSettings = currOrg.getCustomerSettings();
    customerSettings.setOrganisation(currOrg);
    return customerSettings;
  }

  private UserSettings buildUserSettings(CustomerSettings customerSettings, final Organisation affiOrg) {
    final UserSettings userSettings = new UserSettings();
    userSettings.setAllocationId(customerSettings.getAllocationId());
    userSettings.setCollectiveDelivery(customerSettings.getCollectiveDelivery());
    userSettings.setDeliveryType(customerSettings.getDeliveryType());
    userSettings.setPaymentMethod(customerSettings.getPaymentMethod());
    userSettings.setViewBilling(customerSettings.isViewBilling());
    userSettings.setNetPriceConfirm(customerSettings.isNetPriceConfirm());
    userSettings.setShowDiscount(customerSettings.isShowDiscount());
    userSettings.setEmailNotificationOrder(customerSettings.isEmailNotificationOrder());
    userSettings.setDeliveryAddressId(String.valueOf(customerSettings.getDeliveryAddressId()));
    userSettings.setBillingAddressId(String.valueOf(customerSettings.getBillingAddressId()));
    userSettings.setAcceptHappyPointTerm(false);
    userSettings.setSaleOnBehalfOf(true);
    userSettings.setClassicCategoryView(UserSettingConstants.CLASSIC_CATEGORY_VIEW_DEFAULT);
    userSettings.setCurrentStateNetPriceView(userSettings.isNetPriceView());

    final Optional<SupportedAffiliate> affiliate = SupportedAffiliate.fromDesc(affiOrg.getShortname());
    if (!affiliate.isPresent()) {
      return userSettings;
    }

    // Build invoice type
    ErpInvoiceTypeCode invoiceTypeCode;
    if (!ErpInvoiceTypeCode.contains(customerSettings.getInvoiceType().getInvoiceTypeCode())) {
      invoiceTypeCode = ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE;
    } else {
      invoiceTypeCode = ErpInvoiceTypeCode.valueOf(customerSettings.getInvoiceType().getInvoiceTypeCode());
    }

    Optional<InvoiceType> optInvoiceType = invoiceTypeRepo.findOneByInvoiceTypeCode(invoiceTypeCode.name());
    optInvoiceType.ifPresent(userSettings::setInvoiceType);
    userSettings.buildDefaultSettings(affiliate.get());
    return userSettings;
  }

  private Login buildEshopLogin(EshopUser newEshopUser, Organisation affiOrg) {
    final Login login = new Login();
    // Encrypt new password
    login.setPasswordHash(new PasswordHash(ENCRYPTED_DEFAULT_PASSWORD, HashType.BLCK_VAR));
    login.setUserActive(true);
    login.setEshopUser(newEshopUser);
    return login;
  }

  private EshopUser buildEshopUser(UserProfileDto userProfile, UserSettings userSettings) throws Exception {
    final EshopUser eshopUser = new EshopUser();
    eshopUser.setSetting(userSettings.getId());
    eshopUser.setUsername(userProfile.getUserName());
    eshopUser.setEmail(userProfile.getEmail());
    eshopUser.setFirstName(userProfile.getFirstName());
    eshopUser.setLastName(userProfile.getSurName());
    eshopUser.setLastName(userProfile.getSurName());
    eshopUser.setPhone(userProfile.getPhoneNumber());
    eshopUser.setVatConfirm(true); // #1745 Default value on user creation for VAT TRUE
    eshopUser.setType(userProfile.getUserType());

    final Language language = languageRepository.findOneByLangiso(DEFAULT_LANG).orElseThrow(IllegalStateException::new);
    final Salutation salutation = salutationRepo.findOneByCode(SALUTATION_OTHER).orElseThrow(() -> new Exception("Invalid salutation."));

    eshopUser.setLanguage(language);
    eshopUser.setSalutation(salutation);
    eshopUser.setHourlyRate(userProfile.getHourlyRate());
    return eshopUser;
  }

  private EshopGroup getAdminGroup(String customerNumber) throws Exception {
    final EshopRole adminRole = eshopRoleRepo.findOneByName(
        EshopUserCreateAuthority.USER_ADMIN.name())
        .orElseThrow(() -> new Exception("Invalid Admin role."));
    return eshopGroupRepo.findOneByOrgCodeAndRoleId(customerNumber, adminRole.getId())
        .orElseThrow(() -> new Exception("Invalid customer group."));
  }

  private void validateUserProfile(UserProfileDto userProfile, Organisation currOrg,
      Organisation affOrg) throws Exception {

    final List<EshopUser> existingAffiUsers = eshopUserRepository
        .findUsersByUsername(userProfile.getUserName());
    for (final EshopUser user : existingAffiUsers) {
      String affiliateName = null;
      final Organisation organisation = user.firstOrganisation().orElseThrow(IllegalStateException::new);
      final Organisation parentOrganisation = organisationService
          .findOrganisationById(organisation.getParentId())
          .orElseThrow(IllegalStateException::new);
      if (user.hasToCheckTheAffiliate()) {
        if (user.isGroupAdminRole()) {
          affiliateName = user.affiliateInUrl(organisation);
        } else {
          affiliateName = user.affiliateInUrl(parentOrganisation);
        }
      }

      if (StringUtils.equalsIgnoreCase(affOrg.getShortname(), affiliateName)) {
        custNrCanNotCreated.add(currOrg.getOrgCode());
        throw new Exception("User name is not allowed the duplication in the same affiliate." + currOrg.getOrgCode());
      }
    }
  }


}
