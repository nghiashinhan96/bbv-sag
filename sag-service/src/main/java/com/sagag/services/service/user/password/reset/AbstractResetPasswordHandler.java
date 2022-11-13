package com.sagag.services.service.user.password.reset;

import com.sagag.eshop.repo.api.forgotpassword.PasswordResetTokenRepository;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.services.common.locale.LocaleContextHelper;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

public class AbstractResetPasswordHandler {

  @Autowired
  protected OrganisationService orgService;

  @Autowired
  protected PasswordResetTokenRepository passwordTokenRepo;

  @Autowired
  protected LocaleContextHelper localeContextHelper;

  protected String findDefaultAffiliateEmail(final String shortName, final boolean isFinalUser) {
    SettingsKeys.Affiliate.Settings affDefaultEmail = SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL;
    if (isFinalUser) {
      affDefaultEmail = SettingsKeys.Affiliate.Settings.EH_DEFAULT_EMAIL;
    }
    return orgService.findOrgSettingByKey(shortName, affDefaultEmail.toLowerName())
        .orElseThrow(() -> new NoSuchElementException("Not found this affiliate id"));
  }
}
