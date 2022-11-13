package com.sagag.services.service.user.password.change;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.PasswordHash;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.repo.password.DefaultPasswordHashBuilder;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.security.CompositePasswordEncoder;
import com.sagag.services.common.validator.password.NewPasswordAndOldPasswordValidator;
import com.sagag.services.common.validator.password.PasswordFormatValidator;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;
import com.sagag.services.service.mail.ChangePasswordCriteria;
import com.sagag.services.service.mail.ChangePasswordMailSender;

import org.springframework.beans.factory.annotation.Autowired;

public class AbstractPasswordUserHandler {

  private static final String MISSING_DEFAULT_EMAIL = "Missing default email";

  protected static final String SAG = "sag";

  @Autowired
  protected LoginService loginService;

  @Autowired
  protected ChangePasswordMailSender changePasswordMailSender;

  @Autowired
  protected UserService userService;

  @Autowired
  protected PasswordFormatValidator passwordFormatValidator;

  @Autowired
  protected NewPasswordAndOldPasswordValidator newPasswordAndOldPasswordValidator;

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  protected CompositePasswordEncoder passwordEncoder;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Autowired
  private DefaultPasswordHashBuilder passwordHashBuilder;

  protected PasswordHash newPasswordHash(final String password, final boolean updateBySysAdmin) {
    if (updateBySysAdmin) {
      return passwordHashBuilder.buildPasswordHash(password, HashType.BCRYPT);
    }
    return passwordHashBuilder.buildPasswordHash(password);
  }

  protected ChangePasswordCriteria buildSelfChangePasswordEmail(long userId,
      final String affiliateEmail, final String redirectUrl, final String genCode,
      final boolean isFinalUser) {
    return buildChangePasswordEmailCriteria(userId, affiliateEmail, redirectUrl, genCode, false,
        isFinalUser, true);
  }

  protected ChangePasswordCriteria buildOnbehalfChangePasswordEmail(long userId,
      final String affiliateEmail, final String redirectUrl, final String genCode,
      final boolean isFinalUser) {
    return buildChangePasswordEmailCriteria(userId, affiliateEmail, redirectUrl, genCode, true,
        isFinalUser, false);
  }

  private ChangePasswordCriteria buildChangePasswordEmailCriteria(long userId,
      final String affiliateEmail, final String redirectUrl, final String genCode,
      final boolean updatedByAdmin, final boolean isFinalUser, final boolean isUpdatedPass) {
    final EshopUser eshopUser = userService.getUserById(userId);
    return ChangePasswordCriteria.builder().toEmail(eshopUser.getEmail())
        .affiliateEmail(affiliateEmail).username(eshopUser.getUsername()).redirectUrl(redirectUrl)
        .code(genCode).locale(localeContextHelper.toLocale(eshopUser.getLangiso()))
        .isUpdatedByAdmin(updatedByAdmin)
        .isFinalUser(isFinalUser)
        .changePassOk(isUpdatedPass).build();
  }

  protected String getDefaultAffiliateEmail(EshopUserLoginDto criteria, boolean isAdmin,
      UserInfo user) {
    if (criteria.isFinalUser()) {
      return user.getSettings().getEhDefaultEmail();
    }
    if (isAdmin) {
      return getSagEmail();
    }
    return user.getSettings().getAffiliateEmail();
  }

  protected String getSagDefaultEmail() {
    return getSagEmail();
  }

  private String getSagEmail() {
    return orgCollectionService.findSettingValueByCollectionShortnameAndKey(SAG,
        SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName())
      .orElseThrow(() -> new IllegalArgumentException(MISSING_DEFAULT_EMAIL));
  }
}
