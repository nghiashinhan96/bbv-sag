package com.sagag.services.service.user.handler;

import com.sagag.eshop.repo.api.ExternalOrganisationRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;
import com.sagag.services.domain.eshop.dto.OrganisationDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.mdm.api.DvseUserService;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.service.api.DvseBusinessService;
import com.sagag.services.service.mail.RegisterAccountCriteria;
import com.sagag.services.service.mail.RegisterUserMailSender;
import com.sagag.services.service.user.password.SecurityCodeGenerator;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * This class supports APIs for customer admin can interact with their own users.
 */

@Component
public class EshopManageDataByAdminHandler extends AbstractUserHandler {

  @Autowired
  private RegisterUserMailSender registerUserMailSender;

  @Autowired
  private SecurityCodeGenerator generator;

  @Autowired
  private DvseUserService dvseUserService;

  @Autowired
  private ExternalUserService externalUserService;

  @Autowired
  private DvseBusinessService dvseBusinessService;

  @Autowired
  private AddressFilterService addressFilterService;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  private ExternalOrganisationRepository extOrganisationRepo;

  public void createUser(UserInfo adminUser, UserProfileDto userProfile)
      throws ValidationException, MdmCustomerNotFoundException {
    OrganisationDto target = OrganisationDto.builder().id(adminUser.getOrganisationId())
        .orgCode(adminUser.getCustNrStr()).build();
    createUser(adminUser, userProfile, target);
  }

  public void createUser(UserInfo adminUser, UserProfileDto userProfile,
      OrganisationDto targetCustomer) throws ValidationException, MdmCustomerNotFoundException {
    // Generate and encrypt new password
    final String genPassword =
        RandomStringUtils.randomAlphanumeric(SagConstants.DEFAULT_MIN_LENGTH_PASSWORD);
    final EshopUser newEshopUser =
        userService.createUserForOtherCustomer(userProfile, adminUser, genPassword, targetCustomer);
    // If user created for DDAT, Matik-AT affiliate, create DVSE account for this
    final SupportedAffiliate affiliate =
        SupportedAffiliate.fromDesc(adminUser.getAffiliateShortName());
    final int customerOrgId = targetCustomer.getId();
    boolean isDvseCustomer = organisationService.isDvseCustomer(customerOrgId);

    if (isDvseCustomer) {
      dvseBusinessService.createDvseUserInfo(newEshopUser.getId(), affiliate, customerOrgId);
    }
    PasswordResetToken passwordToken = generator.generateCode(newEshopUser);
    Locale local = localeContextHelper.toLocale(newEshopUser.getLanguage().getLangiso());
    final String affiliateEmail = WholesalerUtils.isFinalCustomer(targetCustomer.getOrgCode())
        ? adminUser.getSettings().getEhDefaultEmail()
        : adminUser.getSettings().getAffiliateEmail();

    final RegisterAccountCriteria criteria =
        RegisterAccountCriteria.builder().username(userProfile.getUserName())
            .accessUrl(userProfile.buildRedirectUrl(passwordToken.getToken(),
                passwordToken.getHashUsernameCode()))
            .companyName(adminUser.getCompanyName()).email(userProfile.getEmail())
            .affiliateEmail(affiliateEmail)
            .isFinalUser(WholesalerUtils.isFinalCustomer(targetCustomer.getOrgCode())).locale(local)
            .build();

    this.registerUserMailSender.createSendConfirmEmailJob(criteria, isDvseCustomer);
    // @formatter:on
  }

  public PaymentSettingDto getPaymentSetting(UserInfo user, Long requestUserId) {
    final PaymentSettingDto paymentSettings =
        userPaymentSettingsFormBuilder.buildUserPaymentSetting(requestUserId, false);
    paymentSettings.setAddresses(user.getAddresses());
    paymentSettings
        .setBillingAddresses(addressFilterService.getBillingAddresses(user.getAddresses()));
    return paymentSettings;
  }

  public UserSettingsDto getUserSettings(UserInfo user, Long requestUserId) {
    final CustomerSettings customerSettings =
        custSettingsService.findSettingsByOrgId(user.getOrganisationId());

    final String roleName = getUserRoleName(requestUserId);
    return userSettingsService.getUserSettings(customerSettings, requestUserId, roleName);
  }

  public String getUserRoleName(long userId) {
    final List<String> roles = eshopRoleRepository.findRolesFromUserId(userId);
    Assert.notEmpty(roles, "User has not any role!");
    return roles.get(0);
  }

  public void deactiveUserById(final Long deletedUserId) throws UserValidationException {
    boolean isAllowedToDeleteDvseUser = isAffiliateAllowedToDeleteDvseUser(deletedUserId);
    // Delete Dvse and external user if affiliate is belongs to Austria
    if (isAllowedToDeleteDvseUser) {
      final Integer orgId = userService.getOrgIdByUserId(deletedUserId).orElseThrow(
          () -> new UserValidationException(UserErrorCase.UE_NFO_001, "Not found organisation info"));
      doDeleteInternalDvseExternalUserById(deletedUserId, orgId);
    }
    // Deactive user id
    userService.deactiveUserById(deletedUserId);

  }

  private String getAffiliateShortname(final Long deletedUserId) {
    Optional<String> affiliateShortname =
        this.userService.findAffiliateShortNameById(deletedUserId);
    if (!affiliateShortname.isPresent()) {
      throw new IllegalArgumentException("User does not exist in any affiliates");
    }
    return affiliateShortname.get();
  }

  private boolean isAffiliateAllowedToDeleteDvseUser(final Long userId) {
    return userService.hasPermission(userId, PermissionEnum.DVSE);
  }

  private void doDeleteInternalDvseExternalUserById(final Long userId, int orgId) {
    final ExternalUserDto extUser =
        externalUserService.getDvseExternalUserByUserId(userId).orElse(null);
    if (extUser == null) {
      // #5155 finding 2 ignore external user deletion process if not found
      return;
    }

    final String extCustomerId = extOrganisationRepo
        .findExternalCustomerIdByOrgIdAndExternalApp(orgId, ExternalApp.DVSE);
    final String extUsername = extUser.getUsername();
    final String extPassword = extUser.getPassword();
    dvseUserService.removeUser(extCustomerId, extUsername, extPassword);
    externalUserService.removeExternalUserById(extUser.getId());
  }

  public void deleteDvseExternalUserById(final Long deletedUserId)
      throws UserValidationException, MdmCustomerNotFoundException {
    final String affiliateShortname = getAffiliateShortname(deletedUserId);
    final Integer orgId = userService.getOrgIdByUserId(deletedUserId).orElseThrow(
        () -> new UserValidationException(UserErrorCase.UE_NFO_001, "Not found organisation info"));
    // Delete DVSE and external user if affiliate is belongs to Austria
    if (isAffiliateAllowedToDeleteDvseUser(deletedUserId)) {
      doDeleteInternalDvseExternalUserById(deletedUserId, orgId);
    }
    // Generate DVSE user automatically for next login process
    dvseBusinessService.createDvseUserInfo(deletedUserId,
        SupportedAffiliate.fromDesc(affiliateShortname), orgId);
  }
}
