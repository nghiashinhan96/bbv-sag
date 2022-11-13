package com.sagag.services.service.customer.registration;

import com.sagag.eshop.repo.api.collection.CollectionPermissionRepository;
import com.sagag.eshop.repo.api.collection.CollectionRelationRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.collection.CollectionPermission;
import com.sagag.eshop.repo.entity.collection.CollectionRelation;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.ExternalOrganisationService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.OrganisationCollectionException;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.eshop.service.permission.configuration.BackOfficeCollectionPermissionConfiguration;
import com.sagag.eshop.service.validator.UserProfileValidator;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.common.contants.EshopUserConstants;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.dto.ExternalOrganisationDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.mdm.api.DvseUserService;
import com.sagag.services.mdm.client.MdmResponseException;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.mdm.utils.DvseUserUtils;
import com.sagag.services.service.api.DvseBusinessService;
import com.sagag.services.service.utils.EshopUserUtils;
import com.sagag.services.service.validator.FirstUserAdminValidator;
import com.sagag.services.service.validator.OnBehalfUserValidator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractCustomerRegistrationHandler {

  @Autowired
  private CustomerExternalService custExtService;

  @Autowired
  private FirstUserAdminValidator firstUserAdminValidator;

  @Autowired
  private OnBehalfUserValidator onBehalfUserValidator;

  @Autowired
  protected UserProfileValidator userProfileValidator;

  @Autowired
  protected OrganisationService orgService;

  @Autowired
  private CustomerSettingsService custSettingsService;

  @Autowired
  protected UserService userService;

  @Autowired
  protected DvseBusinessService dvseBusinessService;

  @Autowired
  private DvseUserService dvseUserService;

  @Autowired
  private ExternalOrganisationService extOrgService;

  @Autowired
  private CollectionRelationRepository collectionRelationRepo;

  @Autowired
  private CollectionPermissionRepository collPermissionRepo;

  @Autowired
  private BackOfficeCollectionPermissionConfiguration collPermissionConfig;

  @Autowired
  protected LocaleContextHelper localeContextHelper;

  @Autowired
  protected OrganisationCollectionService orgCollectionService;

  /**
   * Handles customer registration.
   *
   * @param userRegistration
   * @param args additional arguments.
   */
  public void handle(UserRegistrationDto userRegistration, String... args) throws ServiceException {
    final String defaultPassword = args[0];
    Assert.notNull(userRegistration, "The given criteria must not be null");
    validateUserRegistration(userRegistration);

    // Create customer organization
    final String custNr = userRegistration.getCustomerNumber();
    final String affiliateStr = userRegistration.getAffiliate();
    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(affiliateStr);
    final CustomerInfo customerInfo =
        custExtService.getActiveCustomerInfo(affiliate.getCompanyName(), custNr).orElseThrow(
            () -> new UserValidationException(UserErrorCase.UE_CIN_001, "Customer is inactive."));

    final Customer customer = customerInfo.getCustomer();
    final List<Address> addresses = customerInfo.getAddresses();

    final Organisation customerOrg;
    final Optional<Organisation> existingCustomerOrg = orgService.findOrganisationByOrgCode(custNr);
    boolean isDvseCustomer = false;
    if (!existingCustomerOrg.isPresent()) {
      final String collectionShortName =
          StringUtils.defaultIfBlank(userRegistration.getCollectionShortName(), affiliateStr);

      final List<CollectionPermission> maxPermissionForCollection =
          collPermissionRepo.findByCollectionShortName(collectionShortName);
      final List<PermissionEnum> collecionPermissions =
          collPermissionConfig.getPermissions(affiliateStr, maxPermissionForCollection, true)
              .stream().filter(PermissionConfigurationDto::isEnable)
              .map(PermissionConfigurationDto::getPermission).map(PermissionEnum::valueOf)
              .collect(Collectors.toList());

      isDvseCustomer = collecionPermissions.stream().anyMatch(PermissionEnum::isDvse);

      customerOrg = createFirstCustomerOrg(collectionShortName, collecionPermissions, affiliate,
          custNr, customer, addresses);
    } else {
      customerOrg = existingCustomerOrg.get();
    }

    updatePartnerProgramSetting(customerOrg.getCustomerSettings(), userRegistration);
    final String language = localeContextHelper.toLocale(customer.getLanguage()).getLanguage();

    if (!userRegistration.isCreateOnBehalfOnly()) {
      createCustomerAdminUser(userRegistration, defaultPassword, affiliate, customerOrg,
          isDvseCustomer, language);
    }

    // Create user on behalf
    createOnbehalfUserAndDvseUser(affiliate, customerOrg, language,
        userRegistration.isCreateOnBehalfOnly());
  }

  protected void validateUserRegistration(UserRegistrationDto userRegistration)
      throws UserValidationException {
    firstUserAdminValidator.validate(userRegistration);
  }

  /**
   * Creates customer admin user.
   *
   * @param userRegistration
   * @param defaultPassword
   * @param affiliate
   * @param customerOrg
   * @param isDvseCustomer
   * @param language
   *
   * @throws UserValidationException
   * @throws MdmCustomerNotFoundException
   */
  protected abstract void createCustomerAdminUser(UserRegistrationDto userRegistration,
      final String defaultPassword, final SupportedAffiliate affiliate,
      final Organisation customerOrg, boolean isDvseCustomer, final String language)
      throws UserValidationException, MdmCustomerNotFoundException;

  /**
   * Creates customer admin user and DVSE user.
   *
   * @param userRegistration
   * @param defaultPassword
   * @param affiliate
   * @param customer
   * @param langISO
   *
   * @throws UserValidationException
   * @throws MdmCustomerNotFoundException
   *
   * @return the created {@link EshopUser}
   */
  protected abstract EshopUser createUserAdminUserAndDvseUser(UserRegistrationDto userRegistrationDto,
      String defaultPassword, SupportedAffiliate affiliate, Organisation customer, String langISO)
      throws UserValidationException, MdmCustomerNotFoundException;

  private Organisation createFirstCustomerOrg(final String collectionShortName,
      final List<PermissionEnum> collecionPermissions, final SupportedAffiliate affiliate,
      final String custNr, final Customer customer, final List<Address> addresses)
      throws ServiceException {

    final CustomerSettings custSettings =
        custSettingsService.createCustomerSettings(customer, addresses);

    final String custCompanyName = customer.getCompanyName();
    final Organisation customerOrg = createCustomerOrgAndDvseCustomer(affiliate, custNr,
        custCompanyName, custSettings, addresses, collecionPermissions);

    final OrganisationCollection collection =
        orgCollectionService.getCollectionByShortName(collectionShortName)
            .orElseThrow(OrganisationCollectionException.collectionNotFound(collectionShortName));

    final CollectionRelation collectionRelation =
        CollectionRelation.builder().collectionId(collection.getId())
            .organisationId(customerOrg.getId()).isActive(true).build();
    collectionRelationRepo.save(collectionRelation);
    return customerOrg;
  }

  private Organisation createCustomerOrgAndDvseCustomer(SupportedAffiliate affiliate,
      String customerNr, String companyName, CustomerSettings custSettings, List<Address> addresses,
      List<PermissionEnum> defaultPermission) throws UserValidationException {
    final Organisation customer =
        orgService.createCustomer(affiliate, customerNr, companyName, custSettings);
    orgService.createCustomerAddresses(customer, addresses);
    orgService.assignCustomerGroupAndDefaultPermission(customer, defaultPermission);
    // Create new DVSE customer for DDAT at MDM service
    if (!orgService.isDvseCustomer(customer.getId())) {
      return customer;
    }

    // Create new DVSE customer
    try {
      final String customerName = generateUniqueCustomerName();
      final String customerId = dvseUserService.createCustomer(customerName, affiliate);

      final ExternalOrganisationDto externalOrg = new ExternalOrganisationDto();
      externalOrg.setOrgId(customer.getId());
      externalOrg.setExternalCustomerId(customerId);
      externalOrg.setExternalCustomerName(customerName);
      externalOrg.setExternalApp(ExternalApp.DVSE);
      extOrgService.addExternalOrganisation(externalOrg);
    } catch (final NoSuchElementException | MdmResponseException ex) {
      log.error("Create new MDM customer for affiliate has error: ", ex);
      throw new UserValidationException(UserErrorCase.UE_MCC_001, "Create new MDM customer failed");
    }
    return customer;
  }

  private String generateUniqueCustomerName() throws UserValidationException {
    String customerName = null;
    boolean isValidCustomerName = false;
    for (int i = 0; i < DvseUserUtils.MAX_UNIQUE_NAME_RETRY; i++) {
      customerName = DvseUserUtils.generateRandomCustomerName();
      isValidCustomerName = !extOrgService.isCustomerNameExisted(customerName);
      if (isValidCustomerName) {
        break;
      }
    }
    if (!isValidCustomerName) {
      throw new UserValidationException(UserErrorCase.UE_MCC_001,
          "Failed to generate unique customer name");
    }
    return customerName;
  }

  private void updatePartnerProgramSetting(CustomerSettings customerSettings,
      UserRegistrationDto userRegistration) {
    customerSettings.setHasPartnerprogramView(userRegistration.isHasPartnerprogramView());
    custSettingsService.saveCustomerSetting(customerSettings);
  }

  protected void createOnbehalfUserAndDvseUser(final SupportedAffiliate affiliate,
      final Organisation customer, final String langISO, final boolean isCreateOnBehalfOnly)
      throws UserValidationException, MdmCustomerNotFoundException {

    final String custNr = customer.getOrgCode();
    final UserRegistrationDto onBehalfAdminUserDto = new UserRegistrationDto();
    onBehalfAdminUserDto.setFirstName(custNr);
    onBehalfAdminUserDto.setSurName(EshopUserConstants.ON_BEHALF_AGENT_SURNAME);
    onBehalfAdminUserDto.setUserName(EshopUserUtils.buildOnbehalfUsername(custNr));
    onBehalfAdminUserDto.setEmail(EshopUserConstants.ON_BEHALF_DEFAULT_EMAIL);
    onBehalfAdminUserDto.setPhoneNumber(SagConstants.SPACE);
    onBehalfAdminUserDto.setUserType(UserType.ON_BEHALF_ADMIN.name());
    onBehalfAdminUserDto.setLangIso(langISO);

    final String onBehalfUsername = onBehalfAdminUserDto.getUserName();
    if (onBehalfUserValidator.validate(onBehalfUsername)) {
      // If existing and check 'Create On Behalf Only', throw message
      if (isCreateOnBehalfOnly) {
        throw new UserValidationException(UserErrorCase.UE_ICR_001,
            "On behalf User name is existing in same customer.");
      }
      // If existing and without check 'Create On Behalf', do nothing
      return;
    }

    final UserProfileDto userProfileDto =
        SagBeanUtils.map(onBehalfAdminUserDto, UserProfileDto.class);

    createEshopUserByProfile(userProfileDto, EshopUserConstants.DEFAULT_ON_BEHALF_PWORD,
        Optional.of(UserType.ON_BEHALF_ADMIN), customer, affiliate, langISO);
  }

  protected EshopUser createEshopUserByProfile(UserProfileDto userProfile, String rawPassword,
      Optional<UserType> userType, Organisation customer, SupportedAffiliate affiliate,
      String langISO) throws UserValidationException, MdmCustomerNotFoundException {
    final EshopUser eshopUser = userService.createEshopUserAdminByCustomer(userProfile, rawPassword,
        userType, customer, affiliate, langISO);
    final int orgId = customer.getId();
    if (orgService.isDvseCustomer(orgId)) {
      dvseBusinessService.createDvseUserInfo(eshopUser.getId(), affiliate, orgId);
    }
    return eshopUser;
  }

}
