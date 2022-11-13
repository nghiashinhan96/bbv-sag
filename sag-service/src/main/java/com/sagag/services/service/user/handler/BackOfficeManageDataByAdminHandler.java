package com.sagag.services.service.user.handler;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate;
import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.OrganisationNotFoundException;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.dto.BackOfficeUserProfileDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.service.api.DvseBusinessService;
import com.sagag.services.service.mail.RegisterAccountCriteria;
import com.sagag.services.service.mail.RegisterUserMailSender;
import com.sagag.services.service.user.password.SecurityCodeGenerator;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
public class BackOfficeManageDataByAdminHandler extends AbstractUserHandler {

  @Autowired
  private OrganisationService orgService;

  @Autowired
  private DvseBusinessService dvseBusinessService;

  @Autowired
  private SecurityCodeGenerator generator;

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private RegisterUserMailSender registerUserMailSender;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  public void createUserBySystemAdmin(BackOfficeUserProfileDto user)
      throws ValidationException, MdmCustomerNotFoundException {
    final String genPassword =
        RandomStringUtils.randomAlphanumeric(SagConstants.DEFAULT_MIN_LENGTH_PASSWORD);

    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(user.getAffiliateShortName());

    final String custNr = String.valueOf(user.getCustomerNumber());
    final Organisation org = orgService.findOrganisationByOrgCode(custNr)
        .orElseThrow(OrganisationNotFoundException::new);

    final UserInfo userInfo = new UserInfo();
    userInfo.setAffiliateShortName(user.getAffiliateShortName());
    userInfo.setCompanyName(affiliate.getCompanyName());
    userInfo.setAddresses(
        customerExtService.searchCustomerAddresses(userInfo.getCompanyName(), custNr));
    userInfo.setOrganisationId(org.getId());
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(Customer.builder().nr(user.getCustomerNumber())
        .companyName(affiliate.getCompanyName()).sendMethodCode(user.getSendMethodCode()).build());
    userInfo.setSettings(ownSettings);

    final UserProfileDto profileDto = user.getUserProfileDto();

    // Create user
    final EshopUser newEshopUser = userService.createUser(profileDto, userInfo, genPassword);
    final boolean isDvseCustomer = orgService.isDvseCustomer(org.getId());

    if (isDvseCustomer) {
      dvseBusinessService.createDvseUserInfo(newEshopUser.getId(), affiliate, org.getId());
    }

    // Sent Email
    PasswordResetToken passwordToken = generator.generateCode(newEshopUser);
    Locale local = localeContextHelper.toLocale(newEshopUser.getLanguage().getLangiso());

    final List<String> settingKeys = Arrays.asList(Affiliate.Settings.DEFAULT_EMAIL.toLowerName(),
        Affiliate.Settings.DEFAULT_URL.toLowerName());
    final Map<String, String> settingValues = orgCollectionService.getCollectionByOrgId(org.getId())
        .map(collection -> orgCollectionService
            .findSettingValuesByCollectionShortnameAndKeys(collection.getShortname(), settingKeys))
        .orElseThrow(() -> new IllegalArgumentException("Collection Setting not found"));

    final String affiliateEmail =
        Optional.ofNullable(settingValues.get(Affiliate.Settings.DEFAULT_EMAIL.toLowerName()))
            .orElseThrow(() -> new IllegalArgumentException("Missing default email"));

    final String affiliateUrl =
        Optional.ofNullable(settingValues.get(Affiliate.Settings.DEFAULT_URL.toLowerName()))
            .orElseThrow(() -> new IllegalArgumentException("Missing default collection url"));

    profileDto.setAccessUrl(affiliateUrl + Customer.SUB_URL_FORGOT_PWORD_VERIFYCODE);
    final RegisterAccountCriteria criteria =
        RegisterAccountCriteria.builder().username(profileDto.getUserName())
            .accessUrl(profileDto.buildRedirectUrl(passwordToken.getToken(),
                passwordToken.getHashUsernameCode()))
            .companyName(affiliate.getCompanyName()).email(profileDto.getEmail())
            .affiliateEmail(affiliateEmail).locale(local).build();
    registerUserMailSender.createSendConfirmEmailJob(criteria, isDvseCustomer);
  }

}
