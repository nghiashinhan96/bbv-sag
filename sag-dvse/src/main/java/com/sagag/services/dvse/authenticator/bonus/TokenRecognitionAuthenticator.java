package com.sagag.services.dvse.authenticator.bonus;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.user.UserSearchFactory;
import com.sagag.services.common.authenticator.IAuthenticator;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.dvse.dto.bonus.ValidatedRecognition;
import com.sagag.services.incentive.authcookie.AuthCookie;
import com.sagag.services.incentive.authcookie.AuthCookieFactory;
import com.sagag.services.incentive.authcookie.CookieField;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenRecognitionAuthenticator implements IAuthenticator<String, ValidatedRecognition> {

  private static final String LOG_INFO = "- username = {} \n- accessPoint = {}";

  @Autowired
  private UserSearchFactory userSearchFactory;
  @Autowired
  private LoginRepository loginRepo;
  @Autowired
  private OrganisationService orgService;
  @Autowired
  private SupportedAffiliateRepository supportedAffRepo;
  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Override
  @Transactional
  public ValidatedRecognition authenticate(String token, String... args) {
    log.debug("Validating HappyBonus Token = {}", token);
    if (StringUtils.isBlank(token)) {
      final String msg = "Token must not be empty";
      log.debug(msg);
      throw new IllegalArgumentException(msg);
    }

    final AuthCookie cookie = AuthCookieFactory.decodeAndValidateLoginToken(token);
    if (!cookie.isValid()) {
      log.debug("Token expired");
      throw new IllegalArgumentException("Token timeout after 1 hour !");
    }

    final String username = cookie.getPayload(CookieField.USER_NAME);
    final String accessPoint = cookie.getPayload(CookieField.AP_KEY);
    log.info(LOG_INFO, username, accessPoint);

    final Optional<String> affiliateOpt = supportedAffRepo.findShortNameByEsShortName(accessPoint);
    if (!affiliateOpt.isPresent()) {
      final String msg = String.format("Not found supported affiliate by access point = %s",
          accessPoint);
      log.debug(msg);
      throw new IllegalArgumentException(msg);
    }

    final String affiliate = affiliateOpt.get();
    final Optional<EshopUser> eshopUserOpt = findEshopUser(username, affiliate);
    if (!eshopUserOpt.isPresent()) {
      final String msg = String.format("Not found username %s and affiliate = %s",
          username, affiliate);
      log.debug(msg);
      throw new IllegalArgumentException(msg);
    }

    final EshopUser eshopUser = eshopUserOpt.get();
    final Long userId = eshopUser.getId();
    final Optional<Login> userLoginOpt = loginRepo.findByUserId(userId);
    if (!userLoginOpt.isPresent()) {
      final String msg = "Not found user login info";
      log.debug(msg);
      throw new IllegalArgumentException(msg);
    }

    final Optional<Organisation> orgCustomerOpt = orgService.getFirstByUserId(userId);
    if (!orgCustomerOpt.isPresent()) {
      final String msg = "Not found customer";
      log.debug(msg);
      throw new IllegalArgumentException(msg);
    }

    return ValidatedRecognitionBuilder.build(eshopUser, userLoginOpt.get(), orgCustomerOpt.get(),
        accessPoint, token, localeContextHelper.toLocale(eshopUser.getLanguage().getLangiso()));
  }

  private Optional<EshopUser> findEshopUser(final String username,
      final String affiliate) {
    try {
      return userSearchFactory.searchUsernameCaseSensitive(username, affiliate);
    } catch (UserValidationException e) {
      log.error("Searching username has error", e);
      return Optional.empty();
    }
  }
}
