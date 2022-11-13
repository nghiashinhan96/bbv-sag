package com.sagag.services.service.customer.registration;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.eshop.service.validator.criteria.UserProfileValidateCriteria;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.service.mail.RegisterAccountCriteria;
import com.sagag.services.service.mail.RegisterUserMailSender;
import com.sagag.services.service.user.password.SecurityCodeGenerator;
import com.sagag.services.service.utils.EshopUserUtils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class FirstCustomerRegistrationHandler extends AbstractCustomerRegistrationHandler {

  private static final String DF_AFFILIATE_EMAIL =
      SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName();

  @Autowired
  private SecurityCodeGenerator securityCodeGenerator;

  @Autowired
  private RegisterUserMailSender registerUserMailSender;

  @Override
  protected void createCustomerAdminUser(UserRegistrationDto userRegistration,
      final String defaultPassword, final SupportedAffiliate affiliate,
      final Organisation customerOrg, boolean isDvseCustomer, final String language)
      throws UserValidationException, MdmCustomerNotFoundException {
    // Create user admin user of customer
    final EshopUser adminUser = createUserAdminUserAndDvseUser(userRegistration, defaultPassword,
        affiliate, customerOrg, language);

    // Send mail
    final PasswordResetToken passwordToken = securityCodeGenerator.generateCode(adminUser);
    final String collectionShortname = affiliate.getAffiliate();
    final String affiliateEmail = orgCollectionService
        .findSettingValueByCollectionShortnameAndKey(collectionShortname, DF_AFFILIATE_EMAIL)
        .orElseThrow(() -> new UserValidationException(UserErrorCase.UE_IEM_001,
            "Not found the affiliate email in organisation setting"));
    final String userEmail = adminUser.getEmail();
    final String userName = adminUser.getUsername();
    final String affCompName = affiliate.getCompanyName();
    final String redirectUrl = EshopUserUtils.buildRedirectUrl(userRegistration.getAccessUrl(),
        passwordToken.getToken(), passwordToken.getHashUsernameCode());
    sendRegistrationUserMail(userEmail, userName, language, affCompName, affiliateEmail,
        redirectUrl, isDvseCustomer);
  }

  @Override
  protected EshopUser createUserAdminUserAndDvseUser(UserRegistrationDto userRegistrationDto,
      String defaultPassword, SupportedAffiliate affiliate, Organisation customer, String langISO)
      throws UserValidationException, MdmCustomerNotFoundException {
    final UserProfileDto userProfileDto =
        SagBeanUtils.map(userRegistrationDto, UserProfileDto.class);
    final UserProfileValidateCriteria validateCriteria =
        new UserProfileValidateCriteria(userProfileDto, affiliate.getAffiliate());
    userProfileValidator.validateWithUserValidationException(validateCriteria);

    final String rawPassword = StringUtils.defaultIfBlank(defaultPassword,
        RandomStringUtils.randomAlphanumeric(SagConstants.DEFAULT_MIN_LENGTH_PASSWORD));
    return createEshopUserByProfile(userProfileDto, rawPassword, Optional.empty(), customer,
        affiliate, langISO);
  }

  private void sendRegistrationUserMail(final String userEmail, final String userName,
      final String langISO, final String affiliateCompName, final String affiliateEmail,
      final String accessUrl, boolean isDvseCustomer) {

    if (StringUtils.isBlank(userEmail)) {
      return;
    }

    final RegisterAccountCriteria criteria = RegisterAccountCriteria.builder().username(userName)
        .accessUrl(accessUrl).companyName(affiliateCompName).email(userEmail)
        .affiliateEmail(affiliateEmail).locale(localeContextHelper.toLocale(langISO)).build();
    registerUserMailSender.createSendConfirmEmailJob(criteria, isDvseCustomer);
  }
}
