package com.sagag.services.oauth2.api.impl.user;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.AutonetUserService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.AutonetCompanyUtils;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.oauth2.model.VisitRegistration;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import com.sagag.services.oauth2.profiles.AutonetExternalAuthenticatorMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * This service handles user info, and it's setting on authorization.
 */
@Service
@AutonetExternalAuthenticatorMode
@Slf4j
public class AutonetUserDetailsServiceImpl extends AbstractEshopUserDetailsService {

  private final AutonetUserService autonetUserService;

  private final UserCacheService userCacheService;

  @Autowired
  public AutonetUserDetailsServiceImpl(final UserCacheService userCacheService,
    AutonetUserService autonetUserService) {
    this.autonetUserService = autonetUserService;
    this.userCacheService = userCacheService;
  }

  @Override
  @Transactional
  @LogExecutionTime
  public UserDetails loadUserByUsername(String username) {
    log.debug("Authenticating autonet user with username = {}", username);
    final VisitRegistration body = eshopAuthHelper.getVisitRegistrationBody();
    final String affiliate = body.getCompanyID();
    final String language = body.getLanguage();

    final EshopUser user = searchOrCreateNewEshopUser(username, affiliate, language);
    doUpdateDateTimeOnLogin(user, NO_SUPPORTED_SALES_LOGIN);
    return EshopUserDetails.buildAutonetUserDetails(user, eshopAuthHelper.getLocatedAffiliate());
  }

  private EshopUser searchOrCreateNewEshopUser(String username, String affiliate, String language) {
    final Optional<EshopUser> userOpt = search(username, affiliate);
    if (!userOpt.isPresent()) {
      return autonetUserService.createAutonetUser(username, language, affiliate);
    }

    EshopUser user = userOpt.get();
    if (isLanguageChanged(user, language)) {
      user = userService.updateUserLanguage(user, language);
      userCacheService.remove(user.getId());
    }
    return user;
  }

  @Override
  protected Optional<EshopUser> search(String username, String affiliate) {
    log.debug("Returning autonet user info by username = {} - affiliate = {}", username,
        affiliate);
    try {
      return userSearchFactory.searchUsernameCaseSensitive(username, affiliate);
    } catch (UserValidationException e) {
      return Optional.empty();
    }
  }

  @Override
  public boolean support(String affiliate) {
    log.debug("Checking the affiliate = {} is match autonet user mode", affiliate);
    return AutonetCompanyUtils.isAutonetEndpoint(affiliate);
  }
}
