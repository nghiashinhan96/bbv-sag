package com.sagag.services.oauth2.api.impl.user;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.oauth2.exception.UnsupportedAffiliateException;
import com.sagag.services.oauth2.model.VisitRegistration;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import com.sagag.services.oauth2.profiles.CloudDmsExternalAuthenticatorMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CloudDmsExternalAuthenticatorMode
@Slf4j
public class CloudDmsUserDetailsServiceImpl extends AbstractEshopUserDetailsService {

  private final UserCacheService userCacheService;

  @Autowired
  public CloudDmsUserDetailsServiceImpl(final UserCacheService userCacheService) {
    this.userCacheService = userCacheService;
  }

  @Override
  @Transactional
  @LogExecutionTime
  public UserDetails loadUserByUsername(String username) {
    log.debug("Authenticating cloud dms user with username = {}", username);
    final String affiliate = eshopAuthHelper.getLoginAffiliate();

    final VisitRegistration body = eshopAuthHelper.getVisitRegistrationBody();
    final String language = body.getLanguage();

    EshopUser user =
        search(username, affiliate).orElseThrow(() -> new UnsupportedAffiliateException(affiliate));

    if (isLanguageChanged(user, language)) {
      user = userService.updateUserLanguage(user, language);
      userCacheService.remove(user.getId());
    }

    doUpdateDateTimeOnLogin(user, NO_SUPPORTED_SALES_LOGIN);
    return EshopUserDetails.buildCloudDmsUserDetails(user, eshopAuthHelper.getLocatedAffiliate());
  }

  @Override
  public boolean support(String affiliate) {
    return eshopAuthHelper.isCloudDmsLogin();
  }
}
