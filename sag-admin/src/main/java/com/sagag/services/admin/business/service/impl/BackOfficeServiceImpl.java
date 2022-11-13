package com.sagag.services.admin.business.service.impl;

import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.ExternalOrganisationRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.VUserExportRepository;
import com.sagag.eshop.repo.criteria.VUserExportSearchCriteria;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.repo.entity.VUserExport;
import com.sagag.eshop.repo.specification.VUserExportSpecification;
import com.sagag.eshop.service.api.DeliveryTypeAdditionalService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.GroupUserService;
import com.sagag.eshop.service.api.LanguageService;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.PaymentMethodService;
import com.sagag.eshop.service.api.RoleService;
import com.sagag.eshop.service.api.SalutationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.eshop.service.validator.BackOfficeUsernameDuplicationValidator;
import com.sagag.eshop.service.validator.criteria.BackOfficeUsernameDuplicationCriteria;
import com.sagag.services.admin.business.service.BackOfficeService;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.backoffice.dto.ExportingUserDto;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.criteria.UserExportRequest;
import com.sagag.services.domain.eshop.dto.BackOfficeUserDto;
import com.sagag.services.domain.eshop.dto.BackOfficeUserSettingDto;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackOfficeServiceImpl implements BackOfficeService {

  @Autowired
  private UserService userService;

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Autowired
  private LanguageService languageService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private SalutationService salutationService;

  @Autowired
  private GroupUserService groupUserService;

  @Autowired
  private PaymentMethodService paymentMethodService;

  @Autowired
  private ExternalUserService externalUserService;

  @Autowired
  private LoginService loginService;

  @Autowired
  private ExternalOrganisationRepository extOrgRepo;

  @Autowired
  private VUserExportRepository vUserExportRepo;

  @Autowired
  private BackOfficeUsernameDuplicationValidator backOfficeUsernameDuplicationValidator;

  @Autowired
  private EshopGroupRepository eshopGroupRepo;

  @Autowired
  private OrganisationRepository organisationRepo;

  @Autowired
  private UserSettingsService userSettingsService;

  @Autowired
  private DeliveryTypeAdditionalService deliveryTypeAdditionalHandler;

  @Override
  public BackOfficeUserDto getBackOfficeSetting(final long userId) throws UserValidationException {
    final EshopUser eshopUser = eshopUserRepo.findById(userId).orElseThrow(
        () -> new UserValidationException(UserErrorCase.UE_NFU_001, "Invalid User Id."));
    final Organisation organisation = eshopUser.firstOrganisation().orElseThrow(
        () -> new UserValidationException(UserErrorCase.UE_IAF_001, "Invalid affiliate."));

    final BackOfficeUserDto backOfficeUserDto = new BackOfficeUserDto();
    final PaymentSettingDto paymentSettingDto = userService.getPaymentSetting(false, false);
    deliveryTypeAdditionalHandler.additionalHandleForDeliveryType(organisation.getOrgCode(),
        paymentSettingDto);
    backOfficeUserDto.setCollectiveDelivery(paymentSettingDto.getCollectiveTypes());
    backOfficeUserDto.setInvoiceTypes(paymentSettingDto.getInvoiceTypes());
    backOfficeUserDto.setDeliveryTypes(paymentSettingDto.getDeliveryTypes());
    backOfficeUserDto.setPaymentMethod(paymentSettingDto.getPaymentMethods());
    backOfficeUserDto.setLanguageDtos(languageService.getAllLanguage());
    backOfficeUserDto.setSalutationDtos(salutationService.getProfileSalutations());

    final Login login = loginService.getLoginForUser(userId);
    backOfficeUserDto.setIsUserActive(login.isUserActive());
    syncShowHappyPointWithCustomer(EnumUtils.getEnum(EshopAuthority.class, eshopUser.getRoles().get(0)),
        eshopUser.getUserSetting(), organisation.getCustomerSettings());
    getUserPropertiesAndsetting(eshopUser, backOfficeUserDto, organisation);
    externalUserService.getDvseExternalUserByUserId(userId).map(ExternalUserDto::getUsername)
        .ifPresent(backOfficeUserDto::setExternalUserName);

    getExteralOrganisationName(eshopUser)
        .ifPresent(backOfficeUserDto::setExternalCustomerName);

    return backOfficeUserDto;
  }

  private void syncShowHappyPointWithCustomer(EshopAuthority role, UserSettings userSettings,
      CustomerSettings customerSettings) {
    UserSettings toUpdate = userSettingsService.syncShowHappyPointWithCustomer(role, userSettings, customerSettings);
    userSettingsService.updateUserSettings(toUpdate);
  }

  private Optional<String> getExteralOrganisationName(EshopUser eshopUser) {
    return eshopUser.firstOrganisation()
        .map(Organisation::getId)
        .map(orgId -> Optional.ofNullable(
            extOrgRepo.findExternalCustomerNameByOrgIdAndExternalApp(orgId, ExternalApp.DVSE)))
        .orElseGet(Optional::empty);
  }

  @Override
  @Transactional
  public void saveUserSetting(BackOfficeUserSettingDto backOfficeUserSettingDto)
      throws UserValidationException {
    final EshopUser eshopUser =
        eshopUserRepo.findById(Long.valueOf(backOfficeUserSettingDto.getUserId())).orElseThrow(
            () -> new UserValidationException(UserErrorCase.UE_NFU_001, "Invalid User Id."));
    BackOfficeUsernameDuplicationCriteria criteria =
        new BackOfficeUsernameDuplicationCriteria(backOfficeUserSettingDto, eshopUser);

    backOfficeUsernameDuplicationValidator.validate(criteria);
    eshopUser.setUsername(backOfficeUserSettingDto.getUserName());
    final Salutation salutation =
        salutationService.getById(backOfficeUserSettingDto.getSalutationId());
    if (salutation != null) {
      eshopUser.setSalutation(salutation);
    }
    eshopUser.setFirstName(backOfficeUserSettingDto.getFirstName());
    eshopUser.setLastName(backOfficeUserSettingDto.getLastName());
    eshopUser.setEmail(backOfficeUserSettingDto.getEmail());
    eshopUser.setPhone(backOfficeUserSettingDto.getTelephone());
    eshopUser.setFax(backOfficeUserSettingDto.getFax());
    eshopUser.setHourlyRate(backOfficeUserSettingDto.getHourlyRate());
    final Language language = languageService.getOneById(backOfficeUserSettingDto.getLanguageId());
    if (language != null) {
      eshopUser.setLanguage(language);
    }

    final UserSettings userSettings = eshopUser.getUserSetting();
    userSettings.setEmailNotificationOrder(backOfficeUserSettingDto.getEmailNotificationOrder());
    userSettings.setNetPriceConfirm(backOfficeUserSettingDto.getNetPriceConfirm());
    userSettings.setCurrentStateNetPriceView(backOfficeUserSettingDto.getNetPriceView());
    userSettings.setNetPriceView(backOfficeUserSettingDto.getNetPriceView());
    userSettings.setShowDiscount(backOfficeUserSettingDto.getShowDiscount());
    userSettings.setDeliveryId(backOfficeUserSettingDto.getDeliveryId());
    userSettings.setCollectiveDelivery(backOfficeUserSettingDto.getCollectiveDelivery());
    paymentMethodService.getPaymentMethodById(backOfficeUserSettingDto.getPaymentId())
        .ifPresent(userSettings::setPaymentMethod);

    final Organisation organisation = eshopUser.firstOrganisation().orElse(null);
    if (organisation != null) {

      final Optional<EshopGroup> eshopGroup = eshopGroupRepo.findOneByOrgCodeAndRoleId(
          organisation.getOrgCode(), backOfficeUserSettingDto.getTypeId());
      final GroupUser groupUser = groupUserService.findOneByEshopUser(eshopUser);
      if (eshopGroup.isPresent() && groupUser != null) {
        userSettingsService.updateShowHappyPointForUser(eshopUser, eshopGroup.get());
        groupUser.setEshopGroup(eshopGroup.get());
        groupUser.setEshopUser(eshopUser);
        groupUserService.saveGroupUser(groupUser);
      }
    }
    eshopUser.setUserSetting(userSettings);

    if(Objects.nonNull(backOfficeUserSettingDto.getIsUserActive())) {
      final Login userLogin = eshopUser.getLogin();
      userLogin.setUserActive(backOfficeUserSettingDto.getIsUserActive());
      eshopUser.setLogin(userLogin);
    }

    userService.saveEshopUser(eshopUser);
  }

  private void getUserPropertiesAndsetting(final EshopUser eshopUser,
      final BackOfficeUserDto backOfficeUserDto, final Organisation organisation) {

    final UserSettings userSettings = eshopUser.getUserSetting();
    final UserSettingsDto userSettingsDto = new UserSettingsDto();
    SagBeanUtils.copyProperties(userSettings, userSettingsDto);
    userSettingsDto.setPaymentId(userSettings.getPaymentMethod().getId());
    userSettingsDto.setInvoiceId(userSettings.getInvoiceType().getId());

    final String hourlyDate =
        eshopUser.getHourlyRate() == null ? StringUtils.EMPTY : eshopUser.getHourlyRate().toString();
    backOfficeUserDto.setHourlyRate(hourlyDate);
    backOfficeUserDto.setEmail(eshopUser.getEmail());
    backOfficeUserDto.setTelephone(eshopUser.getPhone());
    backOfficeUserDto.setOrgCode(organisation.getOrgCode());
    backOfficeUserDto.setUserSettingsDto(userSettingsDto);
    backOfficeUserDto.setCompanyName(organisation.getName());
    backOfficeUserDto.setTypes(roleService.getAllRoleDto());
    backOfficeUserDto.setSalutationId(eshopUser.getSalutation().getId());
    backOfficeUserDto.setLanguageId(eshopUser.getLanguage().getId());
    backOfficeUserDto.setUserName(eshopUser.getUsername());
    backOfficeUserDto.setFax(eshopUser.getFax());
    backOfficeUserDto.setFirstName(eshopUser.getFirstName());
    backOfficeUserDto.setLastName(eshopUser.getLastName());
    backOfficeUserDto.setAffiliate(organisationRepo.findAffiliateByOrgId(organisation.getId()));
    backOfficeUserDto.setType(eshopUser.getType());
    roleService.findRoleByName(eshopUser.getRoles().get(0))
        .map(EshopRole::getId)
        .ifPresent(backOfficeUserDto::setTypeId);
  }

  @Override
  public List<ExportingUserDto> getExportingUsers(UserExportRequest request) {

    List<VUserExport> users = vUserExportRepo.findAll(
        VUserExportSpecification.of(VUserExportSearchCriteria.fromUserExportRequest(request)));
    if (CollectionUtils.isEmpty(users)) {
      return Collections.emptyList();
    }
    return users.stream().map(item -> ExportingUserDto.builder()
        .customerNumber(item.getOrgCode())
        .dvseCustomerName(item.getExternalCustomerName())
        .dvseUserName(item.getExternalUserName())
        .userName(item.getUserName())
        .firstName(item.getFirstName())
        .lastName(item.getLastName())
        .email(item.getUserEmail())
        .zip(item.getZipcode())
        .firstLoginDate(item.getFirstLoginDate())
        .lastLoginDate(item.getLastLoginDate())
        .affiliateShortname(item.getOrgParentShortName())
        .roleName(item.getRoleName())
        .salutation(item.getSalutation())
        .language(item.getLangiso())
        .build()).collect(Collectors.toList());

  }
}
