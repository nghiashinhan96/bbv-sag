package com.sagag.eshop.service.api.impl;

import static com.sagag.services.common.enums.DeliveryType.PICKUP;
import static com.sagag.services.common.enums.DeliveryType.TOUR;

import com.sagag.eshop.repo.api.AllocationTypeRepository;
import com.sagag.eshop.repo.api.CollectiveDeliveryRepository;
import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.DeliveryTypesRepository;
import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.GroupUserRepository;
import com.sagag.eshop.repo.api.InvoiceTypeRepository;
import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.api.SalutationRepository;
import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.DeliveryType;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.service.api.AxAccountService;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.DeliveryTypesService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.GroupUserService;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.PaymentMethodService;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.api.UserCreateService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.dto.AxUserDto;
import com.sagag.eshop.service.dto.PermissionDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.OrganisationNotFoundException;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.eshop.service.helper.UserServiceFactory;
import com.sagag.eshop.service.helper.UserSettingHelper;
import com.sagag.eshop.service.validator.UserProfileValidator;
import com.sagag.eshop.service.validator.criteria.UserProfileValidateCriteria;
import com.sagag.services.common.contants.EshopUserConstants;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.common.security.CompositePasswordEncoder;
import com.sagag.services.common.utils.SagEshopUserUtils;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.AllocationTypeDto;
import com.sagag.services.domain.eshop.dto.CollectiveDeliveryDto;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;
import com.sagag.services.domain.eshop.dto.InvoiceTypeDto;
import com.sagag.services.domain.eshop.dto.OrganisationDto;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.UserManagementDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

@Service
@Transactional
@Slf4j
public class UserServiceImpl extends AbstractUserCreation
  implements UserService, UserCreateService {

  @Autowired
  private EshopUserRepository userRepository;

  @Autowired
  private EshopGroupRepository eshopGroupRepository;

  @Autowired
  private GroupUserRepository groupUserRepository;

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private SalutationRepository salutationRepo;

  @Autowired
  private EshopRoleRepository eshopRoleRepository;

  @Autowired
  private AllocationTypeRepository allocationRepo;

  @Autowired
  private DeliveryTypesRepository deliveryRepo;

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepo;

  @Autowired
  private CollectiveDeliveryRepository collectiveRepo;

  @Autowired
  private CustomerSettingsRepository customerRepo;

  @Autowired
  private ExternalUserService externalUserService;

  @Autowired
  private AxAccountService axAccountService;

  @Autowired
  private DeliveryTypesService deliveryTypeService;

  @Autowired
  private PermissionService permService;

  @Autowired
  private VUserDetailRepository vUserDetailRepository;

  @Autowired
  private UserProfileValidator userProfileValidator;

  @Autowired
  private GroupUserRepository groupUserRepo;

  @Autowired
  private CustomerSettingsService customerSettingsService;

  @Autowired
  private UserSettingsService userSettingsService;

  @Autowired
  private UserServiceFactory serviceFactory;

  @Autowired
  private LoginService loginService;

  @Autowired
  private GroupUserService groupUserService;

  @Autowired
  private PermissionService permissionService;

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  private CompositePasswordEncoder passwordEncoder;


  @Autowired
  private PaymentMethodService paymentMethodService;

  @Override
  public EshopUser getUserById(Long id) {
    return userRepository.findUserByUserId(id)
        .orElseThrow(() -> new UsernameNotFoundException(
        String.format("User with userId=%s was not found", id)));
  }

  @Override
  public List<EshopUser> getUsersByEmail(String email) {
    return userRepository.findUsersByEmail(email);
  }

  @Override
  public List<EshopUser> getUsersByUsername(String username) {
    return userRepository.findUsersByUsername(username);
  }

  @Override
  public UserProfileDto getUserProfile(long id, final boolean isOtherProfile) {
    return userRepository.findById(id)
        .map(eshopUser -> userProfileInfoConverter()
            .andThen(languageInfoConverter(eshopUser))
            .andThen(salutationInfoConverter(eshopUser))
            .andThen(roleTypesInfoConverter(eshopUser, isOtherProfile))
            .andThen(additionalInfoConverter())
            .apply(eshopUser))
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
  }

  @Override
  public UserProfileDto getUserProfile(UserInfo userInfo, boolean isOtherProfile) {
    return getUserProfile(userInfo.getId(), isOtherProfile);
  }

  @Override
  public UserProfileDto getUserProfileTemplate() {
    return UserProfileDto.builder()
        .salutations(salutationService.getProfileSalutations())
        .languages(getSortedLanguage())
        .types(roleService.getDefaultEshopRoles())
        .build();
  }

  @Override
  public EshopUser createUser(UserProfileDto userProfile, UserInfo user, String genPassword)
      throws com.sagag.services.common.exception.ValidationException {
    OrganisationDto target = OrganisationDto.builder()
        .id(user.getOrganisationId())
        .orgCode(user.getCustNrStr())
        .build();
    return createUserForOtherCustomer(userProfile, user, genPassword, target);
  }

  @Override
  public EshopUser createUserForOtherCustomer(final UserProfileDto userProfile, final UserInfo actionUser,
      final String genPassword, OrganisationDto targetCustomer)
      throws com.sagag.services.common.exception.ValidationException {
    userProfileValidator
        .validate(new UserProfileValidateCriteria(userProfile, actionUser.getAffiliateShortName()));
    // Save setting for delivery Type
    final String sendMethodCode = actionUser.getCustomer().getSendMethodCode();
    int deliveryId =
        getFirstDeliveryTypeId(deliveryRepo.findAll(), getFilteredMethod(sendMethodCode));
    // Get default setting for this garage
    final CustomerSettings customerSettings = customerRepo.findSettingsByOrgId(targetCustomer.getId());

    final UserSettings newUserSettings = serviceFactory.getSettingsCreateService(targetCustomer)
        .createUserSetting(actionUser, customerSettings, userProfile);
    // Create eshop user
    final EshopUser eshopUser = new EshopUser();
    eshopUser.setUsername(userProfile.getUserName());
    eshopUser.setEmail(userProfile.getEmail());
    eshopUser.setFirstName(userProfile.getFirstName());
    eshopUser.setLastName(userProfile.getSurName());
    eshopUser.setPhone(userProfile.getPhoneNumber());

    final int languageId = userProfile.getLanguageId();
    final int salutationId = userProfile.getSalutationId();
    eshopUser.setSetting(newUserSettings.getId());
    eshopUser.setLanguage(languageRepo.findById(languageId).orElse(null));
    eshopUser.setSalutation(salutationRepo.findById(salutationId).orElse(null));
    eshopUser.setHourlyRate(userProfile.getHourlyRate());
    eshopUser.setVatConfirm(true); // #1745 Default value on user creation for VAT TRUE
    eshopUser.setEmailConfirmation(BooleanUtils.isTrue(userProfile.getEmailConfirmation()));
    final EshopUser newEshopUser = userRepository.save(eshopUser);
    customerSettings.setDeliveryId(deliveryId);
    customerRepo.save(customerSettings);

    // Create login user
    loginService.createLogin(newEshopUser, genPassword);

    final EshopGroup eshopGroup =
        eshopGroupRepository.findByOrgIdAndRoleId(targetCustomer.getId(), userProfile.getTypeId())
            .orElseThrow(() -> new ValidationException("Can not find eshop group information"));

    // Insert group user
    final GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(eshopGroup);
    groupUser.setEshopUser(newEshopUser);
    groupUserRepository.save(groupUser);

    newEshopUser.setGroupUsers(Collections.singletonList(groupUser));

    return newEshopUser;
  }

  @Override
  public EshopUser updateUserProfile(UserProfileDto userProfile, UserInfo user,
      final boolean isOtherProfile) throws com.sagag.services.common.exception.ValidationException {
    userProfileValidator
        .validate(new UserProfileValidateCriteria(userProfile, user.getAffiliateShortName()));
    final EshopUser eshopUser = userRepository.findById(userProfile.getId()).orElseThrow(
        () -> new UserValidationException(UserErrorCase.UE_NFU_001, "Invalid User Id."));
    eshopUser.setUsername(userProfile.getUserName());
    eshopUser.setEmail(userProfile.getEmail());
    eshopUser.setFirstName(userProfile.getFirstName());
    eshopUser.setLastName(userProfile.getSurName());
    eshopUser.setPhone(userProfile.getPhoneNumber());
    final int languageId = userProfile.getLanguageId();
    final int salutationId = userProfile.getSalutationId();
    eshopUser.setLanguage(languageRepo.findById(languageId).orElse(null));
    eshopUser.setSalutation(salutationRepo.findById(salutationId).orElse(null));
    eshopUser.setHourlyRate(userProfile.getHourlyRate());

    // if provide email confirmation then update
    if (userProfile.getEmailConfirmation() != null) {
      eshopUser.setEmailConfirmation(userProfile.getEmailConfirmation());
    }
    if (isOtherProfile) {
      final Organisation org =
          eshopUser.firstOrganisation().orElseThrow(OrganisationNotFoundException::new);
      final EshopGroup eshopGroup =
          eshopGroupRepository.findByOrgIdAndRoleId(org.getId(), userProfile.getTypeId())
              .orElseThrow(() -> new ValidationException("Can not find eshop group information"));
      final GroupUser groupUser = groupUserRepository.findOneByEshopUser(eshopUser)
          .orElseThrow(() -> new ValidationException("Can not find groupUser information"));
      userSettingsService.updateShowHappyPointForUser(eshopUser, eshopGroup);
      groupUser.setEshopGroup(eshopGroup);
      groupUser.setEshopUser(eshopUser);
      groupUserRepository.save(groupUser);
    }
    return userRepository.save(eshopUser);
  }

  @Override
  public List<UserManagementDto> getUserSameOrganisation(final UserInfo user) {
    final List<VUserDetail> users =
        vUserDetailRepository.findUsersByOrgId(user.getOrganisationId());
    return users.stream().map(convertUserManagementDto(user.getId())).collect(Collectors.toList());
  }

  private static Function<VUserDetail, UserManagementDto> convertUserManagementDto(
      final long currentUserId) {
    return u -> UserManagementDto.builder().garageName(u.getOrgName()).salutation(u.getSalutCode())
        .role(u.getRoleName()).firstName(u.getFirstName()).lastName(u.getLastName())
        .isUserActive(true).id(u.getUserId()).userName(u.getUserName())
        .isUserAdmin(u.isUserAdminRole()).canDelete(u.getUserId() != currentUserId).build();
  }

  @Override
  public void deactiveUserById(Long userId) {
    log.debug("Deactive user login with user id = {}", userId);
    Assert.notNull(userId, "The given user id must not be null");
    final Optional<Login> loginOpt = loginRepository.findByUserId(userId);
    if (!loginOpt.isPresent()) {
      return;
    }
    final Login login = loginOpt.get();
    login.setUserActive(false);
    loginRepository.save(login);
  }

  @Override
  public PaymentSettingDto getPaymentSetting(boolean isSalesOnbehalfUser, boolean isWholesaler) {
    final PaymentSettingDto paymentSetting = new PaymentSettingDto();
    final List<AllocationTypeDto> allocationTypes = allocationRepo.findAll().stream()
        .map(u -> AllocationTypeDto.builder().id(u.getId()).descCode(u.getDescCode())
            .description(u.getDescription()).type(u.getType()).allowChoose(true).build())
        .collect(Collectors.toList());

    paymentSetting.setAllocationTypes(allocationTypes);
    final List<CollectiveDeliveryDto> collectiveDeliveries = collectiveRepo.findAll().stream()
        .map(u -> CollectiveDeliveryDto.builder().id(u.getId()).descCode(u.getDescCode())
            .type(u.getType()).description(u.getDescription()).allowChoose(true).build())
        .collect(Collectors.toList());
    paymentSetting.setCollectiveTypes(collectiveDeliveries);

    final List<PaymentMethodDto> paymentMethods =
        paymentMethodService.getPaymentMethodOptions(isSalesOnbehalfUser);
    paymentSetting.setPaymentMethods(paymentMethods);

    // 7394 - WINT: wrong delivery type for wholesaler in customer setting
    if (isWholesaler) {
      List<String> descCodes = new ArrayList<>(Arrays.asList(PICKUP.name(), TOUR.name()));
      paymentSetting.setDeliveryTypes(deliveryTypeService.findWholesalerDeliveryTypes(descCodes));
    } else {
      paymentSetting.setDeliveryTypes(deliveryTypeService.findAllDeliveryTypes());
    }

    final List<InvoiceTypeDto> invoiceTypes = invoiceTypeRepo.findAll().stream()
        .map(i -> InvoiceTypeDto.builder().id(i.getId()).descCode(i.getInvoiceTypeCode())
            .invoiceType(i.getInvoiceTypeName()).invoiceTypeDesc(i.getInvoiceTypeDesc())
            .allowChoose(true).build())
        .collect(Collectors.toList());
    paymentSetting.setInvoiceTypes(invoiceTypes);
    return paymentSetting;
  }

  @Override
  public String searchCustomerAdminUser(final String customerNr) {
    final Optional<VUserDetail> onBehalfOfForSaleUser =
        vUserDetailRepository.findUserByOnBehalfAdmin(customerNr);
    return onBehalfOfForSaleUser.map(VUserDetail::getUserName).orElse(StringUtils.EMPTY);
  }

  @Override
  public AxUserDto createAXUser(UserProfileDto userProfileDto) {
    // check if already exists the AX profile in eConnect, return it
    final Optional<ExternalUser> extUser =
        externalUserService.searchByUsernameAndApp(userProfileDto.getUserName(), ExternalApp.AX);
    if (extUser.isPresent()) {
      final ExternalUser alreadyCreatedExtUser = extUser.get();
      final long userId = alreadyCreatedExtUser.getEshopUserId();
      if (userRepository.isAadSaleAccountHasNoRole(userId)) {
        userRepository.findUserByUserId(userId)
            .ifPresent(user -> createGroupUser(getSaleGroupId(), user));
      }
      // already exist the external user with this username
      log.debug("External user {} already exists, use the old one.", extUser.get().getUsername());
      return buildAxUserDto(extUser.get());
    }
    // create default user settings
    final UserSettings settings = userSettingsRepo.save(UserSettingHelper
        .buildDefaultUserSettingFromCustomerSetting(customerSettingHelper.buildDefaultCustomerSetting()));
    // create shop user
    final EshopUser createdAxUser = createDefaultUser(userProfileDto, settings.getId());
    // create login
    createLogin(createdAxUser);
    // create group user (this should be assigned by Admin function)
    final Optional<AadAccounts> axAccount =
        axAccountService.searchSaleAccount(userProfileDto.getUserName());
    axAccount.ifPresent(aadAccounts -> createGroupUser(getSaleGroupId(), createdAxUser));
    // create external user mapping
    final ExternalUser createdExtUser = createExternalUser(createdAxUser);
    // build the AXUserDto to return
    return buildAxUserDto(createdExtUser);
  }

  private int getSaleGroupId() {
    return eshopGroupRepository.findEshopGroupIdByName(EshopAuthority.SALES_ASSISTANT.name())
        .orElseThrow(() -> new NoSuchElementException("Not found eshop group id for sales type"));
  }

  private static AxUserDto buildAxUserDto(final ExternalUser extUser) {
    // build the AXUserDto to return
    final AxUserDto axUserInfo = new AxUserDto();
    axUserInfo.setExternalId(extUser.getId());
    axUserInfo.setEshopUserId(extUser.getEshopUserId());
    axUserInfo.setUsername(extUser.getUsername());
    return axUserInfo;
  }

  private ExternalUser createExternalUser(final EshopUser createdAxUser) {
    // @formatter:off
    final ExternalUserDto externalUser = ExternalUserDto.builder()
            .eshopUserId(createdAxUser.getId())
            .externalApp(ExternalApp.AX)
            .username(createdAxUser.getUsername())
            .password(passwordEncoder.encodeBlockVar(createdAxUser.getUsername()))
            .active(true)
            .createdDate(Calendar.getInstance().getTime()).build();
    // @formatter:on
    return externalUserService.addExternalUser(externalUser);
  }

  private GroupUser createGroupUser(int salesGroupId, EshopUser createdAxUser) {
    final GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(eshopGroupRepository.findById(salesGroupId).orElse(null));
    groupUser.setEshopUser(createdAxUser);
    return groupUserRepository.save(groupUser);
  }

  @Override
  public String getFullName(final Long userId) {
    return userRepository.searchFullNameById(userId);
  }

  @Override
  public void saveEshopUser(EshopUser eshopUser) {
    userRepository.save(eshopUser);
  }

  @Override
  public UserInfo getSessionUserInfo(long currentLoggedUserId) {
    final EshopUser eshopUser = userRepository.findUserLoginByUserId(currentLoggedUserId)
        .orElseThrow(() -> new IllegalArgumentException(String.format(
            "Illegal input argument user id = %d", currentLoggedUserId)));

    final UserInfo user = new UserInfo();
    user.setId(eshopUser.getId());
    user.setType(eshopUser.getType());
    user.setUsername(eshopUser.getUsername());
    user.setFirstName(eshopUser.getFirstName());
    user.setLastName(eshopUser.getLastName());
    user.setOriginalUserId(eshopUser.getOriginalUserId());

    Optional.ofNullable(eshopUser.getLangiso())
    .map(StringUtils::lowerCase)
    .ifPresent(langIso -> {
      user.setLanguage(langIso);
      user.setUserLocale(localeContextHelper.toLocale(langIso));
    });

    user.setEmail(eshopUser.getEmail());
    user.setHourlyRate(eshopUser.getHourlyRate());
    user.setVatConfirm(eshopUser.isVatConfirm());
    user.setSignInDate(eshopUser.getSignInDate());
    user.setFirstLoginDate(eshopUser.getFirstLoginDate());
    user.setLastOfOnBehalfDate(eshopUser.getLastOfOnBehalfDate());

    user.setRoles(eshopRoleRepository.findRolesFromUserId(currentLoggedUserId));
    List<PermissionDto> permissions = permService.getUserPermissions(currentLoggedUserId);
    if (user.isOciVirtualUser()) {
      permissions = removeOfferPermission(permissions);
    }
    user.setPermissions(permissions);
    return user;
  }

  @Override
  public Optional<Integer> getOrgIdByUserId(Long userId) {
    return vUserDetailRepository.findOrgIdByUserId(userId);
  }

  @Override
  public List<String> getAllUsernamesByOrgId(Integer orgId) {
    return vUserDetailRepository.findUsernamesByOrgId(orgId);
  }

  @Override
  public String getUsernameById(Long userId) {
    return userRepository.findUsernameById(userId).orElse(StringUtils.EMPTY);
  }

  @Override
  public Optional<String> findAffiliateShortNameById(Long userId) {
    VUserDetail userDetail = vUserDetailRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("User didn't existed"));
    Integer wholesalerId =
        WholesalerUtils.isFinalCustomer(userDetail.getOrgCode()) ? userDetail.getOrgParentId() : userDetail.getOrgId();
    return Optional.of(organisationService.findAffiliateByOrgId(wholesalerId));
  }

  @Override
  public void updateLoginSignInDate(final long userId, final Date signInDate) {
    userRepository.updateLoginSignInDate(signInDate, userId);
  }

  @Override
  @Transactional
  public EshopUser createEshopUserAdminByCustomer(UserProfileDto userProfile, String rawPassword,
      Optional<UserType> userType, Organisation customer, SupportedAffiliate affiliate,
      String langISO) {
    Assert.notNull(userProfile, "The given user profile must not be null");
    Assert.notNull(customer, "The given customer must not be null");
    Assert.notNull(affiliate, "The given affiliate must not be null");
    Assert.hasText(rawPassword, "The given raw password must not be empty");
    Assert.hasText(langISO, "The given lang iso must not be empty");

    // Build User settings
    final Integer savedUserSettingsId = createUserSettings(customer, affiliate);

    // Build Eshop user
    final EshopUser savedEshopUser =
        createEshopUser(userProfile, userType, langISO, savedUserSettingsId);

    // Build Login info
    loginService.createLogin(savedEshopUser, rawPassword);

    createAdminEshopGroup(customer, savedEshopUser);

    return savedEshopUser;
  }

  private EshopUser createEshopUser(UserProfileDto userProfile, Optional<UserType> userType,
      String langISO, final Integer savedUserSettingsId) {
    final Language language = languageRepo.findOneByLangiso(langISO)
        .orElseGet(() -> languageRepo.findDefaultLanguage(
            StringUtils.upperCase(localeContextHelper.defaultAppLocaleLanguage())));

    final Optional<Salutation> salutation =
        salutationRepo.findOneByCode(EshopUserConstants.SALUTATION_OTHER);
    final EshopUser eshopUser =
        EshopUser.buildEshopUser(userProfile, savedUserSettingsId, language, salutation, userType);
    final EshopUser savedEshopUser = userRepository.save(eshopUser);
    return savedEshopUser;
  }

  @Override
  @Transactional
  public EshopUser createEshopUserAdminForAPMCustomer(UserProfileDto userProfile, String passwordHash,
      String passwordSalt, Optional<UserType> userType, Organisation customer, SupportedAffiliate affiliate,
      String langISO) {
    Assert.notNull(userProfile, "The given user profile must not be null");
    Assert.notNull(customer, "The given customer must not be null");
    Assert.notNull(affiliate, "The given affiliate must not be null");
    Assert.hasText(passwordHash, "The given hash password must not be empty");
    Assert.hasText(passwordSalt, "The given salt must not be empty");
    Assert.hasText(langISO, "The given lang iso must not be empty");

    // Build User settings
    final Integer savedUserSettingsId = createUserSettings(customer, affiliate);

    // Build Eshop user
    final EshopUser savedEshopUser =
        createEshopUser(userProfile, userType, langISO, savedUserSettingsId);

    // Build Login info
    loginService.createAPMUserLogin(savedEshopUser, passwordHash, passwordSalt);

    // Build Admin Eshop Group
    createAdminEshopGroup(customer, savedEshopUser);

    return savedEshopUser;
  }

  private void createAdminEshopGroup(Organisation customer, final EshopUser savedEshopUser) {
    final EshopRole adminRole =
        eshopRoleRepository.findOneByName(EshopAuthority.USER_ADMIN.name())
            .orElseThrow(() -> new IllegalArgumentException("Can not find eshop role by role name"));

    final EshopGroup adminGroup =
        eshopGroupRepository.findOneByOrgCodeAndRoleId(customer.getOrgCode(), adminRole.getId())
            .orElseThrow(() -> new IllegalArgumentException("Not found admin group in DB"));
    final GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(adminGroup);
    groupUser.setEshopUser(savedEshopUser);
    groupUserRepo.save(groupUser);
  }

  private Integer createUserSettings(Organisation customer, SupportedAffiliate affiliate) {
    final CustomerSettings customerSettings =
        customerSettingsService.findSettingsByOrgId(customer.getId());
    final UserSettings userSettings = UserSettings.buildUserSettingsBy(affiliate, customerSettings);
    final Integer savedUserSettingsId = userSettingsRepo.save(userSettings).getId();
    return savedUserSettingsId;
  }

  @Override
  @Transactional
  public EshopUser createVirtualUser(final Long id, final UserType userType, final String langIso) {
    EshopUser eshopUser =
        userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
            String.format("Not found any user for this userid = %d ", id)));

    final UserSettings userSettings = userSettingsRepo.findByUserId(id)
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("Not found any setting for this userid = %d ", id)));

    // Clone user setting
    UserSettings virtualUserSettings =
        userSettingsRepo.save(userSettingsService.clone(userSettings));
    // Create eshop user
    EshopUser clonedEshopUser = clone(eshopUser);
    languageService.findOneByLangiso(langIso).ifPresent(clonedEshopUser::setLanguage);

    // #2500 Use the customer name plus a time-stamp
    Organisation org =
        organisationService.getFirstByUserId(id).orElseThrow(OrganisationNotFoundException::new);
    String virtualUsername = SagEshopUserUtils.getDefaultVirtualUsername(org.getName());
    clonedEshopUser.setUsername(virtualUsername);
    clonedEshopUser.setSetting(virtualUserSettings.getId());
    clonedEshopUser.setType(userType.toString());
    EshopUser virtualEshopUser = userRepository.save(clonedEshopUser);

    // Generate and encrypt new password
    final String genPassword =
        RandomStringUtils.randomAlphanumeric(SagConstants.DEFAULT_MIN_LENGTH_PASSWORD);
    Login login = loginService.createLogin(virtualEshopUser, genPassword);
    final Date currentDate = Calendar.getInstance().getTime();
    loginService.updateFirstLoginDate(login.getId(), currentDate);

    EshopGroup eshopGroup = eshopUser.getFirstEshopGroup().orElseThrow(
        () -> new IllegalArgumentException("Can not find eshop group for specified eshopUser"));
    List<GroupUser> groupUsers = new ArrayList<>();
    groupUsers.add(groupUserService.createGroupUser(virtualEshopUser, eshopGroup));

    virtualEshopUser.setLogin(login);
    virtualEshopUser.setGroupUsers(groupUsers);
    return userRepository.save(virtualEshopUser);
  }

  @Override
  public EshopUser clone(EshopUser eshopUser) {
    return EshopUser.builder() // @formatter:off
        .originalUserId(eshopUser.getId())
        .email(eshopUser.getEmail())
        .emailConfirmation(eshopUser.isEmailConfirmation())
        .firstName(eshopUser.getFirstName())
        .hourlyRate(eshopUser.getHourlyRate())
        .language(eshopUser.getLanguage())
        .lastName(eshopUser.getLastName())
        .newsletter(eshopUser.isNewsletter())
        .phone(eshopUser.getPhone())
        .fax(eshopUser.getFax())
        .signInDate(eshopUser.getSignInDate())
        .salutation(eshopUser.getSalutation())
        .type(eshopUser.getType())
        .username(eshopUser.getUsername())
        .vatConfirm(eshopUser.isVatConfirm())
        .customer(eshopUser.getCustomer())
        .langiso(eshopUser.getLangiso()).build();
    // @formatter:on
  }

  @Override
  public boolean hasPermission(long userId, PermissionEnum perm) {
    final List<PermissionDto> perms = permissionService.getUserPermissions(userId);
    if (CollectionUtils.isEmpty(perms) || perm == null) {
      return false;
    }
    return perms.parallelStream().map(PermissionDto::getPermission).collect(Collectors.toList())
        .contains(perm.name());
  }

  private List<PermissionDto> removeOfferPermission(List<PermissionDto> permissions) {
    return permissions.stream().filter(
        perm -> !StringUtils.equalsIgnoreCase(perm.getPermission(), PermissionEnum.OFFER.name()))
        .collect(Collectors.toList());
  }

  private static int getFirstDeliveryTypeId(final List<DeliveryType> deliveryTypes,
      final ErpSendMethodEnum sendMethod) {
    if (CollectionUtils.isEmpty(deliveryTypes)) {
      throw new IllegalArgumentException("Delivery type list must not be empty.");
    }
    return deliveryTypes.stream()
        .filter(d -> StringUtils.endsWithIgnoreCase(d.getDescCode(), sendMethod.name()))
        .findFirst()
        .map(DeliveryType::getId)
        .orElseThrow(() -> new IllegalArgumentException("Delivery type must be present."));
  }

  private static ErpSendMethodEnum getFilteredMethod(final String sendMethod) {
    if (ErpSendMethodEnum.TOUR.name().equalsIgnoreCase(sendMethod)) {
      return ErpSendMethodEnum.TOUR;
    }
    return ErpSendMethodEnum.PICKUP;
  }

  @Override
  public EshopUser updateUserLanguage(EshopUser user, String langIso) {
    final Language lang = languageService.findLanguageByLangIsoOrTecDoc(langIso);

    user.setLanguage(lang);
    return userRepository.save(user);
  }

  @Override
  public boolean hasRoleByUsername(String username, EshopAuthority role) {
    return CollectionUtils.emptyIfNull(userRepository.findUsersByUsername(username)).stream()
        .filter(user -> username.equals(user.getUsername())).findFirst()
        .map(user -> user.getRoles().contains(role.name())).orElse(false);
  }
}
